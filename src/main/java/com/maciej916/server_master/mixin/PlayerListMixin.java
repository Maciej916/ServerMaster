package com.maciej916.server_master.mixin;

import com.maciej916.server_master.config.ModConfigs;
import com.maciej916.server_master.config.impl.JoinLeaveMessagesConfig;
import com.maciej916.server_master.util.MessageHelper;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerList.class)
public class PlayerListMixin {
    private ServerPlayer currentServerPlayer;

    /**
     * This method is injected into the "placeNewPlayer" method of the PlayerList class.
     * It runs at the beginning (HEAD) of the method, allowing us to store the ServerPlayer
     * object into the 'currentServerPlayer' variable when a new player is placed into the server.
     *
     * @param connection The connection of the new player.
     * @param player The new player being added.
     * @param cookie The cookie used to handle the player connection.
     * @param ci Callback info to continue the method execution.
     */
    @Inject(method = "placeNewPlayer", at = @At("HEAD"))
    private void onPlaceNewPlayer(Connection connection, ServerPlayer player, CommonListenerCookie cookie, CallbackInfo ci) {
        this.currentServerPlayer = player;
    }

    /**
     * This method redirects the "broadcastSystemMessage" method call inside "placeNewPlayer".
     * Instead of broadcasting the default join message, we modify it to welcome the player by name.
     * If 'currentServerPlayer' is not null (indicating that the player was successfully placed),
     * it sends a custom message or when custom messages are disabled it sends the original message.
     *
     * @param instance The instance of PlayerList that we are modifying.
     * @param message The original system message being broadcast.
     * @param bypassHiddenChat Whether to bypass the hidden chat setting when sending the message.
     */
    @Redirect(
            method = "placeNewPlayer",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/players/PlayerList;broadcastSystemMessage(Lnet/minecraft/network/chat/Component;Z)V"
            )
    )
    private void modifyJoinMessage(PlayerList instance, Component message, boolean bypassHiddenChat) {
        try {
            if (currentServerPlayer != null) {
                JoinLeaveMessagesConfig config = ModConfigs.JOIN_LEAVE_MESSAGES_CONFIG;
                if (config.isEnabled()) {
                    MutableComponent newMessage = MessageHelper.formatMessage(currentServerPlayer, config.getJoinMessage());
                    instance.broadcastSystemMessage(newMessage, bypassHiddenChat);
                }
            } else {
                instance.broadcastSystemMessage(message, bypassHiddenChat);
            }
        } catch (Exception e) {
            // If an error occurs, fall back to the original logic.
            instance.broadcastSystemMessage(message, bypassHiddenChat);
        }
    }
}
