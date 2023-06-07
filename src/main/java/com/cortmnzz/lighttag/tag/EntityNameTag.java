package com.cortmnzz.lighttag.tag;

import com.cortmnzz.lighttag.manager.TagPlayerManager;
import com.cortmnzz.lighttag.player.TagPlayer;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Team;

import java.util.*;
import java.util.function.Function;

public class EntityNameTag {
    @Getter private final TagPlayer tagPlayer;
    private final List<TagLine> tagLineList;
    @Getter private final HashMap<TagPlayer, TagRender> tagRenderMap;

    public EntityNameTag(TagPlayer tagPlayer) {
        this.tagPlayer = tagPlayer;
        this.tagLineList = new ArrayList<>();
        this.tagRenderMap = new HashMap<>();
    }

    public EntityNameTag addTagLine(Function<Player, String> function) {
        this.tagLineList.add(new TagLine(function));
        return this;
    }

    public void applyAll() {
        this.tagPlayer.setEntityNameTag(this);

        TagPlayerManager.doGlobally(this.tagPlayer, target -> apply(target.getBukkitPlayer()));
        applyMeAll();
    }

    public void applyMeAll() {
        TagPlayerManager.doGlobally(this.tagPlayer, target -> target.getEntityNameTag().apply(this.tagPlayer.getBukkitPlayer()));
    }

    public void destroyAll() {
        TagPlayerManager.doGlobally(this.tagPlayer, target -> destroy(target.getBukkitPlayer()));
        TagPlayerManager.getList().stream()
                .filter(target -> !Objects.isNull(target.getEntityNameTag()))
                .map(target -> target.getEntityNameTag().getTagRenderMap())
                .filter(list -> list.containsKey(tagPlayer)).forEach(list -> list.remove(tagPlayer));
    }

    public void apply(Entity target) {
        if (target instanceof Player) {
            TagPlayer tagPlayer = TagPlayerManager.get((Player) target);
            EntityPlayer entityPlayer = ((CraftPlayer) tagPlayer.getBukkitPlayer()).getHandle();

            if (!this.tagPlayer.getBukkitPlayer().canSee(tagPlayer.getBukkitPlayer())) {
                return;
            }

            if (this.tagRenderMap.containsKey(tagPlayer)) {
                destroy(target);
            }

            TagRender tagRender = new TagRender();
            this.tagRenderMap.put(tagPlayer, tagRender);

            String name = String.join("", String.format("%03d", this.tagPlayer.getWeight()), this.tagPlayer.getName());
            Team team = tagPlayer.getBukkitScoreboard().registerNewTeam(name.substring(0, Math.min(name.length(), 16)));
            team.setNameTagVisibility(NameTagVisibility.NEVER);
            team.addEntry(this.tagPlayer.getName());
            team.setPrefix(String.valueOf(this.tagPlayer.getWeight()));

            tagRender.setTeam(team);

            new ArrayList<TagLine>(this.tagLineList) {{
                Collections.reverse(this);
            }}.forEach(line -> {
                EntityArmorStand entityArmorStand = new EntityArmorStand(((CraftWorld) tagPlayer.getBukkitPlayer().getWorld()).getHandle());
                entityArmorStand.setInvisible(true);
                entityArmorStand.setCustomNameVisible(true);
                entityArmorStand.setSmall(true);
                entityArmorStand.setCustomName(line.getText().apply(tagPlayer.getBukkitPlayer()));

                this.tagRenderMap.get(tagPlayer).getEntityArmorStandList().add(entityArmorStand);

                entityPlayer.playerConnection.sendPacket(new PacketPlayOutSpawnEntityLiving(entityArmorStand));
            });

            teleport(tagPlayer.getBukkitPlayer());
        }
    }

    public void teleport(Entity target) {
        if (target instanceof Player) {
            TagPlayer tagPlayer = TagPlayerManager.get((Player) target);
            EntityPlayer entityPlayer = ((CraftPlayer) tagPlayer.getBukkitPlayer()).getHandle();

            for (int index = 0; index < this.tagRenderMap.get(tagPlayer).getEntityArmorStandList().size(); index++) {
                Location location = this.tagPlayer.getBukkitPlayer().getLocation().add(0, 0.8, 0).add(0, index * 0.3, 0);
                EntityArmorStand entityArmorStand = this.tagRenderMap.get(tagPlayer).getEntityArmorStandList().get(index);

                PacketPlayOutEntityTeleport teleportPacket = new PacketPlayOutEntityTeleport(entityArmorStand.getId(),
                        (int) (location.getX() * 32.0),
                        (int) (location.getY() * 32.0),
                        (int) (location.getZ() * 32.0),
                        (byte) (location.getYaw() * 256.0f / 360.0f),
                        (byte) (location.getPitch() * 256.0f / 360.0f),
                        false);

                entityPlayer.playerConnection.sendPacket(teleportPacket);
            }
        }
    }

    public void teleportAll() {
        this.tagRenderMap.keySet().stream().map(TagPlayer::getBukkitPlayer).forEach(this::teleport);
    }

    public void destroy(Entity target) {
        if (target instanceof Player) {
            TagPlayer tagPlayer = TagPlayerManager.get((Player) target);
            EntityPlayer entityPlayer = ((CraftPlayer) tagPlayer.getBukkitPlayer()).getHandle();

            if (!Optional.ofNullable(this.tagRenderMap.get(tagPlayer)).isPresent()) {
                return;
            }

            this.tagRenderMap.get(tagPlayer).getEntityArmorStandList().forEach(armorStand -> {
                entityPlayer.playerConnection.sendPacket(new PacketPlayOutEntityDestroy(armorStand.getId()));
            });

            TagRender tagRender = this.tagRenderMap.get(tagPlayer);
            tagRender.getTeam().unregister();

            this.tagRenderMap.remove(tagPlayer);
        }
    }
}