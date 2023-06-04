/**
 * This code has been programmed by Ailakks.
 * Please, leave this note and give me credits
 * in any project in which it is used. Have a nice day!
 *
 * @author : Ailakks
 * @mailto : hola@ailakks.com
 */

package com.cortmnzz.lighttag.player;

import com.cortmnzz.lighttag.LightTag;
import com.cortmnzz.lighttag.manager.TagPlayerManager;
import com.cortmnzz.lighttag.tag.EntityNameTag;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

@Accessors(chain = true)
public class TagPlayer {
    @Getter private final Player bukkitPlayer;

    @Getter private final Scoreboard bukkitScoreboard;
    @Getter private EntityNameTag entityNameTag;
    @Getter private int weight;

    public TagPlayer(Player bukkitPlayer) {
        this.bukkitPlayer = bukkitPlayer;

        this.bukkitScoreboard = LightTag.getInstance().getServer().getScoreboardManager().getNewScoreboard();
        this.bukkitPlayer.setScoreboard(this.bukkitScoreboard);
    }
    public String getName() {
        return this.bukkitPlayer.getName();
    }
    public EntityNameTag setEntityNameTag(EntityNameTag entityNameTag) {
        this.entityNameTag = entityNameTag;
        return this.entityNameTag;
    }
    public TagPlayer setWeight(int weight) {
        this.weight = weight;
        return this;
    }
}