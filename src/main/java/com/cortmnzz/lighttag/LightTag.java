package com.cortmnzz.lighttag;

import com.cortmnzz.lighttag.listeners.PlayerJoinListener;
import com.cortmnzz.lighttag.listeners.PlayerMoveListener;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public final class LightTag extends JavaPlugin {

    @Getter private static LightTag instance;

    @Override
    public void onEnable() {
        instance = this;

        this.getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerMoveListener(), this);

        //ProtocolLibrary.getProtocolManager().addPacketListener(new MovePacketListener());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
