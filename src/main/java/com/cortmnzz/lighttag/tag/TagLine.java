package com.cortmnzz.lighttag.tag;

import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.function.Function;

public class TagLine {
    @Getter private final Function<Player, String> text;

    public TagLine(Function<Player, String> text) {
        this.text = text;
    }
}
