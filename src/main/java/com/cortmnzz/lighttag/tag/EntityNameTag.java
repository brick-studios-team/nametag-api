package com.cortmnzz.lighttag.tag;

import com.cortmnzz.lighttag.manager.NameTagManager;
import com.cortmnzz.lighttag.manager.TagPlayerManager;
import com.cortmnzz.lighttag.player.TagPlayer;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.EntityArmorStand;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityLiving;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class EntityNameTag {
    @Getter private final Entity entity;
    @Getter private final List<EntityArmorStand> entityArmorStandList;
    private final List<TagLine> tagLineList;

    public EntityNameTag(Entity entity) {
        this.entity = entity;
        this.entityArmorStandList = new ArrayList<>();
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
            tagPlayer.setEntityNameTag(this);

            this.tagLineList.forEach(line -> {
                EntityArmorStand entityArmorStand = new EntityArmorStand(((CraftWorld) tagPlayer.getBukkitPlayer().getWorld()).getHandle());
                entityArmorStand.setInvisible(true);
                entityArmorStand.setCustomNameVisible(true);
                entityArmorStand.setSmall(true);
                entityArmorStand.setCustomName(line.getText().apply(tagPlayer.getBukkitPlayer()));
                this.entityArmorStandList.add(entityArmorStand);

                entityPlayer.playerConnection.sendPacket(new PacketPlayOutSpawnEntityLiving(entityArmorStand));
            });

            teleport();
        }
    }
    public void teleport() {
        for (int index = 0; index < this.entityArmorStandList.size(); index++) {
            Location location = this.entity.getLocation().add(0, index * 0.5, 0);

            EntityArmorStand entityArmorStand = this.entityArmorStandList.get(index);
            entityArmorStand.setLocation(location.getX(), location.getY(), location.getZ(), 0, 0);
        }
    }
}
