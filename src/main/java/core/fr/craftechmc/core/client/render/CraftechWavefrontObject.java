package fr.craftechmc.core.client.render;

import com.google.common.base.Charsets;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.IResource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModelCustom;
import net.minecraftforge.client.model.ModelFormatException;
import net.minecraftforge.client.model.obj.Face;
import net.minecraftforge.client.model.obj.GroupObject;
import net.minecraftforge.client.model.obj.TextureCoordinate;
import net.minecraftforge.client.model.obj.Vertex;
import org.lwjgl.opengl.GL11;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CraftechWavefrontObject implements IModelCustom
{
    private static Pattern              vertexPattern            = Pattern
            .compile("(v( (\\-){0,1}\\d+(\\.\\d+)?){3,4} *\\n)|(v( (\\-){0,1}\\d+(\\.\\d+)?){3,4} *$)");
    private static Pattern              vertexNormalPattern      = Pattern
            .compile("(vn( (\\-){0,1}\\d+(\\.\\d+)?){3,4} *\\n)|(vn( (\\-){0,1}\\d+(\\.\\d+)?){3,4} *$)");
    private static Pattern              textureCoordinatePattern = Pattern
            .compile("(vt( (\\-){0,1}\\d+\\.\\d+){2,3} *\\n)|(vt( (\\-){0,1}\\d+(\\.\\d+)?){2,3} *$)");
    private static Pattern              face_V_VT_VN_Pattern     = Pattern
            .compile("(f( \\d+/\\d+/\\d+){3,4} *\\n)|(f( \\d+/\\d+/\\d+){3,4} *$)");
    private static Pattern              face_V_VT_Pattern        = Pattern
            .compile("(f( \\d+/\\d+){3,4} *\\n)|(f( \\d+/\\d+){3,4} *$)");
    private static Pattern              face_V_VN_Pattern        = Pattern
            .compile("(f( \\d+//\\d+){3,4} *\\n)|(f( \\d+//\\d+){3,4} *$)");
    private static Pattern              face_V_Pattern           = Pattern
            .compile("(f( \\d+){3,4} *\\n)|(f( \\d+){3,4} *$)");
    private static Pattern              groupObjectPattern       = Pattern
            .compile("([go]( [\\w\\d\\.]+) *\\n)|([go]( [\\w\\d\\.]+) *$)");

    private static Matcher              vertexMatcher, vertexNormalMatcher, textureCoordinateMatcher;
    private static Matcher              face_V_VT_VN_Matcher, face_V_VT_Matcher, face_V_VN_Matcher, face_V_Matcher;
    private static Matcher              groupObjectMatcher;

    public ArrayList<Vertex>            vertices                 = new ArrayList<>();
    public ArrayList<Vertex>            vertexNormals            = new ArrayList<>();
    public ArrayList<TextureCoordinate> textureCoordinates       = new ArrayList<>();
    public ArrayList<GroupObject>       groupObjects             = new ArrayList<>();
    private GroupObject                 currentGroupObject;
    private final String                fileName;

    public CraftechWavefrontObject(final ResourceLocation resource) throws ModelFormatException
    {
        this.fileName = resource.toString();

        try
        {
            final IResource res = Minecraft.getMinecraft().getResourceManager().getResource(resource);
            this.loadObjModel(res.getInputStream());
        } catch (final IOException e)
        {
            throw new ModelFormatException("IO Exception reading model format", e);
        }
    }

    public CraftechWavefrontObject(final String filename, final InputStream inputStream) throws ModelFormatException
    {
        this.fileName = filename;
        this.loadObjModel(inputStream);
    }

    private void loadObjModel(final InputStream inputStream) throws ModelFormatException
    {
        BufferedReader reader = null;

        String currentLine = null;
        int lineCount = 0;

        try
        {
            reader = new BufferedReader(new InputStreamReader(inputStream, Charsets.UTF_8));

            while ((currentLine = reader.readLine()) != null)
            {
                lineCount++;
                currentLine = currentLine.replaceAll("\\s+", " ").trim();

                if (currentLine.startsWith("#") || currentLine.length() == 0)
                {
                    continue;
                }
                else if (currentLine.startsWith("v "))
                {
                    final Vertex vertex = this.parseVertex(currentLine, lineCount);
                    if (vertex != null)
                    {
                        this.vertices.add(vertex);
                    }
                }
                else if (currentLine.startsWith("vn "))
                {
                    final Vertex vertex = this.parseVertexNormal(currentLine, lineCount);
                    if (vertex != null)
                    {
                        this.vertexNormals.add(vertex);
                    }
                }
                else if (currentLine.startsWith("vt "))
                {
                    final TextureCoordinate textureCoordinate = this.parseTextureCoordinate(currentLine, lineCount);
                    if (textureCoordinate != null)
                    {
                        this.textureCoordinates.add(textureCoordinate);
                    }
                }
                else if (currentLine.startsWith("f "))
                {

                    if (this.currentGroupObject == null)
                    {
                        this.currentGroupObject = new GroupObject("Default");
                    }

                    final Face face = this.parseFace(currentLine, lineCount);

                    if (face != null)
                    {
                        this.currentGroupObject.faces.add(face);
                    }
                }
                else if (currentLine.startsWith("g ") | currentLine.startsWith("o "))
                {
                    final GroupObject group = this.parseGroupObject(currentLine, lineCount);

                    if (group != null)
                    {
                        if (this.currentGroupObject != null)
                        {
                            this.groupObjects.add(this.currentGroupObject);
                        }
                    }

                    this.currentGroupObject = group;
                }
            }

            this.groupObjects.add(this.currentGroupObject);
        } catch (final IOException e)
        {
            throw new ModelFormatException("IO Exception reading model format", e);
        } finally
        {
            try
            {
                if (reader != null)
                    reader.close();
            } catch (final IOException e)
            {
                // hush
            }

            try
            {
                inputStream.close();
            } catch (final IOException e)
            {
                // hush
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void renderAll()
    {
        final Tessellator tessellator = Tessellator.instance;

        if (this.currentGroupObject != null)
        {
            tessellator.startDrawing(this.currentGroupObject.glDrawingMode);
        }
        else
        {
            tessellator.startDrawing(GL11.GL_TRIANGLES);
        }
        this.tessellateAll(tessellator);

        tessellator.draw();
    }

    @SideOnly(Side.CLIENT)
    public void tessellateAll(final Tessellator tessellator)
    {
        for (final GroupObject groupObject : this.groupObjects)
        {
            groupObject.render(tessellator);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void renderOnly(final String... groupNames)
    {
        for (final GroupObject groupObject : this.groupObjects)
        {
            for (final String groupName : groupNames)
            {
                if (groupName.equalsIgnoreCase(groupObject.name))
                {
                    groupObject.render();
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public void tessellateOnly(final Tessellator tessellator, final String... groupNames)
    {
        for (final GroupObject groupObject : this.groupObjects)
        {
            for (final String groupName : groupNames)
            {
                if (groupName.equalsIgnoreCase(groupObject.name))
                {
                    groupObject.render(tessellator);
                }
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void renderPart(final String partName)
    {
        for (final GroupObject groupObject : this.groupObjects)
        {
            if (partName.equalsIgnoreCase(groupObject.name))
            {
                groupObject.render();
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public void tessellatePart(final Tessellator tessellator, final String partName)
    {
        for (final GroupObject groupObject : this.groupObjects)
        {
            if (partName.equalsIgnoreCase(groupObject.name))
            {
                groupObject.render(tessellator);
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void renderAllExcept(final String... excludedGroupNames)
    {
        for (final GroupObject groupObject : this.groupObjects)
        {
            boolean skipPart = false;
            for (final String excludedGroupName : excludedGroupNames)
            {
                if (excludedGroupName.equalsIgnoreCase(groupObject.name))
                {
                    skipPart = true;
                }
            }
            if (!skipPart)
            {
                groupObject.render();
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public void tessellateAllExcept(final Tessellator tessellator, final String... excludedGroupNames)
    {
        boolean exclude;
        for (final GroupObject groupObject : this.groupObjects)
        {
            exclude = false;
            for (final String excludedGroupName : excludedGroupNames)
            {
                if (excludedGroupName.equalsIgnoreCase(groupObject.name))
                {
                    exclude = true;
                }
            }
            if (!exclude)
            {
                groupObject.render(tessellator);
            }
        }
    }

    private Vertex parseVertex(String line, final int lineCount) throws ModelFormatException
    {
        final Vertex vertex = null;

        if (CraftechWavefrontObject.isValidVertexLine(line))
        {
            line = line.substring(line.indexOf(" ") + 1);
            final String[] tokens = line.split(" ");

            try
            {
                if (tokens.length == 2)
                {
                    return new Vertex(Float.parseFloat(tokens[0]), Float.parseFloat(tokens[1]));
                }
                else if (tokens.length == 3)
                {
                    return new Vertex(Float.parseFloat(tokens[0]), Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2]));
                }
            } catch (final NumberFormatException e)
            {
                throw new ModelFormatException(String.format("Number formatting error at line %d", lineCount), e);
            }
        }
        else
        {
            throw new ModelFormatException("Error parsing entry ('" + line + "'" + ", line " + lineCount + ") in file '"
                    + this.fileName + "' - Incorrect format");
        }

        return vertex;
    }

    private Vertex parseVertexNormal(String line, final int lineCount) throws ModelFormatException
    {
        final Vertex vertexNormal = null;

        if (CraftechWavefrontObject.isValidVertexNormalLine(line))
        {
            line = line.substring(line.indexOf(" ") + 1);
            final String[] tokens = line.split(" ");

            try
            {
                if (tokens.length == 3)
                    return new Vertex(Float.parseFloat(tokens[0]), Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2]));
            } catch (final NumberFormatException e)
            {
                throw new ModelFormatException(String.format("Number formatting error at line %d", lineCount), e);
            }
        }
        else
        {
            throw new ModelFormatException("Error parsing entry ('" + line + "'" + ", line " + lineCount + ") in file '"
                    + this.fileName + "' - Incorrect format");
        }

        return vertexNormal;
    }

    private TextureCoordinate parseTextureCoordinate(String line, final int lineCount) throws ModelFormatException
    {
        final TextureCoordinate textureCoordinate = null;

        if (CraftechWavefrontObject.isValidTextureCoordinateLine(line))
        {
            line = line.substring(line.indexOf(" ") + 1);
            final String[] tokens = line.split(" ");

            try
            {
                if (tokens.length == 2)
                    return new TextureCoordinate(Float.parseFloat(tokens[0]), 1 - Float.parseFloat(tokens[1]));
                else if (tokens.length == 3)
                    return new TextureCoordinate(Float.parseFloat(tokens[0]), 1 - Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2]));
            } catch (final NumberFormatException e)
            {
                throw new ModelFormatException(String.format("Number formatting error at line %d", lineCount), e);
            }
        }
        else
        {
            throw new ModelFormatException("Error parsing entry ('" + line + "'" + ", line " + lineCount + ") in file '"
                    + this.fileName + "' - Incorrect format");
        }

        return textureCoordinate;
    }

    private Face parseFace(final String line, final int lineCount) throws ModelFormatException
    {
        Face face = null;

        if (CraftechWavefrontObject.isValidFaceLine(line))
        {
            face = new Face();

            final String trimmedLine = line.substring(line.indexOf(" ") + 1);
            final String[] tokens = trimmedLine.split(" ");
            String[] subTokens = null;

            if (tokens.length == 3)
            {
                if (this.currentGroupObject.glDrawingMode == -1)
                {
                    this.currentGroupObject.glDrawingMode = GL11.GL_TRIANGLES;
                }
                else if (this.currentGroupObject.glDrawingMode != GL11.GL_TRIANGLES)
                {
                    throw new ModelFormatException("Error parsing entry ('" + line + "'" + ", line " + lineCount
                            + ") in file '" + this.fileName
                            + "' - Invalid number of points for face (expected 4, found " + tokens.length + ")");
                }
            }
            else if (tokens.length == 4)
            {
                if (this.currentGroupObject.glDrawingMode == -1)
                {
                    this.currentGroupObject.glDrawingMode = GL11.GL_QUADS;
                }
                else if (this.currentGroupObject.glDrawingMode != GL11.GL_QUADS)
                {
                    throw new ModelFormatException("Error parsing entry ('" + line + "'" + ", line " + lineCount
                            + ") in file '" + this.fileName
                            + "' - Invalid number of points for face (expected 3, found " + tokens.length + ")");
                }
            }

            // f v1/vt1/vn1 v2/vt2/vn2 v3/vt3/vn3 ...
            if (CraftechWavefrontObject.isValidFace_V_VT_VN_Line(line))
            {
                face.vertices = new Vertex[tokens.length];
                face.textureCoordinates = new TextureCoordinate[tokens.length];
                face.vertexNormals = new Vertex[tokens.length];

                for (int i = 0; i < tokens.length; ++i)
                {
                    subTokens = tokens[i].split("/");

                    face.vertices[i] = this.vertices.get(Integer.parseInt(subTokens[0]) - 1);
                    face.textureCoordinates[i] = this.textureCoordinates.get(Integer.parseInt(subTokens[1]) - 1);
                    face.vertexNormals[i] = this.vertexNormals.get(Integer.parseInt(subTokens[2]) - 1);
                }

                face.faceNormal = face.calculateFaceNormal();
            }
            // f v1/vt1 v2/vt2 v3/vt3 ...
            else if (CraftechWavefrontObject.isValidFace_V_VT_Line(line))
            {
                face.vertices = new Vertex[tokens.length];
                face.textureCoordinates = new TextureCoordinate[tokens.length];

                for (int i = 0; i < tokens.length; ++i)
                {
                    subTokens = tokens[i].split("/");

                    face.vertices[i] = this.vertices.get(Integer.parseInt(subTokens[0]) - 1);
                    face.textureCoordinates[i] = this.textureCoordinates.get(Integer.parseInt(subTokens[1]) - 1);
                }

                face.faceNormal = face.calculateFaceNormal();
            }
            // f v1//vn1 v2//vn2 v3//vn3 ...
            else if (CraftechWavefrontObject.isValidFace_V_VN_Line(line))
            {
                face.vertices = new Vertex[tokens.length];
                face.vertexNormals = new Vertex[tokens.length];

                for (int i = 0; i < tokens.length; ++i)
                {
                    subTokens = tokens[i].split("//");

                    face.vertices[i] = this.vertices.get(Integer.parseInt(subTokens[0]) - 1);
                    face.vertexNormals[i] = this.vertexNormals.get(Integer.parseInt(subTokens[1]) - 1);
                }

                face.faceNormal = face.calculateFaceNormal();
            }
            // f v1 v2 v3 ...
            else if (CraftechWavefrontObject.isValidFace_V_Line(line))
            {
                face.vertices = new Vertex[tokens.length];

                for (int i = 0; i < tokens.length; ++i)
                {
                    face.vertices[i] = this.vertices.get(Integer.parseInt(tokens[i]) - 1);
                }

                face.faceNormal = face.calculateFaceNormal();
            }
            else
            {
                throw new ModelFormatException("Error parsing entry ('" + line + "'" + ", line " + lineCount
                        + ") in file '" + this.fileName + "' - Incorrect format");
            }
        }
        else
        {
            throw new ModelFormatException("Error parsing entry ('" + line + "'" + ", line " + lineCount + ") in file '"
                    + this.fileName + "' - Incorrect format");
        }

        return face;
    }

    private GroupObject parseGroupObject(final String line, final int lineCount) throws ModelFormatException
    {
        GroupObject group = null;

        if (CraftechWavefrontObject.isValidGroupObjectLine(line))
        {
            final String trimmedLine = line.substring(line.indexOf(" ") + 1);

            if (trimmedLine.length() > 0)
            {
                group = new GroupObject(trimmedLine);
            }
        }
        else
        {
            throw new ModelFormatException("Error parsing entry ('" + line + "'" + ", line " + lineCount + ") in file '"
                    + this.fileName + "' - Incorrect format");
        }

        return group;
    }

    /***
     * Verifies that the given line from the model file is a valid vertex
     *
     * @param line
     *            the line being validated
     * @return true if the line is a valid vertex, false otherwise
     */
    private static boolean isValidVertexLine(final String line)
    {
        if (CraftechWavefrontObject.vertexMatcher != null)
        {
            CraftechWavefrontObject.vertexMatcher.reset();
        }

        CraftechWavefrontObject.vertexMatcher = CraftechWavefrontObject.vertexPattern.matcher(line);
        return CraftechWavefrontObject.vertexMatcher.matches();
    }

    /***
     * Verifies that the given line from the model file is a valid vertex normal
     *
     * @param line
     *            the line being validated
     * @return true if the line is a valid vertex normal, false otherwise
     */
    private static boolean isValidVertexNormalLine(final String line)
    {
        if (CraftechWavefrontObject.vertexNormalMatcher != null)
        {
            CraftechWavefrontObject.vertexNormalMatcher.reset();
        }

        CraftechWavefrontObject.vertexNormalMatcher = CraftechWavefrontObject.vertexNormalPattern.matcher(line);
        return CraftechWavefrontObject.vertexNormalMatcher.matches();
    }

    /***
     * Verifies that the given line from the model file is a valid texture
     * coordinate
     *
     * @param line
     *            the line being validated
     * @return true if the line is a valid texture coordinate, false otherwise
     */
    private static boolean isValidTextureCoordinateLine(final String line)
    {
        if (CraftechWavefrontObject.textureCoordinateMatcher != null)
        {
            CraftechWavefrontObject.textureCoordinateMatcher.reset();
        }

        CraftechWavefrontObject.textureCoordinateMatcher = CraftechWavefrontObject.textureCoordinatePattern.matcher(line);
        return CraftechWavefrontObject.textureCoordinateMatcher.matches();
    }

    /***
     * Verifies that the given line from the model file is a valid face that is
     * described by vertices, texture coordinates, and vertex normals
     *
     * @param line
     *            the line being validated
     * @return true if the line is a valid face that matches the format "f
     *         v1/vt1/vn1 ..." (with a minimum of 3 points in the face, and a
     *         maximum of 4), false otherwise
     */
    private static boolean isValidFace_V_VT_VN_Line(final String line)
    {
        if (CraftechWavefrontObject.face_V_VT_VN_Matcher != null)
        {
            CraftechWavefrontObject.face_V_VT_VN_Matcher.reset();
        }

        CraftechWavefrontObject.face_V_VT_VN_Matcher = CraftechWavefrontObject.face_V_VT_VN_Pattern.matcher(line);
        return CraftechWavefrontObject.face_V_VT_VN_Matcher.matches();
    }

    /***
     * Verifies that the given line from the model file is a valid face that is
     * described by vertices and texture coordinates
     *
     * @param line
     *            the line being validated
     * @return true if the line is a valid face that matches the format "f
     *         v1/vt1 ..." (with a minimum of 3 points in the face, and a
     *         maximum of 4), false otherwise
     */
    private static boolean isValidFace_V_VT_Line(final String line)
    {
        if (CraftechWavefrontObject.face_V_VT_Matcher != null)
        {
            CraftechWavefrontObject.face_V_VT_Matcher.reset();
        }

        CraftechWavefrontObject.face_V_VT_Matcher = CraftechWavefrontObject.face_V_VT_Pattern.matcher(line);
        return CraftechWavefrontObject.face_V_VT_Matcher.matches();
    }

    /***
     * Verifies that the given line from the model file is a valid face that is
     * described by vertices and vertex normals
     *
     * @param line
     *            the line being validated
     * @return true if the line is a valid face that matches the format "f
     *         v1//vn1 ..." (with a minimum of 3 points in the face, and a
     *         maximum of 4), false otherwise
     */
    private static boolean isValidFace_V_VN_Line(final String line)
    {
        if (CraftechWavefrontObject.face_V_VN_Matcher != null)
        {
            CraftechWavefrontObject.face_V_VN_Matcher.reset();
        }

        CraftechWavefrontObject.face_V_VN_Matcher = CraftechWavefrontObject.face_V_VN_Pattern.matcher(line);
        return CraftechWavefrontObject.face_V_VN_Matcher.matches();
    }

    /***
     * Verifies that the given line from the model file is a valid face that is
     * described by only vertices
     *
     * @param line
     *            the line being validated
     * @return true if the line is a valid face that matches the format "f v1
     *         ..." (with a minimum of 3 points in the face, and a maximum of
     *         4), false otherwise
     */
    private static boolean isValidFace_V_Line(final String line)
    {
        if (CraftechWavefrontObject.face_V_Matcher != null)
        {
            CraftechWavefrontObject.face_V_Matcher.reset();
        }

        CraftechWavefrontObject.face_V_Matcher = CraftechWavefrontObject.face_V_Pattern.matcher(line);
        return CraftechWavefrontObject.face_V_Matcher.matches();
    }

    /***
     * Verifies that the given line from the model file is a valid face of any
     * of the possible face formats
     *
     * @param line
     *            the line being validated
     * @return true if the line is a valid face that matches any of the valid
     *         face formats, false otherwise
     */
    private static boolean isValidFaceLine(final String line)
    {
        return CraftechWavefrontObject.isValidFace_V_VT_VN_Line(line) || CraftechWavefrontObject.isValidFace_V_VT_Line(line)
                || CraftechWavefrontObject.isValidFace_V_VN_Line(line) || CraftechWavefrontObject.isValidFace_V_Line(line);
    }

    /***
     * Verifies that the given line from the model file is a valid group (or
     * object)
     *
     * @param line
     *            the line being validated
     * @return true if the line is a valid group (or object), false otherwise
     */
    private static boolean isValidGroupObjectLine(final String line)
    {
        if (CraftechWavefrontObject.groupObjectMatcher != null)
        {
            CraftechWavefrontObject.groupObjectMatcher.reset();
        }

        CraftechWavefrontObject.groupObjectMatcher = CraftechWavefrontObject.groupObjectPattern.matcher(line);
        return CraftechWavefrontObject.groupObjectMatcher.matches();
    }

    @Override
    public String getType()
    {
        return "obj";
    }
}