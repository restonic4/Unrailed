package me.restonic4.unrailed.block;

import me.restonic4.unrailed.mixin.AbstractMinecartMixin;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.block.PoweredRailBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class StationRailBlock extends PoweredRailBlock {
    public static final BooleanProperty INVERTED = BooleanProperty.create("inverted");

    public StationRailBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState()
                .setValue(this.getShapeProperty(), RailShape.NORTH_SOUTH)
                .setValue(POWERED, false)
                .setValue(WATERLOGGED, false)
                .setValue(INVERTED, false));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockState state = super.getStateForPlacement(ctx);
        boolean isInverted = switch (state.getValue(this.getShapeProperty())) {
            case EAST_WEST -> ctx.getHorizontalDirection() == Direction.EAST;
            case NORTH_SOUTH -> ctx.getHorizontalDirection() == Direction.SOUTH;
            default -> throw new UnsupportedOperationException();
        };
        return state.setValue(INVERTED, isInverted);
    }

    public Vec3 getPushVector(BlockState state) {
        return switch (state.getValue(this.getShapeProperty())) {
            case ASCENDING_EAST, ASCENDING_WEST, EAST_WEST -> new Vec3(0.5, 0, 0);
            case ASCENDING_SOUTH, ASCENDING_NORTH, NORTH_SOUTH -> new Vec3(0, 0, 0.5);
            default -> throw new UnsupportedOperationException();
        };
    }

    public void affectMinecart(AbstractMinecart minecart, BlockState state, BlockPos blockPos) {
        Vec3 pushForce = getPushVector(state);

        if (state.getValue(INVERTED)) {
            pushForce = pushForce.reverse();
        }

        if (state.getValue(POWERED)) {
            Vec3 currentVelocity = minecart.getDeltaMovement();

            double minSpeed = 0.02;
            double friction = 0.5;

            Vec3 reducedVelocity  = currentVelocity.scale(friction);

            reducedVelocity  = new Vec3(
                    Math.max(Math.abs(reducedVelocity .x), minSpeed) * Math.signum(reducedVelocity .x),
                    reducedVelocity .y,
                    Math.max(Math.abs(reducedVelocity .z), minSpeed) * Math.signum(reducedVelocity .z)
            );

            double speedMagnitude = reducedVelocity.length();

            Vec3 pushDirection = pushForce.normalize();

            Vec3 finalVelocity = pushDirection.scale(speedMagnitude);

            minecart.setDeltaMovement(finalVelocity);
        }
        else {
            minecart.setDeltaMovement(minecart.getDeltaMovement().add(pushForce));
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(this.getShapeProperty(), POWERED, BlockStateProperties.WATERLOGGED, INVERTED);
    }
}
