package dev.denismasterherobrine.forgeprotect.config;

import dev.denismasterherobrine.forgeprotect.ForgeProtect;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;

import java.nio.file.Path;

@Mod.EventBusSubscriber(modid = ForgeProtect.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ConfigPathHandler {
    private static Path configPath = null;

    @SubscribeEvent
    public static void onModConfigEvent(final ModConfigEvent event) {
        final ModConfig config = event.getConfig();

        // Get the path to the configuration file
        configPath = config.getFullPath();
    }

    public static Path getConfigPath() {
        return configPath;
    }
}
