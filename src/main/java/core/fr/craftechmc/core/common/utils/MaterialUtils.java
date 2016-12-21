package fr.craftechmc.core.common.utils;

import net.minecraft.block.material.Material;

public class MaterialUtils
{
    public static final Material getMaterialFromName(final String material)
    {
        switch (material)
        {
            case "grass":
                return Material.grass;
            case "ground":
                return Material.ground;
            case "wood":
                return Material.wood;
            case "rock":
                return Material.rock;
            case "iron":
                return Material.iron;
            case "anvil":
                return Material.anvil;
            case "water":
                return Material.water;
            case "lava":
                return Material.lava;
            case "leaves":
                return Material.leaves;
            case "plants":
                return Material.plants;
            case "vines":
                return Material.vine;
            case "sponge":
                return Material.sponge;
            case "cloth":
                return Material.cloth;
            case "fire":
                return Material.fire;
            case "sand":
                return Material.sand;
            case "circuits":
                return Material.circuits;
            case "carpet":
                return Material.carpet;
            case "glass":
                return Material.glass;
            case "redstoneLight":
                return Material.redstoneLight;
            case "tnt":
                return Material.tnt;
            case "coral":
                return Material.coral;
            case "ice":
                return Material.ice;
            case "packedIce":
                return Material.packedIce;
            case "snow":
                return Material.snow;
            case "craftedSnow":
                return Material.craftedSnow;
            case "cactus":
                return Material.cactus;
            case "clay":
                return Material.clay;
            case "gourd":
                return Material.gourd;
            case "dragonEgg":
                return Material.dragonEgg;
            case "portal":
                return Material.portal;
            case "cake":
                return Material.cake;
            case "web":
                return Material.web;
            case "piston":
                return Material.piston;
            default:
                return Material.air;
        }
    }
}