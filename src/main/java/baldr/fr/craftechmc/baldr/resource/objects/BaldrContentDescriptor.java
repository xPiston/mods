package fr.craftechmc.baldr.resource.objects;

import java.util.HashMap;

public class BaldrContentDescriptor
{
    private String                  requiredMod;
    private String                  folderName;
    private HashMap<String, String> contentFiles  = new HashMap<>();
    private HashMap<String, String> resourceFiles = new HashMap<>();

    public BaldrContentDescriptor()
    {
        super();
    }

    public BaldrContentDescriptor(final String requiredMod, final String folderName)
    {
        this();
        this.requiredMod = requiredMod;
        this.folderName = folderName;
    }

    public String getRequiredMod()
    {
        return this.requiredMod;
    }

    public void setRequiredMod(final String requiredMod)
    {
        this.requiredMod = requiredMod;
    }

    public String getFolderName()
    {
        return this.folderName;
    }

    public void setFolderName(final String folderName)
    {
        this.folderName = folderName;
    }

    public HashMap<String, String> getContentFiles()
    {
        return this.contentFiles;
    }

    public void setContentFiles(final HashMap<String, String> contentFiles)
    {
        this.contentFiles = contentFiles;
    }

    public HashMap<String, String> getResourceFiles()
    {
        return this.resourceFiles;
    }

    public void setResourceFiles(final HashMap<String, String> resourceFiles)
    {
        this.resourceFiles = resourceFiles;
    }

    @Override
    public String toString()
    {
        return "BaldrContentDescriptor [requiredMod=" + this.requiredMod + ", folderName=" + this.folderName
                + ", contentFiles=" + this.contentFiles + ", resourceFiles=" + this.resourceFiles + "]";
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + (this.contentFiles == null ? 0 : this.contentFiles.hashCode());
        result = prime * result + (this.folderName == null ? 0 : this.folderName.hashCode());
        result = prime * result + (this.requiredMod == null ? 0 : this.requiredMod.hashCode());
        result = prime * result + (this.resourceFiles == null ? 0 : this.resourceFiles.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (this.getClass() != obj.getClass())
            return false;
        final BaldrContentDescriptor other = (BaldrContentDescriptor) obj;
        if (this.contentFiles == null)
        {
            if (other.contentFiles != null)
                return false;
        }
        else if (!this.contentFiles.equals(other.contentFiles))
            return false;
        if (this.folderName == null)
        {
            if (other.folderName != null)
                return false;
        }
        else if (!this.folderName.equals(other.folderName))
            return false;
        if (this.requiredMod == null)
        {
            if (other.requiredMod != null)
                return false;
        }
        else if (!this.requiredMod.equals(other.requiredMod))
            return false;
        if (this.resourceFiles == null)
        {
            if (other.resourceFiles != null)
                return false;
        }
        else if (!this.resourceFiles.equals(other.resourceFiles))
            return false;
        return true;
    }
}