package com.cortmnzz.lighttag.tag;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.comphenix.protocol.wrappers.WrappedWatchableObject;
import com.cortmnzz.lighttag.manager.NameTagManager;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

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
    public EntityNameTag addTagLine(Function<Player, String> function) {
        this.tagLineList.add(new TagLine(function));
        return this;
    }
    public void applyAll() {
        NameTagManager.doGlobally(this::apply);
    }

    @SuppressWarnings("deprecation")
    public void apply(Entity target) {
        if (target instanceof Player) {
            Player player = (Player) target;

            this.tagLineList.forEach(line -> {
                packet = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.SPAWN_ENTITY_LIVING);
                ArmorStand armorStand = (ArmorStand) player.getWorld().spawnEntity(player.getLocation(), EntityType.ARMOR_STAND);
                armorStand.setGravity(false);
                armorStand.setSmall(true);
                armorStand.setVisible(false);
                armorStand.setMarker(true);
                WrappedDataWatcher dataWatcher = new WrappedDataWatcher();
                WrappedDataWatcher.WrappedDataWatcherObject slimeSizeIndex = new WrappedDataWatcher.WrappedDataWatcherObject(15, WrappedDataWatcher.Registry.get(Byte.class));
                dataWatcher.setObject(slimeSizeIndex, (byte) 2);
                WrappedWatchableObject slimeSize = new WrappedWatchableObject(slimeSizeIndex, (byte) 2);
                packet.getIntegers().write(0, armorStand.getEntityId());
                packet.getEntityUseActions().write(0, EnumWrappers.EntityUseAction.INTERACT_AT);
                packet.getHands().write(0, EnumWrappers.Hand.MAIN_HAND);
                packet.getModifier().write(1, armorStand.getLocation().toVector());
                packet.getBooleans().write(0, true);
                packet.getEntityUseActions().write(0, EnumWrappers.EntityUseAction.INTERACT);
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
}
