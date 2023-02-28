package com.cortmnzz.lighttag.tag;

import org.bukkit.entity.Player;

import java.util.function.Function;

public class TagViewer {
    private final Function<Player, String> function;

    public TagViewer(Function<Player, String> function) {
        this.function = function;
    }
}
