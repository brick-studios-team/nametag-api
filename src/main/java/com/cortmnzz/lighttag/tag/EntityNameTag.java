package com.cortmnzz.lighttag.tag;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.cortmnzz.lighttag.manager.NameTagManager;
import com.cortmnzz.lighttag.manager.TagPlayerManager;
import com.cortmnzz.lighttag.packet.WrapperPlayServerAttachEntity;
import com.cortmnzz.lighttag.player.TagPlayer;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
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
            EntityPlayer entityPlayer = ((CraftPlayer) tagPlayer.getBukkitPlayer()).getHandle();

            this.tagLineList.forEach(line -> {
                EntityArmorStand entityArmorStand = new EntityArmorStand(((CraftWorld) tagPlayer.getBukkitPlayer().getWorld()).getHandle());
                entityArmorStand.setInvisible(true);
                entityArmorStand.setCustomNameVisible(true);
                entityArmorStand.setSmall(true);
                entityArmorStand.setCustomName(line.getText().apply(tagPlayer.getBukkitPlayer()));

                EntitySlime entitySlime = new EntitySlime(((CraftWorld) tagPlayer.getBukkitPlayer().getWorld()).getHandle());
                entitySlime.setInvisible(true);
                entitySlime.setSize(-1);

                entityPlayer.playerConnection.sendPacket(new PacketPlayOutSpawnEntityLiving(entityArmorStand));
                entityPlayer.playerConnection.sendPacket(new PacketPlayOutSpawnEntityLiving(entitySlime));
                entityPlayer.playerConnection.sendPacket(new PacketPlayOutAttachEntity(0, entityArmorStand, entitySlime));
                entityPlayer.playerConnection.sendPacket(new PacketPlayOutAttachEntity(0, entitySlime, entityPlayer));

                WrapperPlayServerAttachEntity attachEntityArmorStandPacket = new WrapperPlayServerAttachEntity(new PacketContainer(PacketType.Play.Server.ATTACH_ENTITY));
                attachEntityArmorStandPacket.setEntityID(entityArmorStand.getId());
                attachEntityArmorStandPacket.setVehicleId(tagPlayer.getBukkitPlayer().getEntityId());

                /*WrapperPlayServerAttachEntity attachEntitySlimePacket = new WrapperPlayServerAttachEntity(new PacketContainer(PacketType.Play.Server.ATTACH_ENTITY));
                attachEntitySlimePacket.setEntityID(slimeEntityPacket.getEntityID());
                attachEntitySlimePacket.setVehicleId(tagPlayer.getBukkitPlayer().getEntityId());*/

                //armorStandEntityPacket.broadcastPacket();
                //slimeEntityPacket.broadcastPacket();
                /*attachEntityArmorStandPacket.broadcastPacket();
                attachEntitySlimePacket.broadcastPacket();*/
            });
        }
    }
}
