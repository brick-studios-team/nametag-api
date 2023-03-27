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

import lombok.Getter;
import net.minecraft.server.v1_8_R3.EntityArmorStand;

public class TagRender {
    @Getter private final EntityArmorStand entityArmorStand;

    public TagRender(EntityArmorStand entityArmorStand) {
        this.entityArmorStand = entityArmorStand;
    }
}
