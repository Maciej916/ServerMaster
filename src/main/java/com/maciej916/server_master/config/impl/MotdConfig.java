package com.maciej916.server_master.config.impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.maciej916.server_master.config.api.JsonSerializable;
import com.maciej916.server_master.config.api.ModConfigType;
import com.maciej916.server_master.util.JSONHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.List;

public class MotdConfig implements JsonSerializable {
    private boolean enabled = true;
    private boolean runInSinglePlayer = false;
    private List<String> messages = List.of(
            "&uWelcome &w%player% &uto our server, while you're here please &l&ubehave&r&u and &l&ufollow &r&uthe rules&r&u!",
            "&uYou can check our rules by typing %rules%",
            "&uServer Time &w%server_time%",
            "&uCurrent players: &w%players%&u/&w%max_players%",
            "&uJoin our %discord% for updates and support!"
    );

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isRunInSinglePlayer() {
        return runInSinglePlayer;
    }

    public List<MutableComponent> getMessages() {
        return messages.stream().map(Component::literal).toList();
    }

    @Override
    public ModConfigType getConfigType() {
        return ModConfigType.MOTD;
    }

    @Override
    public JsonObject save() {
        JsonObject object = new JsonObject();

        object.addProperty("enabled", enabled);
        object.addProperty("run_in_single_player", runInSinglePlayer);

        JsonArray messagesArray = new JsonArray();
        for (String message : messages) {
            messagesArray.add(message);
        }
        object.add("messages", messagesArray);

        return object;
    }

    @Override
    public void load(JsonObject json) {
        JSONHelper.checkRequiredFields(json, "enabled", "run_in_single_player", "messages");

        enabled = json.get("enabled").getAsBoolean();
        runInSinglePlayer = json.get("run_in_single_player").getAsBoolean();

        JsonArray messagesArray = json.getAsJsonArray("messages");
        messages = JSONHelper.jsonArrayToList(messagesArray);
    }
}
