package com.gamerduck.slabs;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.client.*;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;
import static com.gamerduck.slabs.EvenMoarSlabs.modid;

public class DataGeneratorEntry implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        final FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        pack.addProvider(SlabGenerator::new);
        pack.addProvider(SlabLootGenerator::new);
        pack.addProvider(SlabRecipeGenerator::new);
        pack.addProvider(SlabEnglishLangGenerator::new);
    }

    public class SlabGenerator extends FabricModelProvider {

        public SlabGenerator(FabricDataOutput output) {
            super(output);
        }

        @Override
        public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
            for (Slabs.Slab slab : Slabs.allSlabs) {
                Block block = slab.original();
                TextureMap textureMap = TextureMap.all(block);
                Identifier identifier = Models.SLAB.upload(slab.block(), textureMap, blockStateModelGenerator.modelCollector);
                Identifier identifier2 = Models.SLAB_TOP.upload(slab.block(), textureMap, blockStateModelGenerator.modelCollector);
                Identifier identifier3 = Models.CUBE_COLUMN.uploadWithoutVariant(slab.block(), "_double", TextureMap.all(slab.original()), blockStateModelGenerator.modelCollector);
                blockStateModelGenerator.blockStateCollector.accept(blockStateModelGenerator.createSlabBlockState(slab.block(), identifier, identifier2, identifier3));
            }
            for (Stairs.Stair stairs : Stairs.allStairs) {
                Block block = stairs.original();
                TextureMap textureMap = TextureMap.all(block);
                Identifier identifier = Models.INNER_STAIRS.upload(stairs.block(), textureMap, blockStateModelGenerator.modelCollector);
                Identifier identifier2 = Models.STAIRS.upload(stairs.block(), textureMap, blockStateModelGenerator.modelCollector);
                Identifier identifier3 = Models.OUTER_STAIRS.upload(stairs.block(), textureMap, blockStateModelGenerator.modelCollector);
                blockStateModelGenerator.blockStateCollector.accept(BlockStateModelGenerator.createStairsBlockState(stairs.block(), identifier, identifier2, identifier3));
            }
            for (Walls.Wall wall : Walls.allWalls) {
                Block block = wall.original();
                TextureMap textureMap = TextureMap.all(block);
                Identifier identifier = Models.TEMPLATE_WALL_POST.upload(wall.block(), textureMap, blockStateModelGenerator.modelCollector);
                Identifier identifier2 = Models.TEMPLATE_WALL_SIDE.upload(wall.block(), textureMap, blockStateModelGenerator.modelCollector);
                Identifier identifier3 = Models.TEMPLATE_WALL_SIDE_TALL.upload(wall.block(), textureMap, blockStateModelGenerator.modelCollector);
                blockStateModelGenerator.blockStateCollector.accept(BlockStateModelGenerator.createWallBlockState(wall.block(), identifier, identifier2, identifier3));
            }
        }

        @Override
        public void generateItemModels(ItemModelGenerator itemModelGenerator) {
            for (Slabs.Slab slab : Slabs.allSlabs) {
                Models.SLAB.upload(new Identifier(modid, slab.id() + "_item"), TextureMap.all(slab.original()), itemModelGenerator.writer, (id, textures) -> {
                    return Models.SLAB.createJson(id, textures);
                });
            }
            for (Stairs.Stair stairs : Stairs.allStairs) {
                Models.STAIRS.upload(new Identifier(modid, stairs.id() + "_item"), TextureMap.all(stairs.original()), itemModelGenerator.writer, (id, textures) -> {
                    return Models.STAIRS.createJson(id, textures);
                });
            }
            for (Walls.Wall wall : Walls.allWalls) {
                Models.FENCE_INVENTORY.upload(new Identifier(modid, wall.id() + "_item"), TextureMap.all(wall.original()), itemModelGenerator.writer, (id, textures) -> {
                    return Models.FENCE_INVENTORY.createJson(id, textures);
                });
            }
        }
    }

    public class SlabLootGenerator extends FabricBlockLootTableProvider {

        protected SlabLootGenerator(FabricDataOutput dataOutput) {
            super(dataOutput);
        }

        @Override
        public void generate() {
            Slabs.allSlabs.forEach(slab -> addDrop(slab.block(), drops(slab.item())));
            Stairs.allStairs.forEach(stairs -> addDrop(stairs.block(), drops(stairs.item())));
            Walls.allWalls.forEach(walls -> addDrop(walls.block(), drops(walls.item())));
        }
    }

    public class SlabRecipeGenerator extends FabricRecipeProvider {

        public SlabRecipeGenerator(FabricDataOutput output) {
            super(output);
        }

        @Override
        public void generate(Consumer<RecipeJsonProvider> exporter) {
            for (Slabs.Slab slab : Slabs.allSlabs) {
                ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, slab.block(), 6)
                        .criterion(slab.id() + "_has_original", conditionsFromItem(slab.original()))
                        .input('c', slab.original())
                        .pattern("ccc")
                        .offerTo(exporter);
            }
            for (Stairs.Stair stairs : Stairs.allStairs) {
                ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, stairs.block(), 6)
                        .criterion(stairs.id() + "_has_original", conditionsFromItem(stairs.original()))
                        .input('c', stairs.original())
                        .input('a', Blocks.AIR)
                        .pattern("caa")
                        .pattern("cca")
                        .pattern("ccc")
                        .offerTo(exporter);
            }
            for (Walls.Wall wall : Walls.allWalls) {
                ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, wall.block(), 6)
                        .criterion(wall.id() + "_has_original", conditionsFromItem(wall.original()))
                        .input('c', wall.original())
                        .pattern("ccc")
                        .pattern("ccc")
                        .offerTo(exporter);
            }
        }
    }

    public class SlabEnglishLangGenerator extends FabricLanguageProvider {

        protected SlabEnglishLangGenerator(FabricDataOutput dataOutput) {
            super(dataOutput, "en_us");
        }

        @Override
        public void generateTranslations(TranslationBuilder translationBuilder) {
            translationBuilder.add(EvenMoarSlabs.SLAB_ITEM_GROUP, "Even Moar Slabs");
            translationBuilder.add(EvenMoarSlabs.STAIRS_ITEM_GROUP, "Even Moar Stairs");
            translationBuilder.add(EvenMoarSlabs.WALLS_ITEM_GROUP, "Even Moar Walls");
            Slabs.allSlabs.forEach(slab -> translationBuilder.add(slab.block(), slab.translationName()));
            Stairs.allStairs.forEach(stairs -> translationBuilder.add(stairs.block(), stairs.translationName()));
            Walls.allWalls.forEach(walls -> translationBuilder.add(walls.block(), walls.translationName()));
        }
    }

}
