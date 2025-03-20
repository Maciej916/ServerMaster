package com.maciej916.server_master.data.impl;

import com.maciej916.server_master.config.ModConfigs;
import com.maciej916.server_master.util.MessageHelper;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.UnknownNullability;

import java.util.List;

public class LevelDataHandlerAttachment implements INBTSerializable<CompoundTag>  {
    private int autoMessageLastMessage = -1;
    private int autoMessageTickLeft;

    public LevelDataHandlerAttachment() {
        autoMessageTickLeft = ModConfigs.AUTO_MESSENGER_CONFIG.getInterval();
    }

    private void shouldSendAutoMessage(MinecraftServer server) {
        if (autoMessageTickLeft == 0) {
            autoMessageTickLeft = ModConfigs.AUTO_MESSENGER_CONFIG.getInterval();

            List<ServerPlayer> players = server.getPlayerList().getPlayers();
            if (players.isEmpty()) return;

            List<MutableComponent> messagesList = ModConfigs.AUTO_MESSENGER_CONFIG.getMessages();
            boolean isRandomMessage = ModConfigs.AUTO_MESSENGER_CONFIG.isRandomMessage();

            MutableComponent message;
            if (isRandomMessage) {
                int newMessageIndex;
                do {
                    newMessageIndex = server.overworld().getRandom().nextInt(messagesList.size());
                } while (newMessageIndex == autoMessageLastMessage);
                autoMessageLastMessage = newMessageIndex;
                message = messagesList.get(newMessageIndex);
            } else {
                autoMessageLastMessage = (autoMessageLastMessage + 1) % messagesList.size();
                message = messagesList.get(autoMessageLastMessage);
            }

            if (message != null) {
                for (ServerPlayer serverPlayer : players) {
                    serverPlayer.displayClientMessage(MessageHelper.formatMessage(serverPlayer, message), false);
                }
            }

        } else {
            autoMessageTickLeft--;
        }
    }

    public void tick(MinecraftServer server, Level level) {
        if (ModConfigs.AUTO_MESSENGER_CONFIG.isEnabled()) {
            if (server.isDedicatedServer() || ModConfigs.AUTO_MESSENGER_CONFIG.isRunInSinglePlayer()) {
                shouldSendAutoMessage(server);
            }
        }
    }

    @Override
    public @UnknownNullability CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();

        tag.putInt("autoMessageTickLeft", autoMessageTickLeft);

        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag compoundTag) {
        autoMessageTickLeft = compoundTag.getInt("autoMessageTickLeft");
    }
}
