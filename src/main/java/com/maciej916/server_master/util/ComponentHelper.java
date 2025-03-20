package com.maciej916.server_master.util;

import com.google.gson.*;
import com.mojang.serialization.DataResult;
import net.minecraft.network.chat.*;
import net.minecraft.network.chat.Component;

public class ComponentHelper {

    public static JsonObject serialize(MutableComponent component) {
        Style style = component.getStyle();
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("text", component.getString());

        if (!style.isEmpty()) {
            if (style.getColor() != null) {
                jsonObject.addProperty("color", style.getColor().toString());
            }

//            if (style.getShadowColor() != null) {
//                jsonObject.addProperty("shadow_color", style.getShadowColor());
//            }

            if (style.isBold()) {
                jsonObject.addProperty("bold", style.isBold());
            }

            if (style.isItalic()) {
                jsonObject.addProperty("italic", style.isItalic());
            }

            if (style.isUnderlined()) {
                jsonObject.addProperty("underlined", style.isUnderlined());
            }

            if (style.isStrikethrough()) {
                jsonObject.addProperty("strikethrough", style.isStrikethrough());
            }

            if (style.isObfuscated()) {
                jsonObject.addProperty("obfuscated", style.isObfuscated());
            }

            if (style.getClickEvent() != null) {
                ClickEvent clickEvent = style.getClickEvent();
                JsonObject clickEventJson = new JsonObject();

                clickEventJson.addProperty("action", clickEvent.getAction().toString());
                clickEventJson.addProperty("value", clickEvent.getValue());
                jsonObject.add("clickEvent", clickEventJson);
            }

            if (style.getHoverEvent() != null) {
                // TODO Make hover events
            }

            if (style.getInsertion() != null && !style.getInsertion().equals("default")) {
                jsonObject.addProperty("insertion", style.getInsertion());
            }

            if (!style.getFont().toString().equals("minecraft:default")) {
                jsonObject.addProperty("font", style.getFont().toString());
            }
        }

        return jsonObject;
    }

    public static MutableComponent deserialize(JsonObject jsonObject) {
        if (!jsonObject.has("text")) {
            throw new IllegalArgumentException("Missing message field: text");
        }

        String text = jsonObject.get("text").getAsString();
        MutableComponent component = Component.literal(text);

        Style style = Style.EMPTY;

        if (jsonObject.has("color")) {
            String color = jsonObject.get("color").getAsString();
            DataResult<TextColor> colorDataResult = TextColor.parseColor(color);
            style = style.withColor(colorDataResult.getOrThrow());
        }

        if (jsonObject.has("bold")) {
            style = style.withBold(jsonObject.get("bold").getAsBoolean());
        }

        if (jsonObject.has("italic")) {
            style = style.withItalic(jsonObject.get("italic").getAsBoolean());
        }

        if (jsonObject.has("underlined")) {
            style = style.withUnderlined(jsonObject.get("underlined").getAsBoolean());
        }

        if (jsonObject.has("strikethrough")) {
            style = style.withStrikethrough(jsonObject.get("strikethrough").getAsBoolean());
        }

        if (jsonObject.has("obfuscated")) {
            style = style.withObfuscated(jsonObject.get("obfuscated").getAsBoolean());
        }

        if (jsonObject.has("clickEvent")) {
            JsonObject clickEventJson = jsonObject.getAsJsonObject("clickEvent");
            String actionName = clickEventJson.get("action").getAsString();
            String value = clickEventJson.get("value").getAsString();

            ClickEvent.Action action = getClickEventAction(actionName);
            if (action != null) {
                style = style.withClickEvent(new ClickEvent(action, value));
            }
        }

        if (jsonObject.has("insertion")) {
            style = style.withInsertion(jsonObject.get("insertion").getAsString());
        }

//        if (jsonObject.has("shadow_color")) {
//            int shadowColor = jsonObject.get("shadow_color").getAsInt();
//            style = style.withShadowColor(shadowColor);
//        }

        component.setStyle(style);

        return component;
    }

    private static ClickEvent.Action getClickEventAction(String name) {
        for (ClickEvent.Action action : ClickEvent.Action.values()) {
            if (action.getSerializedName().equalsIgnoreCase(name)) {
                return action;
            }
        }
        return null;
    }
}