package com.maciej916.server_master.config;

import com.maciej916.server_master.config.api.ModConfigType;
import com.maciej916.server_master.config.impl.*;

public class ModConfigs {
    public static AutoMessengerConfig AUTO_MESSENGER_CONFIG;
    public static WelcomeMessageConfig WELCOME_MESSAGE_CONFIG;
    public static MotdConfig MOTD_CONFIG;
    public static CustomVariablesConfig CUSTOM_VARIABLES_CONFIG;
    public static RulesConfig RULES_CONFIG;
    public static JoinLeaveMessagesConfig JOIN_LEAVE_MESSAGES_CONFIG;


    public static void registerConfigs() {
        AUTO_MESSENGER_CONFIG = ConfigManager.registerNewConfig(ModConfigType.AUTO_MESSENGER, new AutoMessengerConfig());
        WELCOME_MESSAGE_CONFIG = ConfigManager.registerNewConfig(ModConfigType.WELCOME_MESSAGE, new WelcomeMessageConfig());
        MOTD_CONFIG = ConfigManager.registerNewConfig(ModConfigType.MOTD, new MotdConfig());
        CUSTOM_VARIABLES_CONFIG = ConfigManager.registerNewConfig(ModConfigType.CUSTOM_VARIABLES, new CustomVariablesConfig());
        RULES_CONFIG = ConfigManager.registerNewConfig(ModConfigType.RULES, new RulesConfig());
        JOIN_LEAVE_MESSAGES_CONFIG = ConfigManager.registerNewConfig(ModConfigType.JOIN_LEAVE_MESSAGES, new JoinLeaveMessagesConfig());
    }
}
