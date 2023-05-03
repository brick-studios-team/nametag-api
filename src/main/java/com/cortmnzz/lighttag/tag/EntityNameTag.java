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
    @Getter private final HashMap<TagPlayer, TagViewer> viewerMap;
    @Getter private final List<TagRender> tagRenderList;

    public EntityNameTag(TagPlayer tagPlayer) {
        this.tagPlayer = tagPlayer;
        this.tagLineList = new ArrayList<>();
        this.viewerMap = new HashMap<>();
        this.tagRenderList = new ArrayList<>();
    }

    public EntityNameTag addTagLine(Function<Player, String> function) {
        this.tagLineList.add(new TagLine(function));
        return this;
    }

    public void applyAll(TagPlayer tagPlayer) {
        TagPlayerManager.doGlobally(tagPlayer, target -> apply(target.getBukkitPlayer()));
    }

    public void apply(List<TagPlayer> tagPlayerList) {
        tagPlayerList.forEach(target -> apply(target.getBukkitPlayer()));
    }

    public void destroyAll(List<TagPlayer> tagPlayerList) {
        this.viewerMap.keySet().forEach(target -> destroy(target.getBukkitPlayer()));
    }

    public void destroyAll(TagPlayer tagPlayer) {
        TagPlayerManager.doGlobally(tagPlayer, target -> destroy(target.getBukkitPlayer()));
    }

    public void addViewer(TagPlayer tagPlayer) {
        this.viewerMap.put(tagPlayer, new TagViewer());

        apply(tagPlayer.getBukkitPlayer());
    }
    public void removeViewer(TagPlayer tagPlayer) {
        this.viewerMap.remove(tagPlayer);

        destroy(tagPlayer.getBukkitPlayer());
    }

    public void apply(Entity target) {
        if (target instanceof Player) {
            TagPlayer tagPlayerTarget = TagPlayerManager.get((Player) target);
            EntityPlayer entityPlayer = ((CraftPlayer) tagPlayerTarget.getBukkitPlayer()).getHandle();

            if (!tagPlayer.getBukkitPlayer().canSee(tagPlayerTarget.getBukkitPlayer())) {
                return;
            }

            this.tagPlayer.setEntityNameTag(this);

            Team team = tagPlayer.getBukkitScoreboard().registerNewTeam(tagPlayerTarget.getName());
            team.setNameTagVisibility(NameTagVisibility.NEVER);
            team.addEntry(this.tagPlayer.getName());

            this.viewerMap.get(tagPlayerTarget).setBukkitTeam(team);

            new ArrayList<TagLine>(this.tagLineList) {{
                Collections.reverse(this);
            }}.forEach(line -> {
                EntityArmorStand entityArmorStand = new EntityArmorStand(((CraftWorld) tagPlayerTarget.getBukkitPlayer().getWorld()).getHandle());
                entityArmorStand.setInvisible(true);
                entityArmorStand.setCustomNameVisible(true);
                entityArmorStand.setSmall(true);
                entityArmorStand.setCustomName(line.getText().apply(tagPlayerTarget.getBukkitPlayer()));

                this.tagRenderList.add(new TagRender(entityArmorStand, team));

                entityPlayer.playerConnection.sendPacket(new PacketPlayOutSpawnEntityLiving(entityArmorStand));
            });

            teleport(tagPlayerTarget.getBukkitPlayer());
        }
    }

    public void teleport(Entity target) {
        if (target instanceof Player) {
            TagPlayer tagPlayer = TagPlayerManager.get((Player) target);
            EntityPlayer entityPlayer = ((CraftPlayer) tagPlayer.getBukkitPlayer()).getHandle();

            for (int index = 0; index < this.tagRenderList.size(); index++) {
                Location location = this.tagPlayer.getBukkitPlayer().getLocation().add(0, 0.8, 0).add(0, index * 0.3, 0);
                EntityArmorStand entityArmorStand = this.tagRenderList.get(index).getEntityArmorStand();

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
        this.viewerMap.keySet().stream().map(TagPlayer::getBukkitPlayer).forEach(this::teleport);
    }

    public void destroy(Entity target) {
        if (target instanceof Player) {
            TagPlayer tagPlayer = TagPlayerManager.get((Player) target);
            EntityPlayer entityPlayer = ((CraftPlayer) tagPlayer.getBukkitPlayer()).getHandle();

            if (!Optional.ofNullable(this.viewerMap.get(tagPlayer)).isPresent()) {
                return;
            }

            this.tagRenderList.forEach(tagRender -> {
                entityPlayer.playerConnection.sendPacket(new PacketPlayOutEntityDestroy(tagRender.getEntityArmorStand().getId()));
            });

            this.viewerMap.get(tagPlayer).getBukkitTeam().unregister();
            this.viewerMap.remove(tagPlayer);

            tagPlayer.getEntityNameTag().getTagRenderList().clear();
        }
    }
}
