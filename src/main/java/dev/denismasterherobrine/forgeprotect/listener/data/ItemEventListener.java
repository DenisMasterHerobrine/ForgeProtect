package dev.denismasterherobrine.forgeprotect.listener.data;

import dev.denismasterherobrine.forgeprotect.ForgeProtect;
import dev.denismasterherobrine.forgeprotect.database.DatabaseInitializer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, modid = ForgeProtect.MODID)
public class ItemEventListener {
    @SubscribeEvent
    public static void onItemToss(ItemTossEvent event) {
        Player player = event.getPlayer();
        ItemStack itemStack = event.getEntity().getItem();
        BlockPos pos = event.getEntity().getOnPos();

        String itemType = itemStack.getItem().getName(itemStack).toString();
        String itemPosition = pos.toString();
        String worldName = player.level.dimension().location().toString();
        String dropSource = player.getName().getString();

        CompoundTag nbt = new CompoundTag();
        itemStack.save(nbt);
        String nbtData = nbt.toString();

        String sql =
                "INSERT INTO item_events (item_type, drop_time, item_position, nbt_data, world, drop_source) " +
                        "VALUES (?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement statement = DatabaseInitializer.getDatabaseConnection().prepareStatement(sql);

            statement.setString(1, itemType);
            statement.setString(2, new Timestamp(System.currentTimeMillis()).toString());
            statement.setString(3, itemPosition);
            statement.setString(4, nbtData);
            statement.setString(5, worldName);
            statement.setString(6, dropSource);

            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @SubscribeEvent
    public static void onItemPickup(EntityItemPickupEvent event) {
        Player player = event.getEntity();
        ItemStack itemStack = event.getItem().getItem();
        BlockPos pos = event.getItem().blockPosition();

        String itemType = itemStack.getItem().getName(itemStack).toString();
        String itemPosition = pos.toString();
        String worldName = player.level.dimension().location().toString();
        String pickupSource = player.getName().getString();

        CompoundTag nbt = new CompoundTag();
        itemStack.save(nbt);
        String nbtData = nbt.toString();

        String sql =
                "INSERT INTO item_events (item_type, drop_time, item_position, nbt_data, world, pickup_source) " +
                        "VALUES (?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement statement = DatabaseInitializer.getDatabaseConnection().prepareStatement(sql);

            statement.setString(1, itemType);
            statement.setString(2, new Timestamp(System.currentTimeMillis()).toString());
            statement.setString(3, itemPosition);
            statement.setString(4, nbtData);
            statement.setString(5, worldName);
            statement.setString(6, pickupSource);

            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
