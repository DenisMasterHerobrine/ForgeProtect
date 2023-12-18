package dev.denismasterherobrine.forgeprotect.database.records;

import dev.denismasterherobrine.forgeprotect.database.DatabaseInitializer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Recorder {
    private static final ExecutorService executor = Executors.newCachedThreadPool();

    public static void recordBlockBreak(String playerName, String blockType, String blockPosition, String world, String nbtData) {
        executor.submit(() -> {
            String sql = "INSERT INTO block_events (player_name, block_type, block_position, action, nbt_data, world) VALUES (?, ?, ?, ?, ?, ?)";

            try {
                PreparedStatement statement = DatabaseInitializer.getDatabaseConnection().prepareStatement(sql);
                statement.setString(1, playerName);
                statement.setString(2, blockType);
                statement.setString(3, blockPosition);
                statement.setString(4, "BREAK_BLOCK");
                statement.setString(5, nbtData);
                statement.setString(6, world);

                statement.executeUpdate();
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public static void recordBlockUpdate(String playerName, String blockType, String blockPosition, String world, String nbtData) {
        executor.submit(() -> {
            String sql = "INSERT INTO block_events (player_name, block_type, block_position, action, nbt_data, world) VALUES (?, ?, ?, ?, ?, ?)";

            try {
                PreparedStatement statement = DatabaseInitializer.getDatabaseConnection().prepareStatement(sql);
                statement.setString(1, playerName);
                statement.setString(2, blockType);
                statement.setString(3, blockPosition);
                statement.setString(4, "UPDATE_BLOCK");
                statement.setString(5, nbtData);
                statement.setString(6, world);

                statement.executeUpdate();
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public static void recordBlockPlace(String playerName, String blockType, String blockPosition, String world, String nbtData) {
        executor.submit(() -> {
            String sql = "INSERT INTO block_events (player_name, block_type, block_position, action, nbt_data, world) VALUES (?, ?, ?, ?, ?, ?)";

            try {
                PreparedStatement statement = DatabaseInitializer.getDatabaseConnection().prepareStatement(sql);

                statement.setString(1, playerName);
                statement.setString(2, blockType);
                statement.setString(3, blockPosition);
                statement.setString(4, "PLACE_BLOCK");
                statement.setString(5, nbtData);
                statement.setString(6, world);

                statement.executeUpdate();
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public static void recordDepositItem(Player player, ItemStack item, Integer amount, String containerPosition, String world, CompoundTag tag) {
        executor.submit(() -> {
            String sql = "INSERT INTO item_container_events (event_type, amount, item_type, event_time, container_position, nbt_data, world, player) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";

            try {
                PreparedStatement statement = DatabaseInitializer.getDatabaseConnection().prepareStatement(sql);
                statement.setString(1, "DEPOSIT");
                statement.setInt(2, amount);
                statement.setString(3, item.getItem().getName(item).toString()); // Assuming this method exists
                statement.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
                statement.setString(5, containerPosition);
                statement.setString(6, tag == null ? null : tag.toString());
                statement.setString(7, world);
                statement.setString(8, player.getName().getString()); // Assuming this method exists

                statement.executeUpdate();
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public static void recordRetrieveItem(Player player, ItemStack item, Integer amount, String containerPosition, String world, CompoundTag tag) {
        executor.submit(() -> {
            String sql = "INSERT INTO item_container_events (event_type, amount, item_type, event_time, container_position, nbt_data, world, player) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";

            try {
                PreparedStatement statement = DatabaseInitializer.getDatabaseConnection().prepareStatement(sql);
                statement.setString(1, "RETRIEVE");
                statement.setInt(2, amount);
                statement.setString(3, item.getItem().getName(item).toString());
                statement.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
                statement.setString(5, containerPosition);
                statement.setString(6, tag == null ? null : tag.toString());
                statement.setString(7, world);
                statement.setString(8, player.getName().getString());

                statement.executeUpdate();
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public static void recordDroppedItem(String itemType, String itemPosition, String nbtData, String world, String dropSource) {
        executor.submit(() -> {
            String sql = "INSERT INTO item_events (item_type, drop_time, item_position, nbt_data, world, drop_source) VALUES (?, ?, ?, ?, ?, ?)";

            try {
                PreparedStatement statement = DatabaseInitializer.getDatabaseConnection().prepareStatement(sql);
                statement.setString(1, itemType);
                statement.setString(2, new Timestamp(System.currentTimeMillis()).toString());
                statement.setString(3, itemPosition);
                statement.setString(4, nbtData);
                statement.setString(5, world);
                statement.setString(6, dropSource);

                statement.executeUpdate();
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public static void recordPickedUpItem(String itemType, String itemPosition, String nbtData, String world, String pickupSource) {
        executor.submit(() -> {
            String sql = "INSERT INTO item_events (item_type, drop_time, item_position, nbt_data, world, pickup_source) VALUES (?, ?, ?, ?, ?, ?)";

            try {
                PreparedStatement statement = DatabaseInitializer.getDatabaseConnection().prepareStatement(sql);
                statement.setString(1, itemType);
                statement.setString(2, new Timestamp(System.currentTimeMillis()).toString());
                statement.setString(3, itemPosition);
                statement.setString(4, nbtData);
                statement.setString(5, world);
                statement.setString(6, pickupSource);

                statement.executeUpdate();
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public static void recordMobDeath(Entity entity) {
        executor.submit(() -> {
            String sql = "INSERT INTO mob_events (mob_type, event_type, mob_position, nbt_data, world, reason) VALUES (?, ?, ?, ?, ?, ?)";
            try {
                PreparedStatement statement = DatabaseInitializer.getDatabaseConnection().prepareStatement(sql);
                statement.setString(1, EntityType.getKey(entity.getType()).toString());
                statement.setString(2, "DEATH");
                statement.setString(3, entity.getPosition(0).toString());
                statement.setString(4, entity.serializeNBT().toString());
                statement.setString(5, entity.level.dimension().toString());
                statement.setString(6, entity.getRemovalReason() == null ? "UNKNOWN" : entity.getRemovalReason().toString());
                statement.executeUpdate();
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public static void recordPlayerDeath(String playerName, String deathPosition, String inventoryData, String curiosData, String world) {
        executor.submit(() -> {
            String sql = "INSERT INTO player_deaths (player_name, death_position, inventory_data, curios_data, world) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = DatabaseInitializer.getDatabaseConnection().prepareStatement(sql)) {
                pstmt.setString(1, playerName);
                pstmt.setString(2, deathPosition);
                pstmt.setString(3, inventoryData);
                pstmt.setString(4, curiosData);
                pstmt.setString(5, world);
                pstmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    // Similar methods for recordBlockPlace, recordItemRetrieved, etc.

    // Shutdown method to properly close the executor
    public static void shutdown() {
        executor.shutdown();
    }
}
