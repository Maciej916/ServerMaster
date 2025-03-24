package com.maciej916.server_master.config.impl;

import com.google.gson.JsonObject;
import com.maciej916.server_master.config.api.JsonSerializable;
import com.maciej916.server_master.config.api.ModConfigType;
import com.maciej916.server_master.util.JSONHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class JoinLeaveMessagesConfig implements JsonSerializable {
    private boolean enabled = true;
    private String joinMessage = "&a+ Welcome, %player_name%!";
    private String leaveMessage = "&c- Goodbye, %player_name%!";

    public boolean isEnabled() {
        return enabled;
    }

    public MutableComponent getJoinMessage() {
        return Component.literal(joinMessage);
    }

    public MutableComponent getLeaveMessage() {
        return Component.literal(leaveMessage);
    }

    @Override
    public ModConfigType getConfigType() {
        return ModConfigType.JOIN_LEAVE_MESSAGES;
    }

    @Override
    public JsonObject save() {
        JsonObject object = new JsonObject();

        object.addProperty("enabled", enabled);
        object.addProperty("join_message", joinMessage);
        object.addProperty("leave_message", leaveMessage);

        return object;
    }

    @Override
    public void load(JsonObject json) {
        JSONHelper.checkRequiredFields(json, "enabled", "join_message", "leave_message");

        enabled = json.get("enabled").getAsBoolean();
        joinMessage = json.get("join_message").getAsString();
        leaveMessage = json.get("leave_message").getAsString();
    }
}
