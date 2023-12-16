package dev.denismasterherobrine.forgeprotect.listener.data;

import dev.denismasterherobrine.forgeprotect.ForgeProtect;
import dev.denismasterherobrine.forgeprotect.database.DatabaseInitializer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, modid = ForgeProtect.MODID)
public class BlockEventListener {
    @SubscribeEvent
    public void onBlockBreak(BlockEvent.BreakEvent event) {
        // handle block break
        Player player = event.getPlayer();
        BlockState state = event.getState();
        BlockPos pos = event.getPos();

        String playerName = player.getName().getString();
        String blockType = state.getBlock().getName().toString();
        String blockPosition = pos.toString();
        String worldName = player.level.dimension().location().toString();

        String blockNBT = null;
        BlockEntity blockEntity = player.level.getBlockEntity(pos);
        if (blockEntity != null) {
            CompoundTag nbt;
            nbt = blockEntity.saveWithFullMetadata();
            blockNBT = nbt.toString();
        }

        String sql =
                "INSERT INTO block_events (player_name, block_type, block_position, action, nbt_data, world) " +
                        "VALUES (?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement statement = DatabaseInitializer.getDatabaseConnection().prepareStatement(sql);

            statement.setString(1, playerName);
            statement.setString(2, blockType);
            statement.setString(3, blockPosition);
            statement.setString(4, "BREAK_BLOCK");
            statement.setString(5, blockNBT);
            statement.setString(6, worldName);

            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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

        String sql =
                "INSERT INTO block_events (player_name, block_type, block_position, action, nbt_data, world) " +
                        "VALUES (?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement statement = DatabaseInitializer.getDatabaseConnection().prepareStatement(sql);

            statement.setString(1, playerName);
            statement.setString(2, blockType);
            statement.setString(3, blockPosition);
            statement.setString(4, "PLACE_BLOCK");
            statement.setString(5, blockNBT);
            statement.setString(6, worldName);

            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
