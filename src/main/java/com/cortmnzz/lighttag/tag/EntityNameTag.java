package com.cortmnzz.lighttag.tag;

import com.cortmnzz.lighttag.manager.NameTagManager;
import net.minecraft.server.v1_8_R3.EntityArmorStand;
import net.minecraft.server.v1_8_R3.EntitySlime;
import net.minecraft.server.v1_8_R3.PacketPlayOutAttachEntity;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityLiving;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
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
            Player player = (Player) target;

            this.tagLineList.forEach(line -> {
                Location location = player.getLocation();

                EntitySlime entitySlime = new EntitySlime(((CraftWorld) player.getWorld()).getHandle());
                entitySlime.setInvisible(true);
                entitySlime.setSize(-1);
                entitySlime.mount(((CraftEntity) player).getHandle());
                entitySlime.mount(((CraftEntity) player).getHandle());

                EntityArmorStand entityArmorStand = new EntityArmorStand(((CraftWorld) player.getWorld()).getHandle());

                entityArmorStand.setInvisible(true);
                entityArmorStand.setCustomNameVisible(true);
                entityArmorStand.setSmall(true);
                entityArmorStand.setCustomName(line.getText().apply(player));
                entityArmorStand.setLocation(location.getX(), location.getY(), location.getZ(), 0, 0);
                entityArmorStand.mount(entitySlime);

                //PacketPlayOutAttachEntity packetPlayOutAttachEntity = new PacketPlayOutAttachEntity(0, entityArmorStand, ((CraftPlayer) player).getHandle());
                //((CraftPlayer) player).getHandle().playerConnection.sendPacket(packetPlayOutAttachEntity);

                ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutSpawnEntityLiving(entitySlime));
                ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutSpawnEntityLiving(entityArmorStand));
            });
        }
    }
}
