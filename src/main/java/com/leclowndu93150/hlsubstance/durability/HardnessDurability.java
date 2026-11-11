package com.leclowndu93150.hlsubstance.durability;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public final class HardnessDurability {

    private static final ThreadLocal<Integer> BLOCK_COST = new ThreadLocal<>();

    public static void begin(Level level, BlockState state, BlockPos pos) {
        if (level.isClientSide || !DurabilityConfig.hardnessScalingEnabled()) return;
        BLOCK_COST.set(Math.max(1, (int) Math.round(state.getDestroySpeed(level, pos) * DurabilityConfig.hardnessMultiplier())));
    }

    public static void end() {
        BLOCK_COST.remove();
    }

    public static int scale(int amount) {
        Integer blockCost = BLOCK_COST.get();
        return blockCost == null || amount <= 0 ? amount : amount * blockCost;
    }
}
