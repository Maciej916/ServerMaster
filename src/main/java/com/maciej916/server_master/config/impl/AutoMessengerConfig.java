package com.maciej916.server_master.config.impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.maciej916.server_master.config.api.JsonSerializable;
import com.maciej916.server_master.config.api.ModConfigType;
import com.maciej916.server_master.util.JSONHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.List;

public class AutoMessengerConfig implements JsonSerializable {
    private boolean enabled = true;
    private boolean runInSinglePlayer = false;
    private int interval = 600;
    private boolean randomMessage = false;
    private List<String> messages = List.of(
            "&uPlayer: &w%player%",
            "&uDimension: &w%dimension%",
            "&uTPS: &w%tps%",
            "&uTPS Raw: &w%tps_raw%",
            "&uPlayers: &w%players%",
            "&uMax Players: &w%max_players%",
            "&uServer Time: &w%server_time%",
            "&uWorld: &w%world%",
            "&uCoords: &w%coords%",
            "&uHealth: &w%health%",
            "&uFood: &w%food%",
            "&uEXP: &w%exp%",
            "&uTime: &w%time%",
            "&uUUID: &w%uuid%",
            "&uPlayer Name: &w%player_name%",
            "&uLocation: &w%location%",
            "&uWeather: &w%weather%",
            "&uServer Name: &w%server_name%",
            "&uGamerule: &w%gamerule%",
            "&uGamemode: &w%gamemode%",
            "&uBiome: &w%biome%",
            "&uLast Login: &w%last_login%",
            "&uCurrent Server Tick: &w%current_server_tick%"
    );

    public boolean isEnabled() {
        return enabled;
    }

    public int getInterval() {
        return interval;
    }

    public boolean isRunInSinglePlayer() {
        return runInSinglePlayer;
    }

    public List<MutableComponent> getMessages() {
        return messages.stream().map(Component::literal).toList();
    }

    public boolean isRandomMessage() {
        return randomMessage;
    }

    @Override
    public ModConfigType getConfigType() {
        return ModConfigType.AUTO_MESSENGER;
    }

    @Override
    public JsonObject save() {
        JsonObject object = new JsonObject();

        object.addProperty("enabled", enabled);
        object.addProperty("run_in_single_player", runInSinglePlayer);
        object.addProperty("interval", interval);
        object.addProperty("random_message", randomMessage);

        JsonArray messagesArray = new JsonArray();
        for (String message : messages) {
            messagesArray.add(message);
        }
        object.add("messages", messagesArray);

        return object;
    }

    @Override
    public void load(JsonObject json) {
        JSONHelper.checkRequiredFields(json, "enabled", "run_in_single_player", "interval", "random_message", "messages");

        enabled = json.get("enabled").getAsBoolean();
        runInSinglePlayer = json.get("run_in_single_player").getAsBoolean();
        interval = json.get("interval").getAsInt();
        randomMessage = json.get("random_message").getAsBoolean();

        JsonArray messagesArray = json.getAsJsonArray("messages");
        messages = JSONHelper.jsonArrayToList(messagesArray);
    }
}
