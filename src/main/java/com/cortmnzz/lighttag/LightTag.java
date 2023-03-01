package com.cortmnzz.lighttag;

import com.cortmnzz.lighttag.listeners.PlayerJoinListener;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public final class LightTag extends JavaPlugin {

    @Getter private static LightTag instance;

    @Override
    public void onEnable() {
        instance = this;

        this.getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
