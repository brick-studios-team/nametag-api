package com.cortmnzz.lighttag.tag;

import org.bukkit.entity.Player;

import java.util.function.Function;

public class TagLine {
    private final Function<Player, String> text;

    public TagLine(Function<Player, String> text) {
        this.text = text;
    }
}
