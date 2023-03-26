/**
 * This code has been programmed by Ailakks.
 * Please, leave this note and give me credits
 * in any project in which it is used. Have a nice day!
 *
 * @author : Ailakks
 * @mailto : hola@ailakks.com
 */

package com.cortmnzz.lighttag.player;

import com.cortmnzz.lighttag.manager.TagPlayerManager;
import com.cortmnzz.lighttag.tag.EntityNameTag;
import com.cortmnzz.lighttag.tag.NameTag;
import lombok.Setter;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.bukkit.entity.Player;

@Accessors(chain = true)
public class TagPlayer {
    @Getter private final Player bukkitPlayer;

    @Getter private EntityNameTag entityNameTag;

    public TagPlayer(Player bukkitPlayer) {
        this.bukkitPlayer = bukkitPlayer;
    }
    public void applyAll(TagPlayer tagPlayer) {
        TagPlayerManager.doGlobally(tagPlayer, target -> target.getEntityNameTag().apply(this.bukkitPlayer));
    }
    public EntityNameTag setEntityNameTag(EntityNameTag entityNameTag) {
        this.entityNameTag = entityNameTag;
        return this.entityNameTag;
    }
}