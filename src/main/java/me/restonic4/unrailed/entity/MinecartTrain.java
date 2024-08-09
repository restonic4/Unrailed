package me.restonic4.unrailed.entity;

import me.restonic4.unrailed.compatibility.dynmap.DynmapCompatibilityManager;
import me.restonic4.unrailed.item.ItemRegistryManager;
import me.restonic4.unrailed.util.FabricUtil;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.TicketType;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.vehicle.Minecart;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BlastFurnaceBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class MinecartTrain extends Minecart {
    public MinecartTrain(EntityType<? extends MinecartTrain> entityType, Level level) {
        super(entityType, level);

        if (FabricUtil.isModLoaded("dynmap")) {
            DynmapCompatibilityManager.createMarker(this);
        }
    }

    public MinecartTrain(Level level, double d, double e, double f) {
        super(EntityRegistryManager.TRAIN_MINECART, level);

        this.setPos(d, e, f);
        this.xo = d;
        this.yo = e;
        this.zo = f;

        if (FabricUtil.isModLoaded("dynmap")) {
            DynmapCompatibilityManager.createMarker(this);
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.level().isClientSide() && this.level() instanceof ServerLevel serverLevel) {
            ChunkPos chunkPos = this.chunkPosition();

            //Load a 3x3 chunk area around the minecart
            ChunkPos currentChunk = new ChunkPos(chunkPos.x, chunkPos.z);
            serverLevel.getChunkSource().addRegionTicket(TicketType.FORCED, currentChunk, 3, currentChunk);

            //Dynmap compatibility, updates the marker
            if (FabricUtil.isModLoaded("dynmap") && isMoving()) {
                DynmapCompatibilityManager.updateMarker(this);
            }
        }
    }

    @Override
    public void remove(RemovalReason reason) {
        super.remove(reason);

        //Dynmap compatibility, removes the marker
        if (FabricUtil.isModLoaded("dynmap")) {
            DynmapCompatibilityManager.removeMarker(this);
        }
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand interactionHand) {
        return InteractionResult.PASS;
    }

    @Override
    public AbstractMinecart.Type getMinecartType() {
        return Type.CHEST;
    }

    @Override
    public BlockState getDefaultDisplayBlockState() {
        return Blocks.BLAST_FURNACE.defaultBlockState().setValue(BlastFurnaceBlock.FACING, Direction.NORTH);
    }

    @Override
    protected Item getDropItem() {
        return ItemRegistryManager.TRAIN_MINECART;
    }

    public boolean isMoving() {
        double velocityX = this.getDeltaMovement().x();
        double velocityY = this.getDeltaMovement().y();
        double velocityZ = this.getDeltaMovement().z();

        return velocityX != 0 || velocityY != 0 || velocityZ != 0;
    }
}
