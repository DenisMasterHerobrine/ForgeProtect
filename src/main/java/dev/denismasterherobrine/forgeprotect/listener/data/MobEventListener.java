package dev.denismasterherobrine.forgeprotect.listener.data;

import dev.denismasterherobrine.forgeprotect.ForgeProtect;
import dev.denismasterherobrine.forgeprotect.database.DatabaseInitializer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
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

import java.sql.PreparedStatement;
import java.sql.SQLException;
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
            recordMobDeathToDatabase(entity);
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

        String sql =
                "INSERT INTO block_events (player_name, block_type, block_position, action, nbt_data, world) " +
                        "VALUES (?, ?, ?, ?, ?, ?)";

        for (BlockPos block : blocks) {
            String blockNBT = null;
            BlockEntity blockEntity = event.getLevel().getBlockEntity(block);
            if (blockEntity != null) {
                CompoundTag nbt;
                nbt = blockEntity.saveWithFullMetadata();
                blockNBT = nbt.toString();
            }

            try {
                PreparedStatement statement = DatabaseInitializer.getDatabaseConnection().prepareStatement(sql);

                statement.setString(1, "#explosion{" + agressorName + "}");
                statement.setString(2, event.getLevel().getBlockState(block).getBlock().getName().toString());
                statement.setString(3, block.toString());
                statement.setString(4, "BREAK_BLOCK");
                statement.setString(5, blockNBT);
                statement.setString(6, event.getLevel().dimension().toString());

                statement.executeUpdate();
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void recordMobDeathToDatabase(Entity entity) {
        String sql = "INSERT INTO mob_events (mob_type, event_type, mob_position, nbt_data, world) " +
                "VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement pstmt = DatabaseInitializer.getDatabaseConnection().prepareStatement(sql);
            pstmt.setString(1, EntityType.getKey(entity.getType()).toString());
            pstmt.setString(2, "DEATH");
            pstmt.setString(3, entity.getPosition(0).toString());
            pstmt.setString(4, entity.serializeNBT().toString());
            pstmt.setString(5, entity.level.dimension().toString());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
