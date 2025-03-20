package com.maciej916.server_master.config.api;

import com.google.gson.JsonObject;

public interface JsonSerializable {

    ModConfigType getConfigType();
    JsonObject save();
    void load(JsonObject json) throws IllegalArgumentException;
}
