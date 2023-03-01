package com.cortmnzz.lighttag.manager;

import com.cortmnzz.lighttag.tag.EntityNameTag;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.function.Consumer;

public class NameTagManager {
    private static final HashMap<Entity, EntityNameTag> entityNameTagHashMap = new HashMap<>();

    public static EntityNameTag get(Entity entity) {
        return entityNameTagHashMap.computeIfAbsent(entity, EntityNameTag::new);
    }
    public static void remove(Entity entity) {
        entityNameTagHashMap.remove(entity);
    }
    public static void doGlobally(Consumer<Entity> consumer) {
        entityNameTagHashMap.forEach((key, value) -> consumer.accept(key));
    }
    public static void doGlobally(Player player, Consumer<Entity> consumer) {
        entityNameTagHashMap.keySet().stream().filter(target -> target != player).forEach(consumer);
    }
}
