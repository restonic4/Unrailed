package me.restonic4.unrailed.entity;

import me.restonic4.unrailed.Unrailed;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.vehicle.MinecartTNT;

public class EntityRegistryManager {
    public static final EntityType<MinecartTrain> TRAIN_MINECART = Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            new ResourceLocation(Unrailed.MOD_ID, "train_minecart"),
            FabricEntityTypeBuilder.<MinecartTrain>create(MobCategory.MISC, MinecartTrain::new)
                    .dimensions(EntityDimensions.fixed(0.98F, 0.7F))
                    .trackRangeBlocks(8)
                    .build());

    public static void register() {
        System.out.println("Adding some cool entities i guess");
    }
}
