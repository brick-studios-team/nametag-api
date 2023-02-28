package com.cortmnzz.lighttag.tag;

import org.bukkit.entity.Entity;

public class NameTag {
    public static EntityNameTag builder(Entity entity) {
        return new EntityNameTag(entity);
    }
}
