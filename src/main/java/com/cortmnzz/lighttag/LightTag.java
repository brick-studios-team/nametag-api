package com.cortmnzz.lighttag;

import com.comphenix.protocol.ProtocolLibrary;
import com.cortmnzz.lighttag.listeners.bukkit.PlayerJoinListener;
import com.cortmnzz.lighttag.listeners.packet.SpawnEntityLivingPacketListener;
import com.cortmnzz.lighttag.listeners.bukkit.PlayerMoveListener;
import com.cortmnzz.lighttag.listeners.bukkit.PlayerQuitListener;
import com.cortmnzz.lighttag.listeners.custom.PlayerHideListener;
import com.cortmnzz.lighttag.listeners.custom.PlayerShowListener;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public final class LightTag extends JavaPlugin {

    @Getter private static LightTag instance;

    @Override
    public void onEnable() {
        instance = this;

        this.getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerQuitListener(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerMoveListener(), this);

        this.getServer().getPluginManager().registerEvents(new PlayerShowListener(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerHideListener(), this);

        //ProtocolLibrary.getProtocolManager().addPacketListener(new MovePacketListener());
        //ProtocolLibrary.getProtocolManager().addPacketListener(new SpawnEntityLivingPacketListener());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
