package com.cortmnzz.lighttag.tag;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class EntityNameTag {
    private final Entity entity;
    private final List<Function<Player, TagLine>> entityNameTagList;

    public EntityNameTag(Entity entity) {
        this.entity = entity;
        this.entityNameTagList = new ArrayList<>();
    }
    public void addTagLine(Function<Player, TagLine> entityNameTag) {
        this.entityNameTagList.add(entityNameTag);
    }
    public void apply() {
        this.entityNameTagList.forEach(target -> {
            
        });
    }
}
