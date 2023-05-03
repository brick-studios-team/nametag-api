/**
 * This code has been programmed by Ailakks.
 * Please, leave this note and give me credits
 * in any project in which it is used. Have a nice day!
 *
 * @author : Ailakks
 * @mailto : hola@ailakks.com
 * @created : 27/03/2023
 */

package com.cortmnzz.lighttag.tag;

import lombok.Data;
import net.minecraft.server.v1_8_R3.EntityArmorStand;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.List;

@Data
public class TagRender {
    private List<EntityArmorStand> entityArmorStandList;
    private Team team;

    public TagRender() {
        this.entityArmorStandList = new ArrayList<>();
    }
}
