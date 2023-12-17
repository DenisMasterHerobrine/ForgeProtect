package dev.denismasterherobrine.forgeprotect.listener.data;

import dev.denismasterherobrine.forgeprotect.ForgeProtect;
import dev.denismasterherobrine.forgeprotect.database.records.Recorder;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

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
    public static void onBlockPlace(BlockEvent.EntityPlaceEvent event) {
        Entity entity = event.getEntity();

        if (!(entity instanceof Player)) {
            return; // not placed by a player
        }

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
