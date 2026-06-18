package com.leclowndu93150.hlsubstance.spawn;

import com.leclowndu93150.hlsubstance.Hlsubstance;
import com.leclowndu93150.hlsubstance.block.TentBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.Nullable;

@Mod.EventBusSubscriber(modid = Hlsubstance.MODID)
public final class TentSpawnTracker {

    private static final String BACKUP_TAG = "hlsubstance:tent_backup_spawn";
    private static final String KEY_DIM = "Dim";
    private static final String KEY_X = "X";
    private static final String KEY_Y = "Y";
    private static final String KEY_Z = "Z";
    private static final String KEY_ANGLE = "Angle";
    private static final String KEY_FORCED = "Forced";

    private TentSpawnTracker() {}

    public static void beforeSetRespawn(ServerPlayer player, ResourceKey<Level> newDim, @Nullable BlockPos newSpawn) {
        if (newSpawn == null) {
            clearBackup(player);
            return;
        }
        ServerLevel newLevel = player.server.getLevel(newDim);
        if (newLevel == null) return;
        boolean newIsTent = newLevel.getBlockState(newSpawn).getBlock() instanceof TentBlock;
        if (!newIsTent) {
            clearBackup(player);
            return;
        }

        BlockPos currentSpawn = player.getRespawnPosition();
        ResourceKey<Level> currentDim = player.getRespawnDimension();
        ServerLevel currentLevel = player.server.getLevel(currentDim);
        boolean currentIsTent = currentSpawn != null
                && currentLevel != null
                && currentLevel.getBlockState(currentSpawn).getBlock() instanceof TentBlock;

        if (currentIsTent) return;
        writeBackup(player, currentDim, currentSpawn, player.getRespawnAngle(), player.isRespawnForced());
    }

    @SubscribeEvent
    public static void onClone(PlayerEvent.Clone event) {
        if (!event.isWasDeath()) return;
        CompoundTag oldData = event.getOriginal().getPersistentData();
        if (oldData.contains(BACKUP_TAG)) {
            event.getEntity().getPersistentData().put(BACKUP_TAG, oldData.getCompound(BACKUP_TAG).copy());
        }
    }

    public static void onTentDestroyed(ServerLevel level, BlockPos headPos) {
        for (ServerPlayer player : level.getServer().getPlayerList().getPlayers()) {
            if (!level.dimension().equals(player.getRespawnDimension())) continue;
            BlockPos respawn = player.getRespawnPosition();
            if (respawn == null) continue;
            if (!isPartOfTent(level, headPos, respawn)) continue;

            BackupSpawn backup = readBackup(player);
            if (backup != null && backup.pos != null) {
                player.setRespawnPosition(backup.dim, backup.pos, backup.angle, backup.forced, false);
            } else {
                player.setRespawnPosition(Level.OVERWORLD, null, 0.0F, false, false);
            }
            clearBackup(player);
        }
    }

    private static boolean isPartOfTent(ServerLevel level, BlockPos headPos, BlockPos candidate) {
        if (candidate.equals(headPos)) return true;
        BlockState head = level.getBlockState(headPos);
        if (head.getBlock() instanceof TentBlock) {
            BlockPos foot = headPos.relative(head.getValue(TentBlock.FACING).getOpposite());
            if (candidate.equals(foot)) return true;
        }
        BlockState candidateState = level.getBlockState(candidate);
        if (candidateState.hasProperty(TentBlock.PART)
                && candidateState.getValue(TentBlock.PART) == BedPart.FOOT
                && candidate.relative(candidateState.getValue(TentBlock.FACING)).equals(headPos)) {
            return true;
        }
        return candidate.distSqr(headPos) <= 2;
    }

    private static void writeBackup(Player player, ResourceKey<Level> dim, @Nullable BlockPos pos, float angle, boolean forced) {
        CompoundTag tag = new CompoundTag();
        tag.putString(KEY_DIM, dim.location().toString());
        if (pos != null) {
            tag.putInt(KEY_X, pos.getX());
            tag.putInt(KEY_Y, pos.getY());
            tag.putInt(KEY_Z, pos.getZ());
        }
        tag.putFloat(KEY_ANGLE, angle);
        tag.putBoolean(KEY_FORCED, forced);
        player.getPersistentData().put(BACKUP_TAG, tag);
    }

    private static void clearBackup(Player player) {
        player.getPersistentData().remove(BACKUP_TAG);
    }

    @Nullable
    private static BackupSpawn readBackup(Player player) {
        CompoundTag data = player.getPersistentData();
        if (!data.contains(BACKUP_TAG)) return null;
        CompoundTag tag = data.getCompound(BACKUP_TAG);
        ResourceKey<Level> dim = ResourceKey.create(Registries.DIMENSION,
                new ResourceLocation(tag.getString(KEY_DIM)));
        BlockPos pos = tag.contains(KEY_X)
                ? new BlockPos(tag.getInt(KEY_X), tag.getInt(KEY_Y), tag.getInt(KEY_Z))
                : null;
        return new BackupSpawn(dim, pos, tag.getFloat(KEY_ANGLE), tag.getBoolean(KEY_FORCED));
    }

    private record BackupSpawn(ResourceKey<Level> dim, @Nullable BlockPos pos, float angle, boolean forced) {}
}
