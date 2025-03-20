package com.maciej916.server_master.event;

import com.maciej916.server_master.ServerMaster;
import com.maciej916.server_master.data.ModDataAttachments;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

@EventBusSubscriber(modid = ServerMaster.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class PlayerEventHandler {

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.Clone event) {
        if (event.isWasDeath() && event.getOriginal().hasData(ModDataAttachments.PLAYER_DATA_HANDLER_ATTACHMENT)) {
            event.getEntity().getData(ModDataAttachments.PLAYER_DATA_HANDLER_ATTACHMENT).lastLogin = event.getOriginal().getData(ModDataAttachments.PLAYER_DATA_HANDLER_ATTACHMENT).lastLogin;
        }
    }
}
