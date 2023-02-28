package com.cortmnzz.lighttag.tag;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.cortmnzz.lighttag.manager.PlayerManager;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class EntityNameTag {
    private final Entity entity;
    private final List<TagLine> tagLineList;

    private PacketContainer packet;
    private WrappedDataWatcher dataWatcher;
    private PacketContainer mountPacket;

    public EntityNameTag(Entity entity) {
        this.entity = entity;
        this.tagLineList = new ArrayList<>();
    }
    public void addTagLine(Function<Player, String> function) {
        this.tagLineList.add(new TagLine(function));
    }
    public void applyAll() {
        PlayerManager.doGlobally(this::apply);
    }
    public void apply(Player player) {
        this.tagLineList.forEach(line -> {
            packet = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.SPAWN_ENTITY_LIVING);
            packet.getIntegers().write(0, 0);
            packet.getIntegers().write(1, (int) EntityType.ARMOR_STAND.getTypeId());
            packet.getIntegers().write(2, (int) (entity.getLocation().getX() * 32D));
            packet.getIntegers().write(3, (int) (entity.getLocation().getY() * 32D));
            packet.getIntegers().write(4, (int) (entity.getLocation().getZ() * 32D));
            packet.getIntegers().write(5, 0);
            packet.getIntegers().write(6, 0);
            packet.getIntegers().write(7, 0);

            dataWatcher = new WrappedDataWatcher();
            dataWatcher.setObject(0, (byte) 0x20);
            dataWatcher.setObject(3, (byte) 1);
            dataWatcher.setObject(4, WrappedDataWatcher.Registry.get(Byte.class), (byte) 1);

            mountPacket = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.MOUNT);
            mountPacket.getIntegers().write(0, 0);
            mountPacket.getIntegerArrays().write(0, new int[] {entity.getEntityId()});

            String text = line.getText().apply(player);

            dataWatcher.setObject(2, text);
            packet.getDataWatcherModifier().write(0, dataWatcher);

            try {
                ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
                ProtocolLibrary.getProtocolManager().sendServerPacket(player, mountPacket);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
    }
}
