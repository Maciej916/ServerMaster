package com.maciej916.server_master.config.impl;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.maciej916.server_master.config.api.JsonSerializable;
import com.maciej916.server_master.config.api.ModConfigType;
import com.maciej916.server_master.util.ComponentHelper;
import com.maciej916.server_master.util.JSONHelper;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;

import java.util.HashMap;
import java.util.Map;

public class CustomVariablesConfig implements JsonSerializable {

    private Map<String, MutableComponent> variables = new HashMap<>();

    public CustomVariablesConfig() {
        Style discordStyle = Style.EMPTY;
        discordStyle = discordStyle.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.example.com"));
        discordStyle = discordStyle.withColor(0xFF7F50);
        discordStyle = discordStyle.withBold(true);
        variables.put("discord", Component.literal("Discord").withStyle(discordStyle));

        Style rulesStyle = Style.EMPTY;
        rulesStyle = rulesStyle.withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/rules"));
        rulesStyle = rulesStyle.withColor(0xFF7F50);
        rulesStyle = rulesStyle.withItalic(true);
        variables.put("rules", Component.literal("/rules").withStyle(rulesStyle));
    }

    public Map<String, MutableComponent> getVariables() {
        return variables;
    }

    @Override
    public ModConfigType getConfigType() {
        return ModConfigType.CUSTOM_VARIABLES;
    }

    @Override
    public JsonObject save() {
        JsonObject object = new JsonObject();

        JsonObject variablesJson = new JsonObject();
        for (Map.Entry<String, MutableComponent> entry : variables.entrySet()) {
            variablesJson.add(entry.getKey(), ComponentHelper.serialize(entry.getValue()));
        }
        object.add("variables", variablesJson);

        return object;
    }

    @Override
    public void load(JsonObject json) {
        JSONHelper.checkRequiredFields(json, "variables");

        JsonObject variablesJson = json.getAsJsonObject("variables");
        for (Map.Entry<String, JsonElement> entry : variablesJson.entrySet()) {
            variables.put(entry.getKey(), ComponentHelper.deserialize(entry.getValue().getAsJsonObject()));
        }
    }
}
