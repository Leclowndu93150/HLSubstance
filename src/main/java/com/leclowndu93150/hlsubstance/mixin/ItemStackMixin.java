package com.leclowndu93150.hlsubstance.mixin;

import com.leclowndu93150.hlsubstance.durability.HardnessDurability;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @Inject(method = "mineBlock", at = @At("HEAD"))
    private void hlsubstance$beginHardnessScaling(Level level, BlockState state, BlockPos pos, Player player, CallbackInfo callbackInfo) {
        HardnessDurability.begin(level, state, pos);
    }

    @Inject(method = "mineBlock", at = @At("RETURN"))
    private void hlsubstance$endHardnessScaling(Level level, BlockState state, BlockPos pos, Player player, CallbackInfo callbackInfo) {
        HardnessDurability.end();
    }

    @ModifyVariable(
            method = "hurtAndBreak(ILnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/EquipmentSlot;)V",
            at = @At("HEAD"),
            argsOnly = true)
    private int hlsubstance$scaleDurabilityLoss(int amount) {
        return HardnessDurability.scale(amount);
    }
}
