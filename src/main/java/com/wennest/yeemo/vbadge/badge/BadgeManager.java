package com.wennest.yeemo.vbadge.badge;

import com.wennest.yeemo.vbadge.VBadge;
import com.wennest.yeemo.vbadge.api.config.YeemoYML;
import com.wennest.yeemo.vbadge.utils.StringUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class BadgeManager {

    @NotNull
    private final VBadge plugin;
    @Getter
    @NotNull
    private final Map<String, Badge> badges;
    @NotNull
    private final YeemoYML badgeConfig;
    @NotNull
    private final YeemoYML playerConfig;

    public BadgeManager(@NotNull VBadge plugin) {
        this.plugin = plugin;
        this.badges = new HashMap<>();
        this.playerConfig = this.plugin.getConfigManager().configPlayer;
        this.badgeConfig = this.plugin.getConfigManager().configBadge;
    }

    public void setup() {
        ConfigurationSection cs = badgeConfig.getConfigurationSection("badges");

        if (cs == null) {
            this.plugin.getSLF4JLogger().warn("Unable to load badge from configuration file. Because the badge node root was not found.");
            return;
        }

        Set<String> keys = cs.getKeys(false);
        for (String key : keys) {
            String displayName = cs.getString(key + ".displayName");
            String icon = cs.getString(key + ".icon");

            if (displayName == null || icon == null) {
                continue;
            }

            this.addBadge(key, displayName, icon);
        }

        this.plugin.getSLF4JLogger().info("{} badge/s load from config file.", this.badges.size());
    }

    public void shutdown() {
        this.badges.clear();
    }

    @SuppressWarnings("UnusedReturnValue")
    public boolean addBadge(@NotNull String name, @NotNull String displayName, @NotNull String icon) {
        return this.addBadge(name, new Badge(name, displayName, icon));
    }

    public boolean addBadge(@NotNull String name, @NotNull Badge badge) {
        if (this.isExists(name)) {
            return false;
        }
        this.badges.put(name, badge);
        return true;
    }

    public boolean removeBadge(@NotNull String name) {
        if (this.isExists(name)) {
            this.badges.remove(name);
            return true;
        }

        return false;
    }

    public boolean createBadge(@NotNull String name, @NotNull Badge badge) {
        if (this.addBadge(name, badge)) {
            this.badgeConfig.set("badges." + name + ".displayName", badge.displayName());
            this.badgeConfig.set("badges." + name + ".icon", badge.icon());
            this.badgeConfig.save();
            return true;
        }

        return false;
    }

    public boolean deleteBadge(@NotNull String name) {
        if (this.removeBadge(name)) {
            this.badgeConfig.set("badges." + name, null);
            this.badgeConfig.save();
            return true;
        }

        return false;
    }

    public boolean isExists(@NotNull String name) {
        return this.badges.get(name) != null;
    }

    @Nullable
    public List<Badge> getPlayerAvailableBadges(@NotNull Player player) {
        return this.getPlayerAvailableBadges(player.getUniqueId());
    }

    @Nullable
    public List<Badge> getPlayerAvailableBadges(@NotNull String name) {
        UUID uuid = Bukkit.getPlayerUniqueId(name);
        if (uuid == null) {
            return null;
        }

        return this.getPlayerAvailableBadges(uuid);
    }

    @Nullable
    public List<Badge> getPlayerAvailableBadges(@NotNull UUID uuid) {
        ConfigurationSection playerConfigSection = this.playerConfig.getConfigurationSection("players");
        if (playerConfigSection == null) {
            return null;
        }

        return playerConfigSection.getStringList(uuid + ".available").stream()
                .map(this.badges::get)
                .filter(Objects::nonNull)
                .toList();
    }

    @Nullable
    public List<Badge> getPlayerEnabledBadge(@NotNull UUID uuid) {
        ConfigurationSection playerConfigSection = this.playerConfig.getConfigurationSection("players");
        if (playerConfigSection == null) {
            return null;
        }

        return playerConfigSection.getStringList(uuid + ".enable").stream()
                .map(this.badges::get)
                .filter(Objects::nonNull)
                .toList();
    }

    public void grant(UUID uuid, String badge) {
        String node = "players." + uuid + ".available";
        List<String> availableList = this.playerConfig.getStringList(node);
        availableList.add(badge);
        this.playerConfig.set(node, availableList);
        this.playerConfig.save();
    }

    public void revoke(UUID uuid, String badge) {
        String node = "players." + uuid + ".available";
        List<String> availableList = this.playerConfig.getStringList(node);
        availableList.remove(badge);
        this.playerConfig.set(node, availableList);
        this.playerConfig.save();
    }

    @Nullable
    public Badge getBadge(@NotNull String name) {
        if (this.badges.containsKey(name)) {
            return this.badges.get(name);
        }
        return null;
    }
}
