package fr.craftechmc.loots.common.objects;

import java.util.ArrayList;

import com.google.common.collect.Lists;

public class ContainerLootList
{
    private long                          respawnTime;
    private double                        modulation;
    private int                           lootLevel;
    private ArrayList<ContainerLootEntry> loots = Lists.newArrayList();

    /**
     * @return The minimum time for this LootList to re-appear.
     */
    public long getRespawnTime()
    {
        return this.respawnTime;
    }

    /**
     * @return The modulation to be applied if someone try to generate this
     *         LootList when it's respawnTime computed is in the futur.
     *         RespawnTime computation is up to the implementation. To be
     *         treated like a percentage expressed as a fraction, like 1.25 =
     *         125% of modulation.
     *
     *         The usual implementation state that it's the time left to reach
     *         the expected computed respawnTime that has to be modulated.
     */
    public double getModulation()
    {
        return this.modulation;
    }

    public ArrayList<ContainerLootEntry> getLoots()
    {
        return this.loots;
    }

    public void setLoots(final ArrayList<ContainerLootEntry> loots)
    {
        this.loots = loots;
    }

    public void setRespawnTime(final long respawnTime)
    {
        this.respawnTime = respawnTime;
    }

    public void setModulation(final double modulation)
    {
        this.modulation = modulation;
    }

    public int getLootLevel()
    {
        return this.lootLevel;
    }

    public void setLootLevel(final int lootLevel)
    {
        this.lootLevel = lootLevel;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.lootLevel;
        result = prime * result + (this.loots == null ? 0 : this.loots.hashCode());
        long temp;
        temp = Double.doubleToLongBits(this.modulation);
        result = prime * result + (int) (temp ^ temp >>> 32);
        result = prime * result + (int) (this.respawnTime ^ this.respawnTime >>> 32);
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
        final ContainerLootList other = (ContainerLootList) obj;
        if (this.lootLevel != other.lootLevel)
            return false;
        if (this.loots == null)
        {
            if (other.loots != null)
                return false;
        }
        else if (!this.loots.equals(other.loots))
            return false;
        if (Double.doubleToLongBits(this.modulation) != Double.doubleToLongBits(other.modulation))
            return false;
        if (this.respawnTime != other.respawnTime)
            return false;
        return true;
    }

    @Override
    public String toString()
    {
        return "ContainerLootList [respawnTime=" + this.respawnTime + ", modulation=" + this.modulation + ", lootLevel="
                + this.lootLevel + ", loots=" + this.loots + "]";
    }
}