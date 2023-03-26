/**
 * This code has been programmed by Ailakks.
 * Please, leave this note and give me credits
 * in any project in which it is used. Have a nice day!
 *
 * @author : Ailakks
 * @mailto : hola@ailakks.com
 */

package com.cortmnzz.lighttag.event;

import com.cortmnzz.lighttag.player.TagPlayer;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public final class PlayerShowEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    @Getter private final TagPlayer tagPlayer;
    @Getter private final TagPlayer target;

    public PlayerShowEvent(TagPlayer tagPlayer, TagPlayer target) {
        this.tagPlayer = tagPlayer;
        this.target = target;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}