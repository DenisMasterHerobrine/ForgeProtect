package dev.denismasterherobrine.forgeprotect.listener.data;

import dev.denismasterherobrine.forgeprotect.ForgeProtect;
import dev.denismasterherobrine.forgeprotect.database.DatabaseInitializer;
import dev.denismasterherobrine.forgeprotect.util.PositionUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, modid = ForgeProtect.MODID)
public class ContainerEventListener {
    private final Map<ItemStack, CompoundTag> initialItems = new HashMap<>();
    private final Map<ItemStack, CompoundTag> afterItems = new HashMap<>();

    @SubscribeEvent
    public void onInteractionOpen(PlayerContainerEvent.Open event) {
        Player player = event.getEntity();
        BlockPos pos = PositionUtils.getLookingAt(event.getEntity(), 4.0);
        BlockEntity blockEntity = event.getEntity().getLevel().getBlockEntity(pos);

        if (blockEntity == null) {
            return;
        }

        CompoundTag nbt = blockEntity.saveWithFullMetadata();
        ListTag itemList = nbt.getList("Items", 10); // assuming "Items" is the key

        for (int i = 0; i < itemList.size(); i++) {
            CompoundTag itemTag = itemList.getCompound(i);
            ItemStack item = ItemStack.of(itemTag);
            initialItems.put(item, itemTag);
        }
    }

    @SubscribeEvent
    public void onInteractionClose(PlayerContainerEvent.Close event) {
        Player player = event.getEntity();
        BlockPos pos = PositionUtils.getLookingAt(event.getEntity(), 4.0);
        BlockEntity blockEntity = event.getEntity().getLevel().getBlockEntity(pos);

        if (blockEntity == null) {
            return;
        }

        CompoundTag nbt = blockEntity.saveWithFullMetadata();
        ListTag itemList = nbt.getList("Items", 10); // assuming "Items" is the key

        for (int i = 0; i < itemList.size(); i++) {
            CompoundTag itemTag = itemList.getCompound(i);
            ItemStack item = ItemStack.of(itemTag);
            afterItems.put(item, itemTag);
        }

        for (Map.Entry<ItemStack, CompoundTag> entry : initialItems.entrySet()) {
            ItemStack item = entry.getKey();
            CompoundTag initialTag = entry.getValue();
            int initialCount = initialTag.getInt("Count");

            CompoundTag afterTag = afterItems.get(item);
            int afterCount = afterTag == null ? 0 : afterTag.getInt("Count");

            if (initialCount > afterCount) {
                // Item count decreased or item disappeared, item was retrieved
                int retrievedAmount = initialCount - afterCount;
                recordRetrieveToDatabase(player, item, retrievedAmount, pos.toString(), player.getLevel().dimension().toString(), initialTag);
            }
        }

        for (Map.Entry<ItemStack, CompoundTag> entry : afterItems.entrySet()) {
            ItemStack item = entry.getKey();
            CompoundTag afterTag = entry.getValue();
            int afterCount = afterTag.getInt("Count");

            CompoundTag initialTag = initialItems.get(item);
            int initialCount = initialTag == null ? 0 : initialTag.getInt("Count");

            if (afterCount > initialCount) {
                // Item count increased or new item appeared, item was deposited
                int depositedAmount = afterCount - initialCount;
                recordDepositToDatabase(player, item, depositedAmount, pos.toString(), player.getLevel().dimension().toString(), afterTag);
            }
        }

        initialItems.clear();
        afterItems.clear();
    }

    private void recordDepositToDatabase(Player player, ItemStack item, int amount, String containerPosition, String world, CompoundTag tag) {
        String sql =
                "INSERT INTO item_container_events (event_type, item_type, event_time, container_position, nbt_data, world, player) " +
                "VALUES(?, ?, ?, ?, ?, ?, ?)";


        try {
            PreparedStatement pstmt = DatabaseInitializer.getDatabaseConnection().prepareStatement(sql);
            pstmt.setString(1, "DEPOSIT");
            pstmt.setString(2, item.getItem().getName(item).toString()); // Assuming this method exists
            pstmt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            pstmt.setString(4, containerPosition);
            pstmt.setString(5, tag == null ? null : tag.toString());
            pstmt.setString(6, world);
            pstmt.setString(7, player.getName().getString()); // Assuming this method exists
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void recordRetrieveToDatabase(Player player, ItemStack item, int amount, String containerPosition, String world, CompoundTag tag) {
        String sql = "INSERT INTO item_container_events(event_type, item_type, event_time, container_position, nbt_data, world, player) VALUES(?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement pstmt = DatabaseInitializer.getDatabaseConnection().prepareStatement(sql);
            pstmt.setString(1, "RETRIEVE");
            pstmt.setString(2, item.getItem().getName(item).toString()); // Assuming this method exists
            pstmt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            pstmt.setString(4, containerPosition);
            pstmt.setString(5, tag == null ? null : tag.toString());
            pstmt.setString(6, world);
            pstmt.setString(7, player.getName().getString()); // Assuming this method exists
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
