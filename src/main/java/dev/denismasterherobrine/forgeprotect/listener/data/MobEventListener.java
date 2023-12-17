package dev.denismasterherobrine.forgeprotect.listener.data;

import dev.denismasterherobrine.forgeprotect.ForgeProtect;
import dev.denismasterherobrine.forgeprotect.database.records.Recorder;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.level.ExplosionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, modid = ForgeProtect.MODID)
public class MobEventListener {
    @SubscribeEvent
    public void onMobDeath(LivingDeathEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity instanceof Player) {
            return;
        }

        if (entity instanceof Mob) {
            Recorder.recordMobDeath(entity);
        }
    }

    @SubscribeEvent
    public void onExplosion(ExplosionEvent event) {
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
                CompoundTag nbt;
                nbt = blockEntity.saveWithFullMetadata();
                blockNBT = nbt.toString();
            }

            Recorder.recordBlockBreak("#explosion{" + agressorName + "}", event.getLevel().getBlockState(block).getBlock().getName().toString(), block.toString(), event.getLevel().dimension().toString(), blockNBT);
        }
    }
}
