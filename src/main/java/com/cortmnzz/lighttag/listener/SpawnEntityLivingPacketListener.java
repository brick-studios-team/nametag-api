/**
 * This code has been programmed by Ailakks.
 * Please, leave this note and give me credits
 * in any project in which it is used. Have a nice day!
 *
 * @author : Ailakks
 * @mailto : hola@ailakks.com
 * @created : 26/03/2023
 */

package com.cortmnzz.lighttag.listener;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.cortmnzz.lighttag.LightTag;

public class SpawnEntityLivingPacketListener extends PacketAdapter {
    public SpawnEntityLivingPacketListener() {
        super(LightTag.getInstance(), ListenerPriority.NORMAL, PacketType.Play.Server.SPAWN_ENTITY_LIVING, PacketType.Play.Server.ENTITY_DESTROY);
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {

    }
}
