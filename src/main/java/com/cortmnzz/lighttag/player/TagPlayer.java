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
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

@Accessors(chain = true)
public class TagPlayer {
    @Getter private final Player bukkitPlayer;

    @Getter private final Team bukkitTeam;
    @Getter private EntityNameTag entityNameTag;
    @Getter private int weight;

    public TagPlayer(Player bukkitPlayer) {
        this.bukkitPlayer = bukkitPlayer;

        String name = String.join("_", String.format("%03d", 100 - this.weight), this.bukkitPlayer.getName());
        this.bukkitTeam = LightTag.getInstance().getBukkitScoreboard().registerNewTeam(name.substring(0, Math.min(name.length(), 16)));

        this.bukkitTeam.setNameTagVisibility(NameTagVisibility.NEVER);
        this.bukkitTeam.addEntry(this.bukkitPlayer.getName());

        this.bukkitPlayer.setScoreboard(LightTag.getInstance().getBukkitScoreboard());
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