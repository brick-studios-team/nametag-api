package com.cortmnzz.lighttag.tag;

import com.cortmnzz.lighttag.LightTag;
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
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class EntityNameTag {
    @Getter private final TagPlayer tagPlayer;
    private final List<TagLine> tagLineList;
    @Getter private final List<TagPlayer> viewerList;
    private Team bukkitTeam;

    public EntityNameTag(TagPlayer tagPlayer) {
        this.tagPlayer = tagPlayer;
        this.tagLineList = new ArrayList<>();
        this.viewerList = new ArrayList<>();
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
        this.viewerList.forEach(target -> destroy(target.getBukkitPlayer()));
    }

    public void destroyAll(TagPlayer tagPlayer) {
        TagPlayerManager.doGlobally(tagPlayer, target -> destroy(target.getBukkitPlayer()));
    }

    public void addViewer(TagPlayer tagPlayer) {
        this.viewerList.add(tagPlayer);

        apply(tagPlayer.getBukkitPlayer());
    }
    public void removeViewer(TagPlayer tagPlayer) {
        this.viewerList.remove(tagPlayer);

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
            this.viewerList.add(tagPlayerTarget);

            Scoreboard scoreboard = LightTag.getInstance().getServer().getScoreboardManager().getNewScoreboard();
            this.bukkitTeam = scoreboard.registerNewTeam(tagPlayerTarget.getName());
            this.bukkitTeam.setNameTagVisibility(NameTagVisibility.NEVER);
            this.bukkitTeam.addEntry(this.tagPlayer.getName());
            tagPlayerTarget.getBukkitPlayer().setScoreboard(scoreboard);

            new ArrayList<TagLine>(this.tagLineList) {{
                Collections.reverse(this);
            }}.forEach(line -> {
                EntityArmorStand entityArmorStand = new EntityArmorStand(((CraftWorld) tagPlayerTarget.getBukkitPlayer().getWorld()).getHandle());
                entityArmorStand.setInvisible(true);
                entityArmorStand.setCustomNameVisible(true);
                entityArmorStand.setSmall(true);
                entityArmorStand.setCustomName(line.getText().apply(tagPlayerTarget.getBukkitPlayer()));

                tagPlayerTarget.addTagRender(new TagRender(entityArmorStand));

                entityPlayer.playerConnection.sendPacket(new PacketPlayOutSpawnEntityLiving(entityArmorStand));
            });

            teleport(tagPlayerTarget.getBukkitPlayer());
        }
    }

    public void teleport(Entity target) {
        if (target instanceof Player) {
            TagPlayer tagPlayer = TagPlayerManager.get((Player) target);
            EntityPlayer entityPlayer = ((CraftPlayer) tagPlayer.getBukkitPlayer()).getHandle();

            for (int index = 0; index < this.tagLineList.size(); index++) {
                Location location = this.tagPlayer.getBukkitPlayer().getLocation().add(0, 0.8, 0).add(0, index * 0.3, 0);
                EntityArmorStand entityArmorStand = tagPlayer.getTagRenderList().get(index).getEntityArmorStand();

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
        this.viewerList.stream().map(TagPlayer::getBukkitPlayer).forEach(this::teleport);
    }

    public void destroy(Entity target) {
        if (target instanceof Player) {
            TagPlayer tagPlayer = TagPlayerManager.get((Player) target);
            EntityPlayer entityPlayer = ((CraftPlayer) tagPlayer.getBukkitPlayer()).getHandle();

            if (!this.viewerList.contains(tagPlayer)) {
                return;
            }

            this.viewerList.remove(tagPlayer);
            this.bukkitTeam.unregister();

            tagPlayer.getTagRenderList().forEach(tagRender -> {
                entityPlayer.playerConnection.sendPacket(new PacketPlayOutEntityDestroy(tagRender.getEntityArmorStand().getId()));
            });

            tagPlayer.getTagRenderList().clear();
        }
    }
}
