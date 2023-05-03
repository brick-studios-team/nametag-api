package com.cortmnzz.lighttag.tag;

import lombok.Data;
import org.bukkit.scoreboard.Team;

@Data
public class TagViewer {
    private Team bukkitTeam;

    public TagViewer() {
    }
}
