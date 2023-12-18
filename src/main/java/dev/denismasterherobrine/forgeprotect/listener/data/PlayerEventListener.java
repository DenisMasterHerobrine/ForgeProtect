package dev.denismasterherobrine.forgeprotect.listener.data;

import dev.denismasterherobrine.forgeprotect.ForgeProtect;
import dev.denismasterherobrine.forgeprotect.database.records.Recorder;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, modid = ForgeProtect.MODID)
public class PlayerEventListener {
    @SubscribeEvent
    public static void onPlayerDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();

            String playerName = player.getName().getString();
            BlockPos deathPosition = player.blockPosition();
            String world = player.level.dimension().location().toString();

            ListTag inventoryList = new ListTag();

            // Serialize main inventory slots (0-35)
            for (int i = 0; i < Inventory.INVENTORY_SIZE; i++) {
                ItemStack itemStack = player.getInventory().getItem(i);
                if (!itemStack.isEmpty()) {
                    CompoundTag itemTag = itemStack.save(new CompoundTag());
                    itemTag.putInt("Slot", i);
                    inventoryList.add(itemTag);
                }
            }

            // Serialize armor slots (100-103)
            for (int i = 0; i < player.getInventory().armor.size(); i++) {
                ItemStack itemStack = player.getInventory().armor.get(i);
                if (!itemStack.isEmpty()) {
                    CompoundTag itemTag = itemStack.save(new CompoundTag());
                    itemTag.putInt("Slot", 100 + i);
                    inventoryList.add(itemTag);
                }
            }

            String inventoryData = inventoryList.toString();

            // Curios API support is still WIP, so it's null at the moment.

            // Record the data
            Recorder.recordPlayerDeath(playerName, deathPosition.toString(), inventoryData, null, world);
        }
    }
}
