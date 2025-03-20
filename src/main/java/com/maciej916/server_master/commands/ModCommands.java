package com.maciej916.server_master.commands;

import com.maciej916.server_master.ServerMaster;
import com.maciej916.server_master.commands.impl.RulesCommand;
import com.maciej916.server_master.commands.impl.SMCommand;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

@EventBusSubscriber(modid = ServerMaster.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class ModCommands {

    @SubscribeEvent
    public static void register(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        Commands.CommandSelection environment = event.getCommandSelection();
        CommandBuildContext context = event.getBuildContext();

        SMCommand.register(dispatcher, context);
        RulesCommand.register(dispatcher, context);
    }
}
