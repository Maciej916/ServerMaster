package com.maciej916.server_master.data.impl;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.UnknownNullability;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PlayerDataHandlerAttachment implements INBTSerializable<CompoundTag>  {
    public String lastLogin = "Never";


    public PlayerDataHandlerAttachment() {

    }


    public String getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        lastLogin = now.format(formatter);
    }



    @Override
    public @UnknownNullability CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();

        tag.putString("lastLogin", lastLogin);

        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag compoundTag) {
        lastLogin = compoundTag.getString("lastLogin");
    }
}
