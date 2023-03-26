package com.cortmnzz.lighttag.tag;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.cortmnzz.lighttag.manager.NameTagManager;
import com.cortmnzz.lighttag.manager.TagPlayerManager;
import com.cortmnzz.lighttag.packet.WrapperPlayServerMount;
import com.cortmnzz.lighttag.packet.WrapperPlayServerSpawnEntityLiving;
import com.cortmnzz.lighttag.player.TagPlayer;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class EntityNameTag {
    private final Entity entity;
    private final List<TagLine> tagLineList;

    public EntityNameTag(Entity entity) {
        this.entity = entity;
        this.tagLineList = new ArrayList<>();
    }

    public EntityNameTag addTagLine(Function<Player, String> function) {
        this.tagLineList.add(new TagLine(function));
        return this;
    }

    public void applyAll() {
        NameTagManager.doGlobally(this::apply);
    }

    public void apply(Entity target) {
        if (target instanceof Player) {
            TagPlayer tagPlayer = TagPlayerManager.get((Player) target);

            this.tagLineList.forEach(line -> {
                WrapperPlayServerSpawnEntityLiving armorStandEntityPacket = new WrapperPlayServerSpawnEntityLiving(new PacketContainer(PacketType.Play.Server.SPAWN_ENTITY_LIVING));
                armorStandEntityPacket.setEntityID(EntityType.ARMOR_STAND.getTypeId());

                WrapperPlayServerSpawnEntityLiving slimeEntityPacket = new WrapperPlayServerSpawnEntityLiving(new PacketContainer(PacketType.Play.Server.SPAWN_ENTITY_LIVING));
                slimeEntityPacket.setEntityID(EntityType.SLIME.getTypeId());

                WrapperPlayServerMount mountArmorStandPacket = new WrapperPlayServerMount(new PacketContainer(PacketType.Play.Server.MOUNT));
                mountArmorStandPacket.setPassengerIds(new int[] { armorStandEntityPacket.getEntityID(), slimeEntityPacket.getEntityID() });

                WrapperPlayServerMount mountSlimePacket = new WrapperPlayServerMount(new PacketContainer(PacketType.Play.Server.MOUNT));
                mountSlimePacket.setPassengerIds(new int[] { slimeEntityPacket.getEntityID(), tagPlayer.getBukkitPlayer().getEntityId() });

                armorStandEntityPacket.broadcastPacket();
                slimeEntityPacket.broadcastPacket();
                mountArmorStandPacket.broadcastPacket();
                mountSlimePacket.broadcastPacket();
            });
        }
    }
}
