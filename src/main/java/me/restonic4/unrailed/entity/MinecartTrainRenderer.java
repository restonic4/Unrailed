package me.restonic4.unrailed.entity;

import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MinecartRenderer;
import net.minecraft.resources.ResourceLocation;

public class MinecartTrainRenderer extends MinecartRenderer<MinecartTrain> {
    public MinecartTrainRenderer(EntityRendererProvider.Context context) {
        super(context, ModelLayers.FURNACE_MINECART);
    }

    @Override
    public ResourceLocation getTextureLocation(MinecartTrain entity) {
        return new ResourceLocation("unrailed", "textures/entity/train_minecart.png");
    }
}
