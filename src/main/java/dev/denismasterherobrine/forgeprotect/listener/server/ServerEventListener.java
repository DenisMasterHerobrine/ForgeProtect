package dev.denismasterherobrine.forgeprotect.listener.server;

import dev.denismasterherobrine.forgeprotect.ForgeProtect;
import dev.denismasterherobrine.forgeprotect.database.DatabaseInitializer;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ForgeProtect.MODID)
public class ServerEventListener {
    @SubscribeEvent
    public static void onServerStopping(ServerStoppingEvent event) {
        DatabaseInitializer.closeDatabase();
    }
}
