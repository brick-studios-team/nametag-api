package com.cortmnzz.lighttag.listeners;

import com.cortmnzz.lighttag.manager.TagPlayerManager;
import com.cortmnzz.lighttag.player.TagPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveListener implements Listener {
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        TagPlayer tagPlayer = TagPlayerManager.get(event.getPlayer());

        tagPlayer.getEntityNameTag().teleport();
    }
}
