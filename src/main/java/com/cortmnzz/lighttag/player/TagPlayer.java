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
import com.cortmnzz.lighttag.tag.TagRender;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@Accessors(chain = true)
public class TagPlayer {
    @Getter private final Player bukkitPlayer;

    @Getter private EntityNameTag entityNameTag;
    @Getter private List<TagRender> tagRenderList;

    public TagPlayer(Player bukkitPlayer) {
        this.bukkitPlayer = bukkitPlayer;

        this.tagRenderList = new ArrayList<>();
    }
    public String getName() {
        return this.bukkitPlayer.getName();
    }
    public void applyAll() {
        TagPlayerManager.doGlobally(this, target -> target.getEntityNameTag().apply(this.bukkitPlayer));
    }
    public EntityNameTag setEntityNameTag(EntityNameTag entityNameTag) {
        this.entityNameTag = entityNameTag;
        return this.entityNameTag;
    }
    public void addTagRender(TagRender tagRender) {
        this.tagRenderList.add(tagRender);
    }
    public void removeTagRender(TagRender tagRender) {
        this.tagRenderList.remove(tagRender);
    }
}