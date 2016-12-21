package fr.craftechmc.handweapons.objects;

import lombok.Data;

import java.util.ArrayList;

public @Data class HandWeaponsGroupDescriptor
{
    private String groupName;
    private ArrayList<HandWeaponsDescriptor> descriptors = new ArrayList<HandWeaponsDescriptor>();
    
    public HandWeaponsGroupDescriptor()
    {
        super();
    }
}