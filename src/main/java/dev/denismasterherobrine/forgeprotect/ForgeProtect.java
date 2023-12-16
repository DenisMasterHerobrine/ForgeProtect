package dev.denismasterherobrine.forgeprotect;

import com.mojang.logging.LogUtils;
import dev.denismasterherobrine.forgeprotect.config.ConfigHandler;
import dev.denismasterherobrine.forgeprotect.config.ConfigPathHandler;
import dev.denismasterherobrine.forgeprotect.database.DatabaseInitializer;
import dev.denismasterherobrine.forgeprotect.listener.data.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(ForgeProtect.MODID)
public class ForgeProtect {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "forgeprotect";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final ConfigHandler SERVER_CONFIG = new ConfigHandler();

    public ForgeProtect() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new BlockEventListener());
        MinecraftForge.EVENT_BUS.register(new ItemEventListener());
        MinecraftForge.EVENT_BUS.register(new MobEventListener());
        MinecraftForge.EVENT_BUS.register(new PlayerEventListener());
        MinecraftForge.EVENT_BUS.register(new ContainerEventListener());

        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, SERVER_CONFIG.getSpec());
    }

    @SubscribeEvent
    public void onServerStarting(final ServerStartingEvent event) {
        String dbDir = ConfigHandler.DATABASE_DIR.get();
        Path dbPath = ConfigPathHandler.getConfigPath().getParent();
        dbPath = Paths.get(dbPath.toString(), dbDir);

        if (!Files.exists(dbPath)) {
            try {
                Files.createDirectories(dbPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            DatabaseInitializer.setPath(dbPath);
            DatabaseInitializer.createDatabase();
        }
    }
}
