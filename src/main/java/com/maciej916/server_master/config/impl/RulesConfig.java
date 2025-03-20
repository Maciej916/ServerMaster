package com.maciej916.server_master.config.impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.maciej916.server_master.config.api.JsonSerializable;
import com.maciej916.server_master.config.api.ModConfigType;
import com.maciej916.server_master.util.JSONHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.List;

public class RulesConfig implements JsonSerializable {
    private boolean enabled = true;
    private boolean runInSinglePlayer = false;
    private int rulesPerPage = 5;
    private String rulesTop = "&u>---------- Rules (&w%page% &u/ &w%total_pages%&u) -----------<";
    private String ruleIndicator = "&w%id% &u::";
    private String rulesBottom = "&u>---------------------------------<";
    private List<String> rules = List.of(
        "&wBe Respectful &u– Treat other players with respect.",
        "&wNo Griefing &u– Don't destroy or steal other players' builds or items.",
        "&wNo Cheating or Hacking &u– No use of mods, X-ray, or any unfair advantages.",
        "&wNo Spawn Killing &u– Don't kill players repeatedly near spawn points.",
        "&wKeep Builds Appropriate &u– No inappropriate or offensive structures.",
        "&wDon’t Spam Chat &u– No excessive messages, caps, or advertising in the chat.",
        "&wNo Lag Machines &u– Don’t build devices that intentionally cause server lag.",
        "&wFollow Staff Instructions &u– Listen to server moderators and admins."
    );

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isRunInSinglePlayer() {
        return runInSinglePlayer;
    }

    public String getRulesTop() {
        return rulesTop;
    }

    public String getRuleIndicator() {
        return ruleIndicator;
    }

    public String getRulesBottom() {
        return rulesBottom;
    }

    public List<MutableComponent> getRules() {
        return rules.stream().map(Component::literal).toList();
    }

    public int getRulesPerPage() {
        return rulesPerPage;
    }

    public int getRulesPages() {
        return (int) Math.ceil((double) rules.size() / rulesPerPage);
    }

    @Override
    public ModConfigType getConfigType() {
        return ModConfigType.RULES;
    }

    @Override
    public JsonObject save() {
        JsonObject object = new JsonObject();

        object.addProperty("enabled", enabled);
        object.addProperty("run_in_single_player", runInSinglePlayer);
        object.addProperty("rules_per_page", rulesPerPage);
        object.addProperty("rules_top", rulesTop);
        object.addProperty("rule_indicator", ruleIndicator);
        object.addProperty("rules_bottom", rulesBottom);

        JsonArray rulesArray = new JsonArray();
        for (String rule : rules) {
            rulesArray.add(rule);
        }
        object.add("rules", rulesArray);

        return object;
    }

    @Override
    public void load(JsonObject json) {
        JSONHelper.checkRequiredFields(json, "enabled", "run_in_single_player", "rules_per_page", "rules_top", "rule_indicator", "rules_bottom", "rules");

        enabled = json.get("enabled").getAsBoolean();
        runInSinglePlayer = json.get("run_in_single_player").getAsBoolean();
        rulesPerPage = json.get("rules_per_page").getAsInt();
        rulesTop = json.get("rules_top").getAsString();
        ruleIndicator = json.get("rule_indicator").getAsString();
        rulesBottom = json.get("rules_bottom").getAsString();

        JsonArray rulesArray = json.getAsJsonArray("rules");
        rules = JSONHelper.jsonArrayToList(rulesArray);
    }
}
