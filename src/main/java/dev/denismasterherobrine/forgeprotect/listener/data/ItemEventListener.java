package dev.denismasterherobrine.forgeprotect.listener.data;

import dev.denismasterherobrine.forgeprotect.ForgeProtect;
import dev.denismasterherobrine.forgeprotect.database.records.Recorder;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

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

        Recorder.recordDroppedItem(itemType, itemPosition, nbtData, worldName, event.getPlayer().getName().getString());
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

        Recorder.recordPickedUpItem(itemType, itemPosition, nbtData, worldName, pickupSource);
    }
}
