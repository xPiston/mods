package fr.craftechmc.needs.common;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import fr.craftechmc.baldr.BaldrStorage;
import fr.craftechmc.baldr.resource.objects.BaldrContentDescriptor;
import fr.craftechmc.needs.client.render.ISRSimpleInventoryModelRenderer;
import fr.craftechmc.needs.common.items.INeedsFoodItem;
import fr.craftechmc.needs.common.items.ItemDescriptor;
import fr.craftechmc.needs.common.items.ItemGroupDescriptor;
import fr.craftechmc.needs.common.items.NeedsFoodItemDescribed;
import fr.craftechmc.needs.common.properties.NeedsProperties;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerUseItemEvent;

/**
 * Created by arisu on 22/06/2016.
 */
public class CommonProxy
{

    public void init(final FMLPreInitializationEvent e)
    {
        MinecraftForge.EVENT_BUS.register(this);

        // Load baldr items then register them
        final BaldrStorage provider = BaldrStorage.getInstance();
        final BaldrContentDescriptor itemsDescriptor = provider.getContentDescriptor("craftechneeds-items");

        boolean isClient = FMLCommonHandler.instance().getEffectiveSide().isClient();

        // Fetch groups
        for (final String contentFileName : itemsDescriptor.getContentFiles().keySet())
        {
            final ItemGroupDescriptor itemGroupDescriptor = CraftechNeeds.GSON
                    .fromJson(provider.getContentFile(itemsDescriptor, contentFileName), ItemGroupDescriptor.class);

            // Fetch items
            for (final ItemDescriptor itemDescriptor : itemGroupDescriptor.getDescriptors())
            {
                final NeedsFoodItemDescribed item = new NeedsFoodItemDescribed(itemDescriptor.getName(),
                        itemDescriptor.getSaturation(), itemDescriptor.getLevel(), itemGroupDescriptor.getType());
                item.setTextureName("craftechneeds-items:" + itemDescriptor.getTextureName())
                        .setCreativeTab(CreativeTabs.tabFood);

                if (isClient && !itemDescriptor.getModelName().isEmpty())
                    MinecraftForgeClient.registerItemRenderer(item,
                            new ISRSimpleInventoryModelRenderer(item, itemDescriptor.getTextureName(),
                                    itemDescriptor.getModelName()));

                // Finally register the item
                GameRegistry.registerItem(item, itemDescriptor.getName(), CraftechNeeds.MODID);
            }
        }
    }

    /**
     * Ticks the thirst so it slowly dries
     *
     * @param event
     */
    @SubscribeEvent
    public void onPlayerTick(final PlayerTickEvent event)
    {
        if (event.phase != TickEvent.Phase.END)
            return;

        final EntityPlayer player = event.player;

        if (player.capabilities.isCreativeMode)
            return;

        NeedsProperties.get(player).tick();
    }

    /**
     * Makes the custom hunger, thirst and food system work
     *
     * @param event
     */
    @SubscribeEvent
    public void onPlayerEat(final PlayerUseItemEvent.Finish event)
    {
        if (event.item.getItem() instanceof INeedsFoodItem)
        {
            final NeedsFoodItemDescribed item = (NeedsFoodItemDescribed) event.item.getItem();
            switch (item.getType())
            {
                case FOOD:
                    NeedsProperties.get(event.entityPlayer).getHunger()
                            .replenish(item.getSaturation(), item.getLevel());
                    break;
                case DRINK:
                    NeedsProperties.get(event.entityPlayer).getThirst()
                            .replenish(item.getSaturation(), item.getLevel());
                    break;
                case DRUG:
                    event.entityPlayer.heal((float) item.getLevel());
                    break;
                case SPLINT:
                    NeedsProperties.get(event.entityPlayer).getLegFracture().setRemainingTicks(0);
                    break;
            }
        }
    }

    @SubscribeEvent
    public void onPlayerJump(LivingEvent.LivingJumpEvent event)
    {
        if (event.entity instanceof EntityPlayer)
        {
            NeedsProperties props = NeedsProperties.get((EntityPlayer) event.entity);
            props.getHunger().decay(0.0625);
            props.getThirst().decay(0.11764705882);
        }
    }

    @SubscribeEvent
    public void onPlayerAttack(AttackEntityEvent event)
    {
        NeedsProperties props = NeedsProperties.get(event.entityPlayer);
        props.getHunger().decay(0.125);
        props.getThirst().decay(0.08333333333333);
    }

    @SubscribeEvent
    public void onPlayerDamaged(LivingHurtEvent event)
    {
        if (event.entityLiving instanceof EntityPlayer)
        {
            NeedsProperties props = NeedsProperties.get((EntityPlayer) event.entityLiving);
            if (event.source != DamageSource.starve && event.source != NeedsProperties.THIRST_DAMAGE_SOURCE)
            {
                props.getHunger().decay(event.ammount / 4);
                props.sync();
            }

            if (event.source == DamageSource.fall && event.ammount >= 2)
            {
                props.getLegFracture().setRemainingTicks(-1);
                props.getBleeding().setRemainingTicks(6000);
                props.sync();
            }
        }
    }
}
