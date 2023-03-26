package com.cortmnzz.lighttag.tag;

import com.cortmnzz.lighttag.manager.NameTagManager;
import net.minecraft.server.v1_8_R3.EntityArmorStand;
import net.minecraft.server.v1_8_R3.PacketPlayOutAttachEntity;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityLiving;
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
            Player player = (Player) target;

            this.tagLineList.forEach(line -> {
                EntityArmorStand entityArmorStand = new EntityArmorStand(((CraftWorld) player.getWorld()).getHandle(),
                        player.getLocation().getX(),
                        player.getLocation().getY(),
                        player.getLocation().getZ());
                entityArmorStand.setInvisible(true);
                entityArmorStand.setCustomNameVisible(true);
                entityArmorStand.setCustomName(line.getText().apply(player));

                PacketPlayOutSpawnEntityLiving packetPlayOutSpawnEntityLiving = new PacketPlayOutSpawnEntityLiving(entityArmorStand);
                PacketPlayOutAttachEntity packetPlayOutAttachEntity = new PacketPlayOutAttachEntity(0, entityArmorStand, ((CraftPlayer) player).getHandle());

                ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packetPlayOutSpawnEntityLiving);
                ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packetPlayOutAttachEntity);
            });
        }
    }
}
