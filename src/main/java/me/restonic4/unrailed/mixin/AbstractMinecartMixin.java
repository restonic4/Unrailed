package me.restonic4.unrailed.mixin;

import me.restonic4.unrailed.block.BlockRegistryManager;
import me.restonic4.unrailed.block.StationRailBlock;
import me.restonic4.unrailed.entity.MinecartTrain;
import me.restonic4.unrailed.item.ItemRegistryManager;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.vehicle.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractMinecart.class)
public class AbstractMinecartMixin {
    @Inject(method = "getPickResult", at = @At("HEAD"), cancellable = true)
    private void getPickResult(CallbackInfoReturnable<ItemStack> cir) {
        AbstractMinecart abstractMinecart = (AbstractMinecart) (Object) this;

        if (abstractMinecart instanceof MinecartTrain) {
            cir.setReturnValue(new ItemStack(ItemRegistryManager.TRAIN_MINECART));
        }
    }

    @Inject(method = "moveAlongTrack", at = @At(value = "RETURN", ordinal = 1), cancellable = true)
    private void injectedStationRailCheck(BlockPos blockPos, BlockState state, CallbackInfo ci) {
        if (state.is(BlockRegistryManager.STATION_RAIL_BLOCK)) {
            ((StationRailBlock)state.getBlock()).affectMinecart((AbstractMinecart)(Object)this, state, blockPos);
            ci.cancel();
        }
    }
}

