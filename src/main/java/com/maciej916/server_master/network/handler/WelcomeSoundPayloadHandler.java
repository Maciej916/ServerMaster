package com.maciej916.server_master.network.handler;

import com.maciej916.server_master.ServerMaster;
import com.maciej916.server_master.network.payload.WelcomeSoundPayload;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class WelcomeSoundPayloadHandler {

    public static void handleDataOnNetwork(final WelcomeSoundPayload data, final IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();

            if (player instanceof LocalPlayer localPlayer) {
//                ResourceLocation resourceLocation = ResourceLocation.parse(data.soundLocation());
//                Optional<Reference<SoundEvent>> soundEventReference = localPlayer.registryAccess().lookupOrThrow(Registries.SOUND_EVENT).get(resourceLocation);

                ResourceLocation resourceLocation = ResourceLocation.parse(data.soundLocation());
                SoundEvent soundEvent = BuiltInRegistries.SOUND_EVENT.get(resourceLocation);

                if (soundEvent != null) {
                    localPlayer.playSound(soundEvent, 1.0f, 1.0f);
                }
            }
        })
        .exceptionally(e -> {
            context.disconnect(Component.translatable(ServerMaster.MOD_ID + ".networking.failed", e.getMessage()));
            return null;
        });
    }
}
