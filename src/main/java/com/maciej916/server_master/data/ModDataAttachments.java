package com.maciej916.server_master.data;

import com.maciej916.server_master.ServerMaster;
import com.maciej916.server_master.data.impl.LevelDataHandlerAttachment;
import com.maciej916.server_master.data.impl.PlayerDataHandlerAttachment;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class ModDataAttachments {
    private static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, ServerMaster.MOD_ID);

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<LevelDataHandlerAttachment>> LEVEL_DATA_HANDLER_ATTACHMENT = ATTACHMENT_TYPES.register("level_data_handler_attachment", () -> AttachmentType.serializable(LevelDataHandlerAttachment::new).build());
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<PlayerDataHandlerAttachment>> PLAYER_DATA_HANDLER_ATTACHMENT = ATTACHMENT_TYPES.register("player_data_handler_attachment", () -> AttachmentType.serializable(PlayerDataHandlerAttachment::new).build());

    public static void register(IEventBus eventBus) {
        ATTACHMENT_TYPES.register(eventBus);
    }
}
