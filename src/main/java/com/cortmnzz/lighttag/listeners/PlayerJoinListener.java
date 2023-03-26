package com.cortmnzz.lighttag.listeners;

import com.cortmnzz.lighttag.manager.NameTagManager;
import com.cortmnzz.lighttag.manager.TagPlayerManager;
import com.cortmnzz.lighttag.player.TagPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        TagPlayer tagPlayer = TagPlayerManager.get(event.getPlayer());

        NameTagManager.get(tagPlayer.getBukkitPlayer()).addTagLine(target -> "example").addTagLine(target -> "example2").applyAll();
    }
}
