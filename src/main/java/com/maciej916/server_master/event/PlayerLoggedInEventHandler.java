package com.maciej916.server_master.event;

import com.maciej916.server_master.ServerMaster;
import com.maciej916.server_master.config.ModConfigs;
import com.maciej916.server_master.data.ModDataAttachments;
import com.maciej916.server_master.data.impl.PlayerDataHandlerAttachment;
import com.maciej916.server_master.network.payload.WelcomeSoundPayload;
import com.maciej916.server_master.util.MessageHelper;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitlesAnimationPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

@EventBusSubscriber(modid = ServerMaster.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class PlayerLoggedInEventHandler {

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();

        if (!player.hasData(ModDataAttachments.PLAYER_DATA_HANDLER_ATTACHMENT)) {
            player.setData(ModDataAttachments.PLAYER_DATA_HANDLER_ATTACHMENT, new PlayerDataHandlerAttachment());
        }

        if (player instanceof ServerPlayer serverPlayer) {
            if (ModConfigs.WELCOME_MESSAGE_CONFIG.isEnabled()) {
                serverPlayer.connection.send(new ClientboundSetTitleTextPacket(MessageHelper.formatMessage(serverPlayer, ModConfigs.WELCOME_MESSAGE_CONFIG.getMessage())));
                serverPlayer.connection.send(new ClientboundSetTitlesAnimationPacket(
                        ModConfigs.WELCOME_MESSAGE_CONFIG.getFadeIn(),
                        ModConfigs.WELCOME_MESSAGE_CONFIG.getStay(),
                        ModConfigs.WELCOME_MESSAGE_CONFIG.getFadeOut()
                ));
            }

            if (ModConfigs.WELCOME_MESSAGE_CONFIG.isPlaySound()) {
                serverPlayer.connection.send(new WelcomeSoundPayload(ModConfigs.WELCOME_MESSAGE_CONFIG.getSoundLocation()));
            }

            if (ModConfigs.MOTD_CONFIG.isEnabled()) {
                for (MutableComponent message : ModConfigs.MOTD_CONFIG.getMessages()) {
                    serverPlayer.displayClientMessage(MessageHelper.formatMessage(serverPlayer, message), false);
                }
            }
        }
    }
}
