package me.restonic4.unrailed.client;

import me.restonic4.unrailed.block.BlockRegistryManager;
import me.restonic4.unrailed.entity.EntityRegistryManager;
import me.restonic4.unrailed.entity.MinecartTrainRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.renderer.RenderType;

public class UnrailedClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(EntityRegistryManager.TRAIN_MINECART, MinecartTrainRenderer::new);

        BlockRenderLayerMap.INSTANCE.putBlock(BlockRegistryManager.STATION_RAIL_BLOCK, RenderType.translucent());
    }
}
