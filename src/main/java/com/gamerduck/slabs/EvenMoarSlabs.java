package com.gamerduck.slabs;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

public class EvenMoarSlabs implements ModInitializer {
    public static final String modid = "evenmoarslabs";
    public static final ItemGroup SLAB_ITEM_GROUP = FabricItemGroup.builder(new Identifier(modid, "slab_group"))
            .icon(() -> new ItemStack(Items.CHERRY_SLAB))
            .build();

    public static final ItemGroup STAIRS_ITEM_GROUP = FabricItemGroup.builder(new Identifier(modid, "stairs_group"))
            .icon(() -> new ItemStack(Items.OAK_STAIRS))
            .build();
    public static final ItemGroup WALLS_ITEM_GROUP = FabricItemGroup.builder(new Identifier(modid, "walls_group"))
            .icon(() -> new ItemStack(Items.COBBLESTONE_WALL))
            .build();
    /**
     * Runs the mod initializer.
     */
    @Override
    public void onInitialize() {
        new Slabs();
        new Stairs();
        new Walls();
    }
}
