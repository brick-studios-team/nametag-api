package com.cortmnzz.lighttag.listeners.bukkit;

import com.cortmnzz.lighttag.manager.TagPlayerManager;
import com.cortmnzz.lighttag.player.TagPlayer;
import com.cortmnzz.lighttag.tag.EntityNameTag;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Optional;

public class PlayerMoveListener implements Listener {
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        TagPlayer tagPlayer = TagPlayerManager.get(event.getPlayer());

        Optional.ofNullable(tagPlayer.getEntityNameTag()).ifPresent(EntityNameTag::teleport);
    }
}
