package com.maciej916.server_master.mixin;

import com.maciej916.server_master.config.ModConfigs;
import com.maciej916.server_master.config.impl.JoinLeaveMessagesConfig;
import com.maciej916.server_master.util.MessageHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerGamePacketListenerImpl.class)
public class ServerGamePacketListenerImplMixin {

    // Shadow the player field to access it in the mixin
    @Shadow
    private ServerPlayer player;

    /**
     * This method redirects the "broadcastSystemMessage" call inside the "removePlayerFromWorld" method.
     * We modify the player leave message to customize the format.
     *
     * @param playerList The PlayerList instance used to broadcast messages.
     * @param message The original leave message being broadcast.
     * @param bypassHiddenChat Whether to bypass hidden chat settings.
     */
    @Redirect(
            method = "removePlayerFromWorld",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/players/PlayerList;broadcastSystemMessage(Lnet/minecraft/network/chat/Component;Z)V"
            )
    )
    private void modifyLeaveMessage(PlayerList playerList, Component message, boolean bypassHiddenChat) {
        try {
            if (player != null) {
                JoinLeaveMessagesConfig config = ModConfigs.JOIN_LEAVE_MESSAGES_CONFIG;
                if (config.isEnabled()) {
                    MutableComponent newMessage = MessageHelper.formatMessage(player, config.getLeaveMessage());
                    playerList.broadcastSystemMessage(newMessage, bypassHiddenChat);
                }
            } else {
                playerList.broadcastSystemMessage(message, bypassHiddenChat);
            }
        } catch (Exception e) {
            e.printStackTrace(); // Log the exception for debugging
            playerList.broadcastSystemMessage(message, bypassHiddenChat); // Fallback to the original message
        }
    }




}