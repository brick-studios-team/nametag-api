package com.cortmnzz.lighttag.manager;

import com.cortmnzz.lighttag.LightTag;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

public class PlayerManager {
    public static void doGlobally(Consumer<Player> consumer) {
        LightTag.getInstance().getServer().getOnlinePlayers().forEach(consumer);
    }
}
