package dev.denismasterherobrine.forgeprotect.listener.data;

import dev.denismasterherobrine.forgeprotect.ForgeProtect;
import dev.denismasterherobrine.forgeprotect.database.records.Recorder;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.level.ExplosionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, modid = ForgeProtect.MODID)
public class BlockEventListener {
    @SubscribeEvent
    public void onBlockBreak(BlockEvent.BreakEvent event) {
        // handle block break
        Player player = event.getPlayer();
        BlockState state = event.getState();
        BlockPos pos = event.getPos();

        BlockEntity blockEntity = player.level.getBlockEntity(pos);
        String playerName = player.getName().getString();
        String blockType = state.getBlock().getName().toString();
        String blockPosition = pos.toString();
        String worldName = player.level.dimension().location().toString();
        String blockNBT = null;

        if (blockEntity != null) {
            CompoundTag nbt;
            nbt = blockEntity.saveWithFullMetadata();
            blockNBT = nbt.toString();
        }

        Recorder.recordBlockBreak(playerName, blockType, blockPosition, worldName, blockNBT);
    }

    @SubscribeEvent
    public void onPortalSpawn(BlockEvent.PortalSpawnEvent event) {
        // handle block break
        Player player = event.getLevel().getNearestPlayer(5, 5, 5, 5, false);
        BlockState state = event.getState();
        BlockPos pos = event.getPos();

        BlockEntity blockEntity = player.level.getBlockEntity(pos);
        String playerName = player.getName().getString();
        String blockType = state.getBlock().getName().toString();
        String blockPosition = pos.toString();
        String worldName = player.level.dimension().location().toString();
        String blockNBT = null;

        if (blockEntity != null) {
            CompoundTag nbt;
            nbt = blockEntity.saveWithFullMetadata();
            blockNBT = nbt.toString();
        }

        Recorder.recordBlockPlace(playerName, blockType, blockPosition, worldName, blockNBT);
    }

    @SubscribeEvent
    public void onFarmlandTrampled(BlockEvent.FarmlandTrampleEvent event) {
        // handle block break
        Entity entity = event.getEntity();
        BlockState state = event.getState();
        BlockPos pos = event.getPos();

        BlockEntity blockEntity = entity.level.getBlockEntity(pos);
        String entityName = entity.getName().getString();
        String blockType = state.getBlock().getName().toString();
        String blockPosition = pos.toString();
        String worldName = entity.level.dimension().location().toString();
        String blockNBT = null;

        if (blockEntity != null) {
            CompoundTag nbt;
            nbt = blockEntity.saveWithFullMetadata();
            blockNBT = nbt.toString();
        }

        Recorder.recordBlockBreak("#trample{" + entityName + "}", blockType, blockPosition, worldName, blockNBT);
    }

    @SubscribeEvent
    public static void onBlockPlace(BlockEvent.EntityPlaceEvent event) {
        Entity entity = event.getEntity();

        if (entity == null) {
            return;
        }

        if (!(entity instanceof Player)) {
            BlockState state = event.getPlacedBlock();
            BlockPos pos = event.getPos();

            String name = String.valueOf(entity.getType());
            String blockType = state.getBlock().getName().toString();
            String blockPosition = pos.toString();
            String worldName = entity.level.dimension().location().toString();

            String blockNBT = null;
            BlockEntity blockEntity = entity.level.getBlockEntity(pos);

            if (blockEntity != null) {
                CompoundTag nbt = blockEntity.saveWithFullMetadata();
                blockNBT = nbt.toString();
            }

            Recorder.recordBlockPlace("entity{" + name + "}", blockType, blockPosition, worldName, blockNBT);
        } else {
            Player player = (Player) entity;
            BlockState state = event.getPlacedBlock();
            BlockPos pos = event.getPos();

            String playerName = player.getName().getString();
            String blockType = state.getBlock().getName().toString();
            String blockPosition = pos.toString();
            String worldName = player.level.dimension().location().toString();

            String blockNBT = null;
            BlockEntity blockEntity = player.level.getBlockEntity(pos);

            if (blockEntity != null) {
                CompoundTag nbt = blockEntity.saveWithFullMetadata();
                blockNBT = nbt.toString();
            }

            Recorder.recordBlockPlace(playerName, blockType, blockPosition, worldName, blockNBT);
        }
    }

    @SubscribeEvent
    public void onExplosion(ExplosionEvent.Detonate event) {
        Entity entity = event.getExplosion().getDamageSource().getEntity();

        if (entity instanceof Player) {
            return;
        }

        String agressorName = null;
        if (entity instanceof Creeper) {
            agressorName = ((Creeper) entity).getTarget().getName().getString();
        }

        List<BlockPos> blocks = event.getExplosion().getToBlow();
        blocks = blocks.stream()
                .filter(blockPos -> !event.getLevel().getBlockState(blockPos).getBlock().equals(Blocks.AIR))
                .collect(Collectors.toList());

        for (BlockPos block : blocks) {
            String blockNBT = null;
            BlockEntity blockEntity = event.getLevel().getBlockEntity(block);

            if (blockEntity != null) {
                CompoundTag nbt = blockEntity.saveWithFullMetadata();
                blockNBT = nbt.toString();
            }

            Recorder.recordBlockBreak("#explosion{" + agressorName + "}", event.getLevel().getBlockState(block).getBlock().getName().toString(), block.toString(), event.getLevel().dimension().toString(), blockNBT);
        }
    }

    @SubscribeEvent
    public static void onBlockFallStop(BlockEvent.NeighborNotifyEvent event) {
        BlockPos pos = event.getPos();
        BlockState state = event.getLevel().getBlockState(pos);

        if (state.getBlock() instanceof FallingBlock) {
            String blockType = state.getBlock().getName().toString();
            String world = event.getLevel().dimensionType().toString();

            String blockNBT = null;
            BlockEntity blockEntity = event.getLevel().getBlockEntity(pos);

            if (blockEntity != null) {
                CompoundTag nbt = blockEntity.saveWithFullMetadata();
                blockNBT = nbt.toString();
            }

            // Record the melting event
            Recorder.recordBlockUpdate("#fall", blockType, pos.toString(), world, blockNBT);
        }
    }
}
