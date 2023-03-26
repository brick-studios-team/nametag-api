package com.cortmnzz.lighttag.tag;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.cortmnzz.lighttag.manager.NameTagManager;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
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
            Player player = (Player) target;

            this.tagLineList.forEach(line -> {
                Location location = player.getLocation();

                PacketContainer slimePacket = new PacketContainer(PacketType.Play.Server.SPAWN_ENTITY_LIVING);
                slimePacket.getIntegers().write(0, (int) EntityType.SLIME.getTypeId());
                slimePacket.getIntegers().write(1, (int) (location.getX() * 32));
                slimePacket.getIntegers().write(2, (int) (location.getY() * 32));
                slimePacket.getIntegers().write(3, (int) (location.getZ() * 32));
                slimePacket.getIntegers().write(4, 0);
                ProtocolLibrary.getProtocolManager().broadcastServerPacket(slimePacket);

                int slimeId = slimePacket.getIntegers().read(0);

                PacketContainer armorStandPacket = new PacketContainer(PacketType.Play.Server.SPAWN_ENTITY_LIVING);
                armorStandPacket.getIntegers().write(0, (int) EntityType.ARMOR_STAND.getTypeId());
                armorStandPacket.getIntegers().write(1, (int) (location.getX() * 32));
                armorStandPacket.getIntegers().write(2, (int) (location.getY() * 32));
                armorStandPacket.getIntegers().write(3, (int) (location.getZ() * 32));
                armorStandPacket.getIntegers().write(4, 0);
                ProtocolLibrary.getProtocolManager().broadcastServerPacket(armorStandPacket);

                int armorStandId = armorStandPacket.getIntegers().read(0);

                PacketContainer playerAttachPacket = new PacketContainer(PacketType.Play.Server.ATTACH_ENTITY);
                playerAttachPacket.getIntegers().write(0, player.getEntityId());
                playerAttachPacket.getIntegers().write(1, slimeId);
                playerAttachPacket.getIntegers().write(2, 0);
                ProtocolLibrary.getProtocolManager().broadcastServerPacket(playerAttachPacket);

                PacketContainer slimeAttachPacket = new PacketContainer(PacketType.Play.Server.ATTACH_ENTITY);
                slimeAttachPacket.getIntegers().write(0, slimeId);
                slimeAttachPacket.getIntegers().write(1, armorStandId);
                slimeAttachPacket.getIntegers().write(2, 0);
                ProtocolLibrary.getProtocolManager().broadcastServerPacket(slimeAttachPacket);
            });
        }
    }
}
