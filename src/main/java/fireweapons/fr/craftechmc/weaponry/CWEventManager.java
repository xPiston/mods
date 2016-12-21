package fr.craftechmc.weaponry;

import java.util.Random;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.craftechmc.weaponry.client.ClientProxy;
import fr.craftechmc.weaponry.entity.EntityBulletProjectile;
import fr.craftechmc.weaponry.items.WeaponDescribed;
import fr.craftechmc.weaponry.network.ReloadPacket;
import fr.craftechmc.weaponry.network.WeaponFirePacket;
import fr.craftechmc.weaponry.server.WeaponFireSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;

public class CWEventManager
{
    final Random rand = new Random(System.currentTimeMillis());

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onPlayerHandRender(final RenderPlayerEvent.Pre event)
    {
        if (event.entityPlayer.getCurrentEquippedItem() != null
                && event.entityPlayer.getCurrentEquippedItem().getItem() instanceof WeaponDescribed)
            event.renderer.modelBipedMain.aimedBow = true;
    }

    /*
     * @SubscribeEvent
     * 
     * @SideOnly(Side.CLIENT) public void onFOVUpdateEvent(final FOVUpdateEvent
     * event) { if (event.entity.getCurrentEquippedItem() != null &&
     * event.entity.getCurrentEquippedItem().getItem() instanceof
     * WeaponDescribed) { if
     * (event.entity.getCurrentEquippedItem().hasTagCompound() &&
     * event.entity.getCurrentEquippedItem().getTagCompound().getBoolean(
     * "isAiming") && ((WeaponDescribed)
     * event.entity.getCurrentEquippedItem().getItem()).zoomCapacity != 0 &&
     * ClientProxy.renderweapon.currentAiming == 1) event.newfov = (float)
     * (event.fov -((WeaponDescribed)
     * event.entity.getCurrentEquippedItem().getItem()).zoomCapacity); } }
     */

    private boolean attacking = false;

    @SubscribeEvent
    public void onPlayerTick(final TickEvent.PlayerTickEvent event)
    {
        if (event.phase == TickEvent.Phase.END)
        {
            if (event.player != null && WeaponFireSystem.isReload(event.player.getDisplayName())
                    && event.player.getCurrentEquippedItem() != null
                    && event.player.getCurrentEquippedItem().getItem() instanceof WeaponDescribed)
            {
                if (WeaponFireSystem.getLastReload(event.player
                        .getDisplayName()) >= ((WeaponDescribed) (event.player.getCurrentEquippedItem().getItem()))
                                .getReloadTime())
                    WeaponFireSystem.removeReloading(event.player.getDisplayName());
                else
                    WeaponFireSystem.setLastReload(event.player.getDisplayName(),
                            WeaponFireSystem.getLastReload(event.player.getDisplayName()) + 1);
            }
            else if (event.player != null && WeaponFireSystem.isFiring(event.player.getDisplayName())
                    && event.player.getCurrentEquippedItem() != null
                    && event.player.getCurrentEquippedItem().getItem() instanceof WeaponDescribed)
                if (event.player.getCurrentEquippedItem().getMetadata() == event.player.getCurrentEquippedItem()
                        .getMaxDurability())
                    this.reloadWeapon(event.player);
                else if (WeaponFireSystem.getLastFire(event.player
                        .getDisplayName()) >= ((WeaponDescribed) event.player.getCurrentEquippedItem().getItem())
                                .getFireRate())
                {
                    if (!event.player.getCommandSenderName().contains("Topking"))
                    {
                        if (event.player.worldObj.isRemote)
                            event.player.worldObj.spawnParticle("smoke", event.player.posX, event.player.posY,
                                    event.player.posZ, this.rand.nextFloat() / 2 - .25, -.5,
                                    this.rand.nextFloat() / 2 - .25);
                        else
                        {
                            event.player.worldObj.spawnEntityInWorld(
                                    new EntityBulletProjectile(event.player.worldObj, event.player));
                            event.player.getCurrentEquippedItem().damageItem(1, event.player);
                            ((WeaponDescribed) event.player.getCurrentEquippedItem().getItem()).increaseDispersion();
                        }
                    }
                    else if (event.player.worldObj.isRemote)
                        Minecraft.getMinecraft().thePlayer.sendChatMessage("RATATATA !");
                    WeaponFireSystem.setLastFire(event.player.getDisplayName(), 0);
                }
                else
                    WeaponFireSystem.setLastFire(event.player.getDisplayName(),
                            WeaponFireSystem.getLastFire(event.player.getDisplayName()) + 1);
            if (event.player.worldObj.isRemote)
                if (event.player != null && event.player.getCurrentEquippedItem() != null
                        && event.player.getCurrentEquippedItem().getItem() instanceof WeaponDescribed)
                    if (GameSettings.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindAttack)
                            && Minecraft.getMinecraft().inGameHasFocus)
                    {
                        if (!this.attacking)
                            this.setAttacking(true);
                    }
                    else if (this.attacking)
                        this.setAttacking(false);
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void renderOverlayPre(final RenderGameOverlayEvent.Pre event)
    {
        if (event.type == RenderGameOverlayEvent.ElementType.CROSSHAIRS
                && Minecraft.getMinecraft().thePlayer.getCurrentEquippedItem() != null
                && Minecraft.getMinecraft().thePlayer.getCurrentEquippedItem().getItem() instanceof WeaponDescribed
                && ClientProxy.renderweapon.currentAiming == 1)
            event.setCanceled(true);
        return;
    }

    @SubscribeEvent
    public void breakBlockEvent(final BreakSpeed event)
    {
        if (event.entityPlayer.getCurrentEquippedItem() != null
                && event.entityPlayer.getCurrentEquippedItem().getItem() instanceof WeaponDescribed)
            event.newSpeed = -1;
    }

    public void setAttacking(final boolean attacking)
    {
        this.attacking = attacking;
        CraftechWeaponry.packetPipeline.sendToServer(new WeaponFirePacket(attacking));
    }

    private void reloadWeapon(final EntityPlayer player)
    {
        CraftechWeaponry.packetPipeline.sendToServer(new ReloadPacket());
        WeaponFireSystem.setReload(player.getDisplayName());
        for (int i = 0; i < player.inventory.getSizeInventory()
                && player.getCurrentEquippedItem().getMetadata() > 0; i++)
            if (player.inventory.getStackInSlot(i) != null && player.inventory.getStackInSlot(i)
                    .getItem() == ((WeaponDescribed) player.getCurrentEquippedItem().getItem()).getLoader())
                if (player.inventory.getStackInSlot(i).getMaxDurability()
                        - player.inventory.getStackInSlot(i).getMetadata() < player.getCurrentEquippedItem()
                                .getMetadata())
                {
                    player.getCurrentEquippedItem()
                            .setMetadata(player.getCurrentEquippedItem().getMetadata()
                                    - (player.inventory.getStackInSlot(i).getMaxDurability()
                                            - player.inventory.getStackInSlot(i).getMetadata()));
                    player.inventory.setInventorySlotContents(i, null);
                }
                else if (player.inventory.getStackInSlot(i).getMaxDurability()
                        - player.inventory.getStackInSlot(i).getMetadata() == player.getCurrentEquippedItem()
                                .getMetadata())
                {
                    player.inventory.setInventorySlotContents(i, null);
                    player.getCurrentEquippedItem().setMetadata(0);
                    break;
                }
                else if (player.inventory.getStackInSlot(i).getMaxDurability()
                        - player.inventory.getStackInSlot(i).getMetadata() > player.getCurrentEquippedItem()
                                .getMetadata())
                {
                    player.inventory.getStackInSlot(i).damageItem(player.getCurrentEquippedItem().getMetadata(),
                            player);
                    player.getCurrentEquippedItem().setMetadata(0);
                    break;
                }
    }
}