package com.unascribed.lambdanetwork;

import java.util.Iterator;
import java.util.Map;

import com.google.common.collect.Maps;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.craftechmc.core.common.objects.BlockPos;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;

public class PendingPacket
{
    private final LambdaNetwork       owner;

    private LambdaChannel             channel;

    private PacketSpec                packet;

    private String                    packetId;

    private final Map<String, Object> data = Maps.newHashMap();

    public PendingPacket(final LambdaNetwork owner)
    {
        this.owner = owner;
        if (owner.channelCount() == 1)
            this.channel = owner.getSoleChannel();
    }

    public PacketSpec getPacket()
    {
        return this.packet;
    }

    public Map<String, ?> getData()
    {
        return this.data;
    }

    public PendingPacket packet(final String packet)
    {
        if (this.channel != null)
            this.packet = this.channel.getPacketSpec(packet);
        this.packetId = packet;
        return this;
    }

    public PendingPacket onChannel(final String channel)
    {
        this.channel = this.owner.getChannel(channel);
        if (this.packetId != null)
            this.packet = this.channel.getPacketSpec(this.packetId);
        return this;
    }

    public PendingPacket with(final String key, final int value)
    {
        this.checkHasPacket();
        if (!this.packet.getType(key).isValidForInteger())
            this.invalidType(key, "int");
        this.data.put(key, value);
        return this;
    }

    public PendingPacket with(final String key, final long value)
    {
        this.checkHasPacket();
        if (!this.packet.getType(key).isValidForInteger())
            this.invalidType(key, "long");
        this.data.put(key, value);
        return this;
    }

    public PendingPacket with(final String key, final boolean value)
    {
        this.checkHasPacket();
        if (!this.packet.getType(key).isValidForBoolean())
            this.invalidType(key, "boolean");
        this.data.put(key, value);
        return this;
    }

    public PendingPacket with(final String key, final float value)
    {
        this.checkHasPacket();
        if (!this.packet.getType(key).isValidForFloating())
            this.invalidType(key, "float");
        this.data.put(key, value);
        return this;
    }

    public PendingPacket with(final String key, final double value)
    {
        this.checkHasPacket();
        if (!this.packet.getType(key).isValidForFloating())
            this.invalidType(key, "double");
        this.data.put(key, value);
        return this;
    }

    public PendingPacket with(final String key, final String value)
    {
        this.checkHasPacket();
        if (!this.packet.getType(key).isValidForString())
            this.invalidType(key, "String");
        if (value == null)
            throw new IllegalArgumentException("Cannot use null for String value");
        this.data.put(key, value);
        return this;
    }

    public PendingPacket with(final String key, final NBTTagCompound value)
    {
        this.checkHasPacket();
        if (!this.packet.getType(key).isValidForNBT())
            this.invalidType(key, "NBTTagCompound");
        if (value == null)
            throw new IllegalArgumentException("Cannot use null for NBTTagCompound value");
        this.data.put(key, value);
        return this;
    }

    public PendingPacket with(final String key, final byte[] value)
    {
        this.checkHasPacket();
        if (!this.packet.getType(key).isValidForData())
            this.invalidType(key, "byte[]");
        if (value == null)
            throw new IllegalArgumentException("Cannot use null for byte[] value");
        this.data.put(key, value);
        return this;
    }

    private void invalidType(final String key, final String type)
    {
        throw new IllegalArgumentException("Type " + type + " is not valid for '" + key + "' (data type "
                + this.packet.getType(key) + ") in packet '" + this.packet.getIdentifier() + "'");
    }

    private void checkHasPacket()
    {
        if (this.packet == null)
            throw new IllegalArgumentException("Must specify packet before data");
    }

    /**
     * For use on the server-side. Sends this packet to the given player.
     */
    public void to(final EntityPlayer player)
    {
        if (this.packet.getSide().isServer())
            this.wrongSide();
        if (player instanceof EntityPlayerMP)
            ((EntityPlayerMP) player).playerNetServerHandler.sendPacket(this.toClientboundVanillaPacket());
    }

    /**
     * For use on the server-side. Sends this packet to every player that is
     * within the given radius of the given position. <i>It is almost always
     * better to use {@link #toAllWatching(Entity)}, this is only useful for
     * certain special cases.</i>
     */
    public void toAllAround(final World world, final Entity entity, final double radius)
    {
        this.toAllAround(world, entity.posX, entity.posY, entity.posZ, radius);
    }

    /**
     * For use on the server-side. Sends this packet to every player that is
     * within the given radius of the given position. <i>It is almost always
     * better to use {@link #toAllWatching(World, BlockPos)}, this is only
     * useful for certain special cases.</i>
     */
    public void toAllAround(final World world, final Vec3 pos, final double radius)
    {
        this.toAllAround(world, pos.xCoord, pos.yCoord, pos.zCoord, radius);
    }

    /**
     * For use on the server-side. Sends this packet to every player that is
     * within the given radius of the given position.
     */
    public void toAllAround(final World world, final double x, final double y, final double z, final double radius)
    {
        if (this.packet.getSide().isServer())
            this.wrongSide();
        final double sq = radius * radius;
        final Packet packet = this.toClientboundVanillaPacket();
        PendingPacket.playerMPsOnly(world.playerEntities).forEach(ep ->
        {
            if (ep.getDistanceSq(x, y, z) <= sq)
                ep.playerNetServerHandler.sendPacket(packet);
        });
    }

    /**
     * For use on the server-side. Sends this packet to every player that can
     * see the given block.
     */
    public void toAllWatching(final World world, final int x, final int z)
    {
        if (this.packet.getSide().isServer())
            this.wrongSide();
        if (world instanceof WorldServer)
        {
            final WorldServer srv = (WorldServer) world;
            final Chunk c = srv.getChunkFromBlockCoords(x, z);
            if (srv.getPlayerManager().func_152621_a(c.xPosition, c.zPosition))
            {
                final Packet packet = this.toClientboundVanillaPacket();
                PendingPacket.playerMPsOnly(world.playerEntities).forEach(ep ->
                {
                    if (srv.getPlayerManager().isPlayerWatchingChunk(ep, c.xPosition, c.zPosition))
                        ep.playerNetServerHandler.sendPacket(packet);
                });
            }
        }
    }

    /**
     * For use on the server-side. Sends this packet to every player that can
     * see the given tile entity.
     */
    public void toAllWatching(final TileEntity te)
    {
        this.toAllWatching(te.getWorld(), te.xCoord, te.zCoord);
    }

    /**
     * For use on the server-side. Sends this packet to every player that can
     * see the given entity.
     */
    public void toAllWatching(final Entity e)
    {
        if (this.packet.getSide().isServer())
            this.wrongSide();
        if (e.worldObj instanceof WorldServer)
        {
            final WorldServer srv = (WorldServer) e.worldObj;
            srv.getEntityTracker().sendToAllTrackingEntity(e, this.toClientboundVanillaPacket());
        }
    }

    /**
     * For use on the server-side. Sends this packet to every player in the
     * given world.
     */
    public void toAllIn(final World world)
    {
        if (this.packet.getSide().isServer())
            this.wrongSide();
        final Packet packet = this.toClientboundVanillaPacket();
        PendingPacket.playerMPsOnly(world.playerEntities).forEach(ep ->
        {
            ep.playerNetServerHandler.sendPacket(packet);
        });
    }

    /**
     * For use on the server-side. Sends this packet to every player currently
     * connected to the server. Use sparingly, you almost never need to send a
     * packet to everyone.
     */
    public void toEveryone()
    {
        if (this.packet.getSide().isServer())
            this.wrongSide();
        final Packet packet = this.toClientboundVanillaPacket();

        PendingPacket.playerMPsOnly(MinecraftServer.getServer().getConfigurationManager().playerEntityList)
                .forEach(ep ->
                {
                    ep.playerNetServerHandler.sendPacket(packet);
                });
    }

    /**
     * For use on the <i>client</i>-side. This is the only valid method for use
     * on the client side.
     */
    @SideOnly(Side.CLIENT)
    public void toServer()
    {
        if (this.packet.getSide().isClient())
            this.wrongSide();
        Minecraft.getMinecraft().getNetHandler().addToSendQueue(this.toServerboundVanillaPacket());
    }

    private void wrongSide()
    {
        throw new IllegalStateException(
                "Packet '" + this.packet.getIdentifier() + "' cannot be sent from side " + this.packet.getSide());
    }

    /**
     * Mainly intended for internal use, but can be useful for more complex use
     * cases.
     */
    public Packet toServerboundVanillaPacket()
    {
        return this.channel.getPacketFrom(this).toC17Packet();
    }

    /**
     * Mainly intended for internal use, but can be useful for more complex use
     * cases.
     */
    public Packet toClientboundVanillaPacket()
    {
        return this.channel.getPacketFrom(this).toS3FPacket();
    }

    static Iterable<EntityPlayerMP> playerMPsOnly(final Iterable i)
    {
        return () -> new Iterator<EntityPlayerMP>()
        {
            private EntityPlayerMP next;
            private final Iterator underlying = i.iterator();

            private void advance()
            {
                while (this.next == null && this.underlying.hasNext())
                {
                    final Object n = this.underlying.next();
                    if (n instanceof EntityPlayerMP)
                        this.next = (EntityPlayerMP) n;
                }
            }

            @Override
            public boolean hasNext()
            {
                this.advance();
                return this.next != null;
            }

            @Override
            public EntityPlayerMP next()
            {
                this.advance();
                final EntityPlayerMP ret = this.next;
                this.next = null;
                return ret;
            }
        };
    }
}
