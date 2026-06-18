package com.leclowndu93150.hlsubstance.mixin;

import com.leclowndu93150.hlsubstance.spawn.TentSpawnTracker;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public class ServerPlayerSetSpawnMixin {

    @Inject(method = "setRespawnPosition", at = @At("HEAD"))
    private void hlsubstance$captureBackup(ResourceKey<Level> dim, BlockPos pos, float angle, boolean forced, boolean updateMessage, CallbackInfo ci) {
        TentSpawnTracker.beforeSetRespawn((ServerPlayer) (Object) this, dim, pos);
    }
}
