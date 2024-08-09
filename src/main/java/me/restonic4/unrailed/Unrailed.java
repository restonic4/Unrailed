package me.restonic4.unrailed;

import me.restonic4.unrailed.block.BlockRegistryManager;
import me.restonic4.unrailed.compatibility.dynmap.DynmapCompatibilityManager;
import me.restonic4.unrailed.entity.EntityRegistryManager;
import me.restonic4.unrailed.item.ItemRegistryManager;
import me.restonic4.unrailed.util.FabricUtil;
import net.fabricmc.api.ModInitializer;

public class Unrailed implements ModInitializer {
    public static final String MOD_ID = "unrailed";

    @Override
    public void onInitialize() {
        ItemRegistryManager.register();
        EntityRegistryManager.register();
        BlockRegistryManager.register();

        if (FabricUtil.tryAddingModCompatibility("dynmap")) {
            DynmapCompatibilityManager.register();
        }
    }
}
