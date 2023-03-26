/**
 * This code has been programmed by Ailakks.
 * Please, leave this note and give me credits
 * in any project in which it is used. Have a nice day!
 *
 * @author : Ailakks
 * @mailto : hola@ailakks.com
 */

package com.cortmnzz.lighttag.player;

import lombok.Setter;
import lombok.Getter;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class TagPlayer {
    @Getter private final Player bukkitPlayer;

    @Getter @Setter private Entity entity;

    public TagPlayer(Player bukkitPlayer) {
        this.bukkitPlayer = bukkitPlayer;
    }
}