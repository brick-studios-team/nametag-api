package com.cortmnzz.lighttag.tag;

public class TagComponent {
    public static TagLine text(String line) {
        return new TagLine(target -> line);
    }
}
