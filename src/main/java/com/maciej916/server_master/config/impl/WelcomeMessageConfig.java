package com.maciej916.server_master.config.impl;

import com.google.gson.JsonObject;
import com.maciej916.server_master.config.api.JsonSerializable;
import com.maciej916.server_master.config.api.ModConfigType;
import com.maciej916.server_master.util.JSONHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class WelcomeMessageConfig implements JsonSerializable {
    private boolean enabled = true;
    private String message = "&uWelcome &w%player_name%&u!";
    private int fadeIn = 20;
    private int stay = 100;
    private int fadeOut = 20;

    private boolean playSound = true;
    private String soundLocation = "minecraft:entity.experience_orb.pickup";

    public boolean isEnabled() {
        return enabled;
    }

    public MutableComponent getMessage() {
        return Component.literal(message);
    }

    public int getFadeIn() {
        return fadeIn;
    }

    public int getStay() {
        return stay;
    }

    public int getFadeOut() {
        return fadeOut;
    }

    public boolean isPlaySound() {
        return playSound;
    }

    public String getSoundLocation() {
        return soundLocation;
    }

    @Override
    public ModConfigType getConfigType() {
        return ModConfigType.WELCOME_MESSAGE;
    }

    @Override
    public JsonObject save() {
        JsonObject object = new JsonObject();

        object.addProperty("enabled", enabled);
        object.addProperty("message", message);
        object.addProperty("fade_in", fadeIn);
        object.addProperty("stay", stay);
        object.addProperty("fade_out", fadeOut);

        object.addProperty("play_sound", playSound);
        object.addProperty("sound_location", soundLocation);

        return object;
    }

    @Override
    public void load(JsonObject json) {
        JSONHelper.checkRequiredFields(json, "enabled", "message", "fade_in", "stay", "fade_out", "play_sound", "sound_location");

        enabled = json.get("enabled").getAsBoolean();
        message = json.get("message").getAsString();
        fadeIn = json.get("fade_in").getAsInt();
        stay = json.get("fade_in").getAsInt();
        fadeOut = json.get("fade_out").getAsInt();

        playSound = json.get("play_sound").getAsBoolean();
        soundLocation = json.get("sound_location").getAsString();
    }
}
