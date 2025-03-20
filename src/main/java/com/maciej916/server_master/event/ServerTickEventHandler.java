package com.maciej916.server_master.event;

import com.maciej916.server_master.ServerMaster;
import com.maciej916.server_master.data.impl.LevelDataHandlerAttachment;
import com.maciej916.server_master.data.ModDataAttachments;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

@EventBusSubscriber(modid = ServerMaster.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class ServerTickEventHandler {

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Pre event) {
        MinecraftServer server = event.getServer();
        Level level = server.overworld();

        if (level.hasData(ModDataAttachments.LEVEL_DATA_HANDLER_ATTACHMENT)) {
            LevelDataHandlerAttachment dataHandler = level.getData(ModDataAttachments.LEVEL_DATA_HANDLER_ATTACHMENT);
            dataHandler.tick(server, level);
        } else {
            level.setData(ModDataAttachments.LEVEL_DATA_HANDLER_ATTACHMENT, new LevelDataHandlerAttachment());
        }
    }
}
