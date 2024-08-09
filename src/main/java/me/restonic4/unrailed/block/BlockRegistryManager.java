package me.restonic4.unrailed.block;

import me.restonic4.unrailed.Unrailed;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.List;

public class BlockRegistryManager {
    public static final Block STATION_RAIL_BLOCK = registerBlock("station_rail",
            new StationRailBlock(FabricBlockSettings.copyOf(Blocks.POWERED_RAIL)),
            List.of(CreativeModeTabs.TOOLS_AND_UTILITIES, CreativeModeTabs.REDSTONE_BLOCKS)
    );

    private static Block registerBlock(String id, Block block, List<ResourceKey<CreativeModeTab>> tabs) {
        Item item = registerBlockItem(id, block);

        if (tabs != null && !tabs.isEmpty()) {
            tabs.forEach(tab -> ItemGroupEvents.modifyEntriesEvent(tab).register(content -> content.accept(item)));
        }

        return Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(Unrailed.MOD_ID, id), block);
    }

    private static Item registerBlockItem(String name, Block block) {
        return Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(Unrailed.MOD_ID, name), new BlockItem(block, new FabricItemSettings()));
    }

    public static void register() {
        System.out.println("Adding some cool blocks i guess");
    }
}
