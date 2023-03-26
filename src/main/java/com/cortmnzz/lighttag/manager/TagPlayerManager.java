/**
 * This code has been programmed by Ailakks.
 * Please, leave this note and give me credits
 * in any project in which it is used. Have a nice day!
 *
 * @author : Ailakks
 * @mailto : hola@ailakks.com
 */

package com.cortmnzz.lighttag.manager;

import com.cortmnzz.lighttag.player.TagPlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class TagPlayerManager {
    HashMap<UUID, TagPlayer> tagPlayerMap = new HashMap<>();

    public TagPlayer getPlayer(Player bukkitPlayer) {
        return tagPlayerMap.computeIfAbsent(bukkitPlayer.getUniqueId(), uuid -> new TagPlayer(bukkitPlayer));
    }

    public void removePlayer(Player bukkitPlayer) {
        tagPlayerMap.remove(bukkitPlayer.getUniqueId());
    }

    public List<TagPlayer> getList() {
        return new ArrayList<>(this.tagPlayerMap.values());
    }

    public void doGlobally(Consumer<TagPlayer> consumer) {
        getList().forEach(consumer);
    }
}