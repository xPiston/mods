package fr.craftechmc.admin.common.fogpath;

import java.util.LinkedList;

import org.yggard.brokkgui.wrapper.BrokkGuiManager;

import cpw.mods.fml.common.FMLCommonHandler;
import fr.craftechmc.admin.common.AdminPacketConstants;
import fr.craftechmc.admin.common.CraftechAdmin;
import fr.craftechmc.core.math.Vector3d;
import fr.craftechmc.environment.common.fog.FogPath;
import fr.craftechmc.environment.common.fog.FogPathWorldSavedData;
import fr.craftechmc.environment.common.fog.FogZone;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

/**
 * This fogpath manager is common; it's the fogpath editor Created by arisu on
 * 24/07/2016.
 */
public class FogPathManager
{
    private enum EditorMode
    {
        INIT, NAME_ZONE, NAME_FOG_PATH, SELECT_ZONE, SELECT_FOG_PATH, EDIT_FOG_PATH
    }

    private static volatile FogPathManager instance;

    public static FogPathManager getInstance()
    {
        if (FogPathManager.instance == null)
            synchronized (FogPathManager.class)
            {
                if (FogPathManager.instance == null)
                    FogPathManager.instance = new FogPathManager();
            }
        return FogPathManager.instance;
    }

    private World      world;
    private EditorMode editorMode = EditorMode.INIT;
    private FogZone    currentZone;
    private FogPath    currentFogPath;
    private Vector3d   lastCoords;

    private FogPathManager()
    {

    }

    public void addCheckpoint(final int x, final int y, final int z, final EntityPlayer player)
    {
        this.world = player.worldObj;
        // Init -> Select zone
        if (!this.editorMode.equals(EditorMode.EDIT_FOG_PATH))
        {
            this.lastCoords = new Vector3d(x, y, z);
            this.selectZone();
        }

        // Edit -> Add checkpoint
        else
        {
            this.currentFogPath.addCheckpoint(x, y, z);
            this.sync(player);
        }
    }

    public void removeCheckpoint(final int x, final int y, final int z, final EntityPlayer player)
    {
        this.world = player.worldObj;
        if (this.editorMode.equals(EditorMode.EDIT_FOG_PATH))
        {
            final LinkedList<Vector3d> checkpoints = this.currentFogPath.getCheckpoints();
            final Vector3d coords = checkpoints.get(checkpoints.size() - 1);
            if ((int) coords.getX() == x && (int) coords.getY() == y && (int) coords.getZ() == z)
            {
                checkpoints.remove(checkpoints.size() - 1);
                this.sync(player);
            }
            else
            {
                checkpoints.remove(0);
                this.sync(player);
            }
        }
    }

    private void nameZone()
    {
        // Name the zone
        this.editorMode = EditorMode.NAME_ZONE;
        if (FMLCommonHandler.instance().getEffectiveSide().isClient())
            Minecraft.getMinecraft().displayGuiScreen(BrokkGuiManager.getBrokkGuiScreen(new InputSetterGui("ZONE")));
    }

    private void nameFog()
    {
        // Name the zone
        this.editorMode = EditorMode.NAME_FOG_PATH;
        if (FMLCommonHandler.instance().getEffectiveSide().isClient())
            Minecraft.getMinecraft().displayGuiScreen(BrokkGuiManager.getBrokkGuiScreen(new InputSetterGui("FOGPATH")));
    }

    public void input(final String text)
    {
        // Zone selected or named -> Select or name the fog
        if (!this.editorMode.equals(EditorMode.NAME_FOG_PATH) && !this.editorMode.equals(EditorMode.SELECT_FOG_PATH))
        {
            this.currentZone = new FogZone();
            this.currentZone.setName(text);
            FogPathWorldSavedData.get(this.world).getZones().add(this.currentZone);

            if (FMLCommonHandler.instance().getEffectiveSide().isClient())
                CraftechAdmin.network.send().packet(AdminPacketConstants.NAME_INPUT).with("name", text).toServer();

            this.nameFog();
        }
        // Fog selected or named -> now edit
        else if (this.editorMode.equals(EditorMode.NAME_FOG_PATH) || this.editorMode.equals(EditorMode.SELECT_FOG_PATH))
        {
            this.currentFogPath = new FogPath();
            this.currentFogPath.setName(text);
            this.currentZone.addFogPath(this.currentFogPath);

            if (this.lastCoords == null)
                this.lastCoords = new Vector3d(0, 0, 0);
            this.currentFogPath.addCheckpoint(this.lastCoords.getX(), this.lastCoords.getY(), this.lastCoords.getZ());

            if (FMLCommonHandler.instance().getEffectiveSide().isClient())
                CraftechAdmin.network.send().packet(AdminPacketConstants.NAME_INPUT).with("name", text).toServer();

            this.editorMode = EditorMode.EDIT_FOG_PATH;
        }
    }

    // Select zone
    public void selectZone()
    {
        this.editorMode = EditorMode.SELECT_ZONE;
        if (FMLCommonHandler.instance().getEffectiveSide().isClient())
            Minecraft.getMinecraft().displayGuiScreen(BrokkGuiManager
                    .getBrokkGuiScreen(new ListSelectGui("Zone : ", FogPathWorldSavedData.get(this.world).getZones())));
    }

    public void select(final ANameGetter el)
    {
        // Zone selection done
        if (!this.editorMode.equals(EditorMode.SELECT_FOG_PATH))
        {
            if (el == null) // Zone creation requested
            {
                if (this.lastCoords != null)
                    this.nameZone();
                return;
            }

            this.currentZone = (FogZone) el;

            if (FMLCommonHandler.instance().getEffectiveSide().isClient())
                CraftechAdmin.network.send().packet(AdminPacketConstants.SELECT_INPUT).with("elName", el.getName())
                        .toServer();

            this.editorMode = EditorMode.SELECT_FOG_PATH;
            if (FMLCommonHandler.instance().getEffectiveSide().isClient())
                Minecraft.getMinecraft().displayGuiScreen(BrokkGuiManager
                        .getBrokkGuiScreen(new ListSelectGui("Path : ", this.currentZone.getFogPaths())));
        }
        // Fog path selection done
        else
        {
            if (el == null) // Fog path creation requested
            {
                if (this.lastCoords != null)
                    this.nameFog();
                return;
            }

            this.currentFogPath = (FogPath) el;

            if (FMLCommonHandler.instance().getEffectiveSide().isClient())
                CraftechAdmin.network.send().packet(AdminPacketConstants.SELECT_INPUT).with("elName", el.getName())
                        .toServer();

            this.editorMode = EditorMode.EDIT_FOG_PATH;
        }
    }

    public void sync(final EntityPlayer player)
    {
        this.world = player.worldObj;
        final NBTTagCompound compound = new NBTTagCompound();
        FogPathWorldSavedData.get(player.worldObj).writeToNBT(compound);
        if (FMLCommonHandler.instance().getEffectiveSide().isServer())
            CraftechAdmin.network.send().packet(AdminPacketConstants.FOG_PATH_SYNC).with("zones", compound).to(player);
        FogPathWorldSavedData.saveAll();
    }

    public ANameGetter getElementByName(final String name)
    {
        if (this.editorMode == EditorMode.SELECT_ZONE)
        {
            for (final FogZone zone : FogPathWorldSavedData.get(this.world).getZones())
                if (zone.getName().equalsIgnoreCase(name))
                    return zone;
        }
        else if (this.editorMode == EditorMode.SELECT_FOG_PATH)
        {
            for (final FogPath fogPath : this.currentZone.getFogPaths())
                if (fogPath.getName().equalsIgnoreCase(name))
                    return fogPath;
        }
        return null;
    }

    public void setWorld(final World world)
    {
        this.world = world;
    }

    public void setEditorMode(final EditorMode editorMode)
    {
        this.editorMode = editorMode;
    }

    public void setCurrentZone(final FogZone currentZone)
    {
        this.currentZone = currentZone;
    }

    public void setCurrentFogPath(final FogPath currentFogPath)
    {
        this.currentFogPath = currentFogPath;
    }

    public void setLastCoords(final Vector3d lastCoords)
    {
        this.lastCoords = lastCoords;
    }
}