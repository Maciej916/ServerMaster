package com.maciej916.server_master.config.api;

public enum ModConfigType {
    AUTO_MESSENGER,
    WELCOME_MESSAGE,
    MOTD,
    CUSTOM_VARIABLES,
    RULES
    ;

    public String getConfigFileName() {
        return this.name().toLowerCase();
    }

    public String getConfigFile() {
        return this.name().toLowerCase() + ".json";
    }
}
