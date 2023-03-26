/**
 * This code has been programmed by Ailakks.
 * Please, leave this note and give me credits
 * in any project in which it is used. Have a nice day!
 *
 * @author : Ailakks
 * @mailto : hola@ailakks.com
 * @created : 26/03/2023
 */

package com.cortmnzz.lighttag.listeners.bukkit;

import com.cortmnzz.lighttag.manager.TagPlayerManager;
import com.cortmnzz.lighttag.player.TagPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        TagPlayer tagPlayer = TagPlayerManager.get(event.getPlayer());

        tagPlayer.getEntityNameTag().destroyAll(tagPlayer);

        TagPlayerManager.remove(tagPlayer.getBukkitPlayer());
    }
}
