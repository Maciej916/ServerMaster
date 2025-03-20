package com.maciej916.server_master.commands.impl;

import com.maciej916.server_master.config.ModConfigs;
import com.maciej916.server_master.config.impl.RulesConfig;
import com.maciej916.server_master.util.MessageHelper;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;

import java.util.Collections;
import java.util.List;

public class RulesCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
        dispatcher.register(
                Commands.literal("rules")
                        .executes(ctx -> showRules(ctx.getSource(), 1))
                        .then(Commands.argument("page", IntegerArgumentType.integer(1, ModConfigs.RULES_CONFIG.getRulesPages()))
                                .executes(ctx -> {
                                    int page = IntegerArgumentType.getInteger(ctx, "page");
                                    return showRules(ctx.getSource(), page);
                                }))
        );
    }

    private static int showRules(CommandSourceStack source, int page) {
        RulesConfig config = ModConfigs.RULES_CONFIG;
        ServerPlayer player = source.getPlayer();
        boolean isDedicatedServer =source.getServer().isDedicatedServer();

        if (!config.isEnabled()) {
            source.sendFailure(Component.translatable("command.server_master.rules_disabled").withColor(0xFF5555));
            return 1;
        } else if (!config.isRunInSinglePlayer() && !isDedicatedServer) {
            source.sendFailure(Component.translatable("command.server_master.rules_disabled_single_player").withColor(0xFF5555));
            return 1;
        }

        int totalPages = config.getRulesPages();
        List<MutableComponent> currentRules = getCurrentRules(config, page);

        if (!config.getRulesTop().isEmpty()) {
            source.sendSuccess(() -> MessageHelper.formatMessage(player, Component.literal(config.getRulesTop()
                    .replace("%page%", String.valueOf(page))
                    .replace("%total_pages%", String.valueOf(totalPages))
            )), false);
        }

        int index = 1 + (config.getRulesPerPage() * page) - config.getRulesPerPage();

        for (MutableComponent currentRule : currentRules) {
            MutableComponent ruleComponent = Component.empty();
            ruleComponent.append(Component.literal(config.getRuleIndicator().replace("%id%", String.valueOf(index)) + " "));
            ruleComponent.append(currentRule);
            source.sendSuccess(() -> MessageHelper.formatMessage(player, ruleComponent), false);
            index++;
        }

        if (!config.getRulesBottom().isEmpty()) {
            source.sendSuccess(() -> MessageHelper.formatMessage(player, Component.literal(config.getRulesBottom())), false);
        }

        return 1;
    }

    public static List<MutableComponent> getCurrentRules(RulesConfig config, int page) {
        int rulesPerPage = config.getRulesPerPage();
        List<MutableComponent> rulesList = config.getRules();

        int fromIndex = (page - 1) * rulesPerPage;
        int toIndex = Math.min(fromIndex + rulesPerPage, rulesList.size());

        if (fromIndex >= rulesList.size() || fromIndex < 0) {
            return Collections.emptyList();
        }

        return rulesList.subList(fromIndex, toIndex);
    }
}
