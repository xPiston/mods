package fr.craftechmc.zombie.client;

import cpw.mods.fml.client.registry.RenderingRegistry;
import fr.craftechmc.zombie.client.render.RenderZombieCraftech;
import fr.craftechmc.zombie.common.CZCommonProxy;
import fr.craftechmc.zombie.common.entity.EntityZombieCraftech;

/**
 * Created by Phenix246 on 20/06/2016.
 */
public class CZClientProxy extends CZCommonProxy
{

    @Override
    public void registerEntity()
    {
        RenderingRegistry.registerEntityRenderingHandler(EntityZombieCraftech.class, new RenderZombieCraftech());
    }

}
