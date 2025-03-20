package com.maciej916.server_master.network.payload;

import com.maciej916.server_master.ServerMaster;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record WelcomeSoundPayload(String soundLocation) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<WelcomeSoundPayload> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(ServerMaster.MOD_ID, "welcome_sound_payload"));

    public static final StreamCodec<ByteBuf, WelcomeSoundPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            WelcomeSoundPayload::soundLocation,
            WelcomeSoundPayload::new
    );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
