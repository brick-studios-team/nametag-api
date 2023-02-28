package com.cortmnzz.lighttag.tag;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class EntityNameTag {
    private final Entity entity;
    private final List<TagViewer> tagViewerList;

    public EntityNameTag(Entity entity) {
        this.entity = entity;
        this.tagViewerList = new ArrayList<>();
    }
    public void addTagLine(Function<Player, String> function) {
        this.tagViewerList.add(new TagViewer(function));
    }
    public void apply() {
        this.tagViewerList.forEach(target -> {

        });
    }
}
