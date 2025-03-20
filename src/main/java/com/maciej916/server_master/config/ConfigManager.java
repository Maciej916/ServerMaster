package com.maciej916.server_master.config;

import com.maciej916.server_master.ServerMaster;
import com.maciej916.server_master.config.api.JsonSerializable;
import com.maciej916.server_master.config.api.ModConfigType;
import com.maciej916.server_master.util.JSONHelper;
import net.neoforged.fml.loading.FMLPaths;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class ConfigManager {
    private static final Map<ModConfigType, JsonSerializable> CONFIGS = new HashMap<>();

    private static final Path CONFIG_PATH = FMLPaths.CONFIGDIR.get().resolve(ServerMaster.MOD_ID);

    public static Path getConfigFile(ModConfigType config) {
        return CONFIG_PATH.resolve(config.getConfigFile());
    }

    public static <T extends JsonSerializable> T registerNewConfig(ModConfigType name, T config) {
        CONFIGS.put(name, config);
        return config;
    }

    private static boolean initConfigDirectory() {
        try {
            Files.createDirectories(CONFIG_PATH);
            ServerMaster.LOGGER.info("Config folder exists or created at: " + CONFIG_PATH);
            return true;
        } catch (IOException e) {
            ServerMaster.LOGGER.error("Failed to create config folder: " + CONFIG_PATH, e);
        }
        return false;
    }

    public static void initConfigs() {
        ServerMaster.LOGGER.info("Initializing configs...");

        boolean hasConfigDirectory = initConfigDirectory();
        if (!hasConfigDirectory) return;

        ModConfigs.registerConfigs();
    }

    public static boolean reloadConfigs() {
        ServerMaster.LOGGER.info("Reloading configs...");
        return loadConfigs();
    }

    public static boolean loadConfigs() {
        ServerMaster.LOGGER.info("Loading configs...");

        boolean hasFullSuccess = true;

        for (Map.Entry<ModConfigType, JsonSerializable> entry : CONFIGS.entrySet()) {
            ModConfigType configType = entry.getKey();
            JsonSerializable config = entry.getValue();
            Path configFile = getConfigFile(configType);

            try {
                if (Files.exists(configFile)) {
                    ServerMaster.LOGGER.info("Loading config: " + configType.getConfigFile());
                    JSONHelper.loadConfigFromJson(config);
                } else {
                    ServerMaster.LOGGER.info("Config not found, creating default config for: " + configType.getConfigFile());
                    JSONHelper.saveConfigAsJson(config);
                }
            } catch (IOException e) {
                ServerMaster.LOGGER.error("Error handling config: " + configType.getConfigFile() + ". " + e.getMessage());
                hasFullSuccess = false;

                try {
                    if (Files.exists(configFile)) {
                        Path brokenConfigFile = configFile.getParent().resolve(configFile.getFileName().toString().replace(".json", "_broken.json"));

                        if (Files.exists(brokenConfigFile)) {
                            Files.delete(brokenConfigFile);
                            ServerMaster.LOGGER.warn("Deleted existing broken config file: " + brokenConfigFile);
                        }

                        Files.move(configFile, brokenConfigFile);
                        ServerMaster.LOGGER.warn("Renamed broken config file to: " + brokenConfigFile);

                        JSONHelper.saveConfigAsJson(config);
                        ServerMaster.LOGGER.info("Created new default config: " + configType.getConfigFile());
                    } else {
                        JSONHelper.saveConfigAsJson(config);
                    }
                } catch (IOException renameEx) {
                    ServerMaster.LOGGER.error("Failed to rename broken config file: " + configType.getConfigFile(), renameEx);
                }
            }
        }

        return hasFullSuccess;
    }

}