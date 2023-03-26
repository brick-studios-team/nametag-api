package com.cortmnzz.lighttag.listeners;

import com.cortmnzz.lighttag.manager.TagPlayerManager;
import com.cortmnzz.lighttag.player.TagPlayer;
import com.cortmnzz.lighttag.tag.EntityNameTag;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        TagPlayer tagPlayer = TagPlayerManager.get(event.getPlayer());

        tagPlayer.setEntityNameTag(new EntityNameTag(tagPlayer.getBukkitPlayer()));

        tagPlayer.getEntityNameTag().addTagLine(target -> "example").addTagLine(target -> "example2").applyAll();
        tagPlayer.applyAll();
    }
}
