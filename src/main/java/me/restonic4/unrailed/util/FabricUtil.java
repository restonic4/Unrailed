package me.restonic4.unrailed.util;

import net.fabricmc.loader.api.FabricLoader;

public class FabricUtil {
    public static boolean isModLoaded(String mod_id) {
        return FabricLoader.getInstance().isModLoaded(mod_id);
    }

    public static boolean tryAddingModCompatibility(String mod_id) {
        boolean isModLoaded = isModLoaded(mod_id);
        String message = (isModLoaded) ? (mod_id + " mod found, adding compatibility") : (mod_id + " mod not found, skipping compatibility");

        System.out.println(message);

        return isModLoaded;
    }
}
