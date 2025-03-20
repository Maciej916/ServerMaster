package com.maciej916.server_master.util;

import com.google.common.math.Stats;
import com.maciej916.server_master.config.ModConfigs;
import com.maciej916.server_master.data.ModDataAttachments;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.util.TimeUtil;
import net.minecraft.world.TickRateManager;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.biome.Biome;
import org.joml.Math;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageHelper {
    private static final Pattern SPLIT_PATTERN = Pattern.compile("(&[0-9a-fk-ors-x]|%[^%]+%)");
    private static final DecimalFormat TIME_FORMATTER = new DecimalFormat("########0.000");

    public static MutableComponent formatMessage(ServerPlayer serverPlayer, MutableComponent message) {
        List<Component> components = new ArrayList<>();

        for (Component component : message.toFlatList()) {
            String text = component.getString();
            Style currentStyle = component.getStyle();

            Matcher matcher = SPLIT_PATTERN.matcher(text);
            int lastIndex = 0;

            while (matcher.find()) {
                // Add any text before the match using the current style
                if (matcher.start() > lastIndex) {
                    String beforeMatch = text.substring(lastIndex, matcher.start());
                    if (!beforeMatch.isEmpty()) {
                        components.add(Component.literal(beforeMatch).setStyle(currentStyle));
                    }
                }

                String match = matcher.group();

                if (match.startsWith("&")) {
                    // Update the style based on color code
                    currentStyle = getStyleFromColorCode(match, currentStyle);
                } else if (match.startsWith("%") && match.endsWith("%")) {
                    String variable = match.substring(1, match.length() - 1);

                    // Get the variable component with the current style
                    MutableComponent variableComponent = getVariableComponent(variable, serverPlayer).copy();

                    if (variableComponent.getStyle().isEmpty()) {
                        variableComponent = variableComponent.setStyle(currentStyle);
                    }

                    components.add(variableComponent);
                }

                lastIndex = matcher.end();
            }

            // Append any remaining text after the last match
            if (lastIndex < text.length()) {
                components.add(Component.literal(text.substring(lastIndex)).setStyle(currentStyle));
            }
        }

        // Create the final component by combining all processed components
        MutableComponent finalComponent = Component.empty();
        for (Component c : components) {
            finalComponent.append(c);
        }

        return finalComponent;
    }

    private static Style getStyleFromColorCode(String colorCode, Style currentStyle) {
        if (colorCode == null || colorCode.isEmpty()) return currentStyle;

        TextColor color = getTextColorFromCode(colorCode);

        if (color != null) {
            currentStyle = currentStyle.withColor(color);
        }

        currentStyle = switch (colorCode) {
            case "&k" -> currentStyle.withObfuscated(true);
            case "&l" -> currentStyle.withBold(true);
            case "&m" -> currentStyle.withStrikethrough(true);
            case "&n" -> currentStyle.withUnderlined(true);
            case "&o" -> currentStyle.withItalic(true);
            case "&r" -> Style.EMPTY; // Reset to empty style
            default -> currentStyle;
        };

        return currentStyle;
    }

    private static TextColor getTextColorFromCode(String colorCode) {
        return switch (colorCode) {
            case "&0" -> TextColor.fromRgb(0x000000);
            case "&1" -> TextColor.fromRgb(0x0000AA);
            case "&2" -> TextColor.fromRgb(0x00AA00);
            case "&3" -> TextColor.fromRgb(0x00AAAA);
            case "&4" -> TextColor.fromRgb(0xAA0000);
            case "&5" -> TextColor.fromRgb(0xAA00AA);
            case "&6" -> TextColor.fromRgb(0xFFAA00);
            case "&7" -> TextColor.fromRgb(0xAAAAAA);
            case "&8" -> TextColor.fromRgb(0x555555);
            case "&9" -> TextColor.fromRgb(0x5555FF);
            case "&a" -> TextColor.fromRgb(0x55FF55);
            case "&b" -> TextColor.fromRgb(0x55FFFF);
            case "&c" -> TextColor.fromRgb(0xFF5555);
            case "&d" -> TextColor.fromRgb(0xFF55FF);
            case "&e" -> TextColor.fromRgb(0xFFFF55);
            case "&f" -> TextColor.fromRgb(0xFFFFFF);

            case "&s" -> TextColor.fromRgb(0xFF4500);
            case "&t" -> TextColor.fromRgb(0xFF69B4);
            case "&u" -> TextColor.fromRgb(0xecb65d);
            case "&w" -> TextColor.fromRgb(0xFF7F50);
            case "&x" -> TextColor.fromRgb(0x22C55E);
            default -> null;
        };
    }

    private static Component getVariableComponent(String variable, ServerPlayer player) {
        Map<String, MutableComponent> customVariables = ModConfigs.CUSTOM_VARIABLES_CONFIG.getVariables();

        if (customVariables.containsKey(variable)) {
            return customVariables.get(variable);
        }

        return switch (variable) {
            case "sm" -> getSmComponent();
            case "player" -> player.getDisplayName();
            case "tps" -> getTPS(player, true);
            case "tps_raw" -> getTPS(player, false);
            case "dimension" -> Component.literal(Objects.requireNonNull(player.level().dimension().location().toString()));
            case "players" -> Component.literal(String.valueOf(player.server.getPlayerList().getPlayerCount()));
            case "max_players" -> Component.literal(String.valueOf(player.server.getPlayerList().getMaxPlayers()));
            case "server_time" -> getServerTime();
            case "world" -> Component.literal(player.level().dimension().location().toString());
            case "coords" -> Component.literal(String.format("X: %.2f, Y: %.2f, Z: %.2f", player.getX(), player.getY(), player.getZ()));
            case "health" -> Component.literal(String.format("%.1f", player.getHealth()));
            case "food" -> Component.literal(String.valueOf(player.getFoodData().getFoodLevel()));
            case "exp" -> Component.literal(String.valueOf(player.experienceLevel));
            case "time" -> Component.literal(String.valueOf(player.level().getDayTime()));
            case "uuid" -> Component.literal(player.getUUID().toString());
            case "player_name" -> Component.literal(player.getName().getString());
            case "location" -> Component.literal(String.format("X: %.2f, Y: %.2f, Z: %.2f", player.getX(), player.getY(), player.getZ()));
            case "weather" -> {
                if (player.level().isRaining()) {
                    if (player.level().isThundering()) {
                        yield Component.literal("Thunderstorm");
                    } else {
                        yield Component.literal("Rain");
                    }
                } else {
                    yield Component.literal("Clear");
                }
            }
            case "server_name" -> Component.literal(player.server.getServerModName());
            case "gamerule" -> Component.literal(String.valueOf(player.server.getGameRules().getBoolean(GameRules.RULE_DAYLIGHT)));
            case "gamemode" -> Component.literal(String.valueOf(player.gameMode.getGameModeForPlayer()));
            case "biome" -> getPlayerBiome(player);
            case "last_login" -> getPlayerLastLogin(player);
            case "current_server_tick" -> Component.literal(String.valueOf(player.server.getTickCount()));
            default -> Component.literal("%" + variable + "%");
        };
    }

    private static Component getSmComponent() {
        Style smStyle = Style.EMPTY.withColor(0xFF4500).withItalic(true);
        return Component.literal("[SM]").withStyle(smStyle);
    }

    public static Component getServerTime() {
        LocalDateTime now = LocalDateTime.now(); // Gets current server time
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"); // Define the format

        return Component.literal(now.format(formatter)); // Return formatted date and time as a component
    }

    public static Component getPlayerBiome(ServerPlayer player) {
        Holder<Biome> biome = player.level().getBiome(player.blockPosition());
        return Component.literal(biome.getRegisteredName());
    }

    public static Component getPlayerLastLogin(ServerPlayer player) {
        if (player.hasData(ModDataAttachments.PLAYER_DATA_HANDLER_ATTACHMENT)) {
            return Component.literal(player.getData(ModDataAttachments.PLAYER_DATA_HANDLER_ATTACHMENT).getLastLogin());
        }

        return Component.literal("Never");
    }

    private static Component getTPS(ServerPlayer player, boolean withColor) {
        long[] times = player.server.getTickTimesNanos();
        TickRateManager tickRateManager = player.server.tickRateManager();

        double tickTime = Stats.meanOf(times) / (double) TimeUtil.NANOSECONDS_PER_MILLISECOND;
        double tps = (double)TimeUtil.MILLISECONDS_PER_SECOND / Math.max(tickTime, tickRateManager.millisecondsPerTick());

        if (withColor) {
            return Component.literal(TIME_FORMATTER.format(tps)).withColor(calculateTPSColor(tickRateManager, tps));
        } else {
            return Component.literal(TIME_FORMATTER.format(tps));
        }
    }

    private static int calculateTPSColor(TickRateManager tickRateManager, double tps) {
        float maxTPS = (float)TimeUtil.MILLISECONDS_PER_SECOND / tickRateManager.millisecondsPerTick();
        return Mth.hsvToRgb((float)(Mth.inverseLerp(tps, 0.0F, maxTPS) * (double)0.33F), 1.0F, 1.0F);
    }

}
