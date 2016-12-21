package fr.craftechmc.contentmod.common.multiblocks;

public interface IMultiBlockTileGag extends IMultiBlockTile
{
    public abstract void destroyCore();

    public abstract boolean isCorePresent();

    public abstract void setxCore(int x);

    public abstract void setyCore(int i);

    public abstract void setzCore(int z);

    public abstract TileModelMultiBlockCore getCore();

    public abstract void setUnlocalizedCoreName(String str);
}