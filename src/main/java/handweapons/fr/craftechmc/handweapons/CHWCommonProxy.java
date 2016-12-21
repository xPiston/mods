package fr.craftechmc.handweapons;

import fr.craftechmc.handweapons.items.ItemsManager;
import fr.ourten.baldr.BaldrStorage;
import fr.ourten.baldr.resource.objects.BaldrContentDescriptor;

public class CHWCommonProxy
{
    public void makeSidedOps()
    {

    }

    public void loadSide()
    {
        BaldrStorage provider = BaldrStorage.getInstance();

        BaldrContentDescriptor hand = provider.getContentDescriptor("craftechweaponry-hand");

        hand.getContentFiles().keySet().forEach(
                cFile -> ItemsManager.getInstance().handweaponsgroups.add(provider.getContentFile(hand, cFile)));
    }
}