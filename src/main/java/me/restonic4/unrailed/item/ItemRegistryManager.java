package me.restonic4.unrailed.item;

import me.restonic4.unrailed.Unrailed;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.*;

public class ItemRegistryManager {
    public static final Item TRAIN_MINECART = Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(Unrailed.MOD_ID, "train_minecart"),
            new TrainMinecartItem(
                    (new Item.Properties()).stacksTo(1)
            )
    );

    public static void addItemToTab(Item item, ResourceKey<CreativeModeTab> creativeModeTab) {
        ItemGroupEvents.modifyEntriesEvent(creativeModeTab).register(content -> {
            content.accept(item);
        });
    }

    public static void register() {
        System.out.println("Adding some cool items i guess");

        addItemToTab(TRAIN_MINECART, CreativeModeTabs.REDSTONE_BLOCKS);
        addItemToTab(TRAIN_MINECART, CreativeModeTabs.TOOLS_AND_UTILITIES);
    }
}
