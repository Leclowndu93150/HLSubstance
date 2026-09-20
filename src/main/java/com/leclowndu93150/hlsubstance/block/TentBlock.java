package com.leclowndu93150.hlsubstance.block;

import com.leclowndu93150.hlsubstance.spawn.TentSpawnTracker;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class TentBlock extends BedBlock {

    public static final MapCodec<BedBlock> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(DyeColor.CODEC.fieldOf("color").forGetter(BedBlock::getColor), propertiesCodec())
                    .apply(instance, TentBlock::new));

    public TentBlock(DyeColor color, BlockBehaviour.Properties properties) {
        super(color, properties);
    }

    @Override
    public MapCodec<BedBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide && level instanceof ServerLevel serverLevel) {
            notifyDestroyed(serverLevel, pos, state);
        }
        return super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    protected void onRemove(BlockState oldState, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!level.isClientSide && level instanceof ServerLevel serverLevel && !newState.is(this)) {
            notifyDestroyed(serverLevel, pos, oldState);
        }
        super.onRemove(oldState, level, pos, newState, movedByPiston);
    }

    private static void notifyDestroyed(ServerLevel level, BlockPos pos, BlockState state) {
        BlockPos head = state.getValue(PART) == BedPart.HEAD
                ? pos
                : pos.relative(state.getValue(FACING));
        TentSpawnTracker.onTentDestroyed(level, head);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return null;
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return TentShapes.get(state.getValue(PART), state.getValue(FACING));
    }

    @Override
    protected VoxelShape getBlockSupportShape(BlockState state, BlockGetter level, BlockPos pos) {
        return TentShapes.getSupport(state.getValue(PART), state.getValue(FACING));
    }
}
