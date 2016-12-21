package fr.craftechmc.handweapons.objects;

import lombok.Data;

public @Data class HandWeaponsDescriptor
{
    private String name, texture, model;
    private float damage;
    private int durability;
    private float scale, inventoryScale, lootScale;
    private float lootRotationX, lootRotationY, lootRotationZ;
    private float rotationX, rotationY, rotationZ;
    private float thirdPersonRotationX, thirdPersonRotationY, thirdPersonRotationZ;
    private float inventoryRotationX, inventoryRotationY, inventoryRotationZ;
    private float inventoryPosX, inventoryPosY;

    public HandWeaponsDescriptor()
    {
        super();
    }
}