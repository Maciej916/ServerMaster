package com.maciej916.server_master.event;

import com.maciej916.server_master.ServerMaster;
import com.maciej916.server_master.data.ModDataAttachments;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

@EventBusSubscriber(modid = ServerMaster.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class PlayerLoggedOutEventHandler {

    @SubscribeEvent
    public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        Player player = event.getEntity();

        if (player.hasData(ModDataAttachments.PLAYER_DATA_HANDLER_ATTACHMENT)) {
            player.getData(ModDataAttachments.PLAYER_DATA_HANDLER_ATTACHMENT).setLastLogin();
        }
    }
}
