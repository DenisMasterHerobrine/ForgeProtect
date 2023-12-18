package dev.denismasterherobrine.forgeprotect.listener.data;

import dev.denismasterherobrine.forgeprotect.ForgeProtect;
import dev.denismasterherobrine.forgeprotect.database.records.Recorder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

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


}
