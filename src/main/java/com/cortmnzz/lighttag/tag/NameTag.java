package com.cortmnzz.lighttag.tag;

import com.cortmnzz.lighttag.player.TagPlayer;

public class NameTag {
    public static EntityNameTag builder(TagPlayer tagPlayer) {
        return new EntityNameTag(tagPlayer);
    }
}
