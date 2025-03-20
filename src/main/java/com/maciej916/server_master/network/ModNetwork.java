package com.maciej916.server_master.network;

import com.maciej916.server_master.ServerMaster;
import com.maciej916.server_master.network.handler.WelcomeSoundPayloadHandler;
import com.maciej916.server_master.network.payload.WelcomeSoundPayload;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = ServerMaster.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ModNetwork {
    private static final String PROTOCOL_VERSION = "1.0";

    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(ServerMaster.MOD_ID).versioned(PROTOCOL_VERSION);

        registrar.playToClient(WelcomeSoundPayload.TYPE, WelcomeSoundPayload.STREAM_CODEC, WelcomeSoundPayloadHandler::handleDataOnNetwork);
    }
}
