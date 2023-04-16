package com.gamerduck.slabs;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.Iterator;

import static com.gamerduck.slabs.EvenMoarSlabs.*;
import static net.minecraft.block.AbstractBlock.Settings.copy;

public class Slabs {
    public static final ObjectArrayList<Slab> allSlabs = new ObjectArrayList<Slab>();

    public Slabs() {
        Iterator it = Registries.BLOCK.iterator();

        while (it.hasNext()) {
            Block block = (Block) it.next();
            String name = block.getName().getString();
            String lowercase = name.toLowerCase();
            try {
                if (block == null || lowercase.contains("torch")
                        || lowercase.contains("sign") || lowercase.contains("stairs")
                        || lowercase.contains("slab") || lowercase.contains("door") || lowercase.contains("skull")
                        || lowercase.contains("button") || lowercase.contains("head") || lowercase.contains("banner")
                        || lowercase.contains("pot") || lowercase.contains("froglight") || lowercase.contains("dripleaf")
                        || lowercase.contains("carpet") || lowercase.contains("roots") || lowercase.contains("flower")
                        || lowercase.contains("blossom") || lowercase.contains("wall") || lowercase.contains("Frog")
                        || lowercase.contains("planks") || lowercase.contains("redstone") || lowercase.contains("anvil")
                        || lowercase.contains("piston")
                        || !block.getDefaultState().isOpaque() || block.getDefaultState().exceedsCube() || block.getDefaultState().hasBlockEntity()
                        || block.getDefaultState().hasSidedTransparency() || lowercase.contains("cake") || lowercase.contains("candle")
                        || block.getDefaultState().getLuminance() > 0 || Registries.BLOCK.containsId(new Identifier("minecraft", lowercase.replaceAll(" ", "_").replaceAll("'", "_") + "_slab"))) {

                } else {
                    allSlabs.add(new Slab(lowercase.replaceAll(" ", "_").replaceAll("'", "_") + "_slab", name + " Slab", block));
                }
            } catch (IllegalArgumentException e) {
                continue;
            }
        }

    }

    public static class Slab {
        private String id;
        private String translationName;
        private Item item;
        private SlabBlock block;
        private Block original;

        private Slab(String id, String translationName, Block original) {
            this.id = id;
            this.translationName = translationName;
            this.block = Registry.register(Registries.BLOCK, new Identifier(modid, id), new SlabBlock(FabricBlockSettings.of(original.getDefaultState().getMaterial(), original.getDefaultMapColor())));
            this.original = original;
            this.item = Registry.register(Registries.ITEM, new Identifier(modid, id), new BlockItem(block, new FabricItemSettings()));
            ItemGroupEvents.modifyEntriesEvent(SLAB_ITEM_GROUP).register(content -> content.add(item));
//            allSlabs.add(this);
        }

        public String id() {return id;}
        public String translationName() {return translationName;}
        public SlabBlock block() {return block;}
        public Item item() {return item;}
        public Block original() {return original;}

    }


}
