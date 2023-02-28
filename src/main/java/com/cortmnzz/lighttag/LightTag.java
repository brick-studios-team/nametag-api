package com.cortmnzz.lighttag;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public final class LightTag extends JavaPlugin {

    @Getter private static LightTag instance;

    @Override
    public void onEnable() {
        instance = this;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
