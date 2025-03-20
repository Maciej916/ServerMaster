package com.maciej916.server_master.util;

import com.google.gson.*;
import com.maciej916.server_master.config.ConfigManager;
import com.maciej916.server_master.config.api.JsonSerializable;
import com.maciej916.server_master.config.api.ModConfigType;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class JSONHelper {

    /**
     * Save the config as a JSON file.
     * @param config The config object to save
     * @throws IOException If an I/O error occurs during saving the file
     */
    public static void saveConfigAsJson(JsonSerializable config) throws IOException {
        ModConfigType configType = config.getConfigType();
        Path configFile = ConfigManager.getConfigFile(configType);

        // Get the data from the config as a JsonObject
        JsonObject data = config.save();

        // Ensure the directory exists
        if (!Files.exists(configFile.getParent())) {
            Files.createDirectories(configFile.getParent());
        }

        // Write the JsonObject to the JSON file with pretty printing
        try (FileWriter writer = new FileWriter(configFile.toFile())) {
            Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
            gson.toJson(data, writer);
        }
    }

    /**
     * Load the config from a JSON file.
     * @param config The config object to load
     * @throws IOException If an I/O error occurs during loading the file
     */
    public static void loadConfigFromJson(JsonSerializable config) throws IOException {
        ModConfigType configType = config.getConfigType();
        Path configFile = ConfigManager.getConfigFile(configType);

        if (!Files.exists(configFile)) {
            throw new IOException("Config file does not exist: " + configFile);
        }

        try (FileReader reader = new FileReader(configFile.toFile())) {
            JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
            config.load(json);
        } catch (IllegalArgumentException e) {
            throw new IOException("Invalid config: Missing required fields in " + configFile + ". " + e.getMessage(), e);
        } catch (IOException | JsonParseException e) {
            throw new IOException("Error reading config file: " + configFile, e);
        }
    }

    public static List<String> jsonArrayToList(JsonArray jsonArray) {
        List<String> list = new ArrayList<>();
        for (JsonElement element : jsonArray) {
            list.add(element.getAsString());
        }
        return list;
    }

    public static void checkRequiredFields(JsonObject json, String... requiredFields) {
        List<String> missingFields = new ArrayList<>();

        for (String field : requiredFields) {
            if (!json.has(field)) {
                missingFields.add(field);
            }
        }

        if (!missingFields.isEmpty()) {
            throw new IllegalArgumentException("Missing fields: " + String.join(", ", missingFields));
        }
    }

}
