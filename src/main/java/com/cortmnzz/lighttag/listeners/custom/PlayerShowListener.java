/**
 * This code has been programmed by Ailakks.
 * Please, leave this note and give me credits
 * in any project in which it is used. Have a nice day!
 *
 * @author : Ailakks
 * @mailto : hola@ailakks.com
 * @created : 26/03/2023
 */

package com.cortmnzz.lighttag.listeners.custom;

import com.cortmnzz.lighttag.event.PlayerShowEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerShowListener implements Listener {
    @EventHandler
    public void onPlayerShow(PlayerShowEvent event) {
        event.getTarget().getEntityNameTag().apply(event.getTagPlayer().getBukkitPlayer());
    }
}
