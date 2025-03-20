package com.maciej916.server_master.commands.impl;

import com.maciej916.server_master.config.ConfigManager;
import com.maciej916.server_master.util.MessageHelper;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;

public class SMCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
        dispatcher.register(
                Commands.literal("sm")
                        .requires(source -> source.hasPermission(4))
                        .then(Commands.argument("action", StringArgumentType.string())
                                .suggests((ctx, builder) -> {
                                    builder.suggest("reload");
                                    builder.suggest("author");

                                    return builder.buildFuture();
                                })
                                .executes(ctx -> {
                                    String action = StringArgumentType.getString(ctx, "action");
                                    return handleAction(ctx.getSource(), action);
                                })
                        )
        );
    }

    private static int handleAction(CommandSourceStack source, String action) {
        ServerPlayer player = source.getPlayer();


        switch (action.toLowerCase()) {
            case "reload":
                boolean success = ConfigManager.reloadConfigs();

                MutableComponent reloadComponent = Component.empty();
                reloadComponent.append("%sm% ");

                if (success) {
                    reloadComponent.withStyle(Style.EMPTY.withColor(0x22C55E));
                    reloadComponent.append(Component.translatable("command.server_master.configuration_reloaded"));
                } else {
                    reloadComponent.withStyle(Style.EMPTY.withColor(0xFF5555));
                    reloadComponent.append(Component.translatable("command.server_master.configuration_reload_failed"));
                }

                source.sendSuccess(() -> MessageHelper.formatMessage(player, reloadComponent), true);
                break;
            case "author":
                Style authorStyle = Style.EMPTY.withColor(0xFF7F50);
                Style githubStyle = Style.EMPTY.withColor(0xFF7F50).withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/Maciej916/ServerMaster"));
                MutableComponent authorComponent = Component.empty();
                authorComponent.withStyle(Style.EMPTY.withColor(0xecb65d));
                authorComponent.append("%sm% ");
                authorComponent.append(
                        Component.translatable("command.server_master.author",
                                Component.literal("Maciej916").withStyle(authorStyle),
                                Component.literal("https://github.com/Maciej916/ServerMaster").withStyle(githubStyle)
                        )
                );

                source.sendSuccess(() -> MessageHelper.formatMessage(player, authorComponent), true);
                break;
            default:
                MutableComponent errorComponent = Component.empty();
                errorComponent.withStyle(Style.EMPTY.withColor(0xFF5555));
                errorComponent.append("%sm% ");
                errorComponent.append(Component.translatable("command.server_master.unknown_action", action));

                source.sendFailure(MessageHelper.formatMessage(player, errorComponent));
                break;
        }
        return 1;
    }
}
