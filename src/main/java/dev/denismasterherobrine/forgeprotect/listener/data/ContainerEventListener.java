package dev.denismasterherobrine.forgeprotect.listener.data;

import dev.denismasterherobrine.forgeprotect.ForgeProtect;
import dev.denismasterherobrine.forgeprotect.database.records.Recorder;
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
                Recorder.recordRetrieveItem(player, item, retrievedAmount, pos.toString(), player.getLevel().dimension().toString(), initialTag);
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
                Recorder.recordDepositItem(player, item, depositedAmount, pos.toString(), player.getLevel().dimension().toString(), afterTag);
            }
        }

        initialItems.clear();
        afterItems.clear();
    }
}
