package com.cortmnzz.lighttag.listeners;

import com.cortmnzz.lighttag.manager.NameTagManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        NameTagManager.get(player).applyAll();
    }
}
