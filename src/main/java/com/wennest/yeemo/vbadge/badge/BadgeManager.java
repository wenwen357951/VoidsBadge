package com.wennest.yeemo.vbadge.badge;

import com.wennest.yeemo.vbadge.VBadge;
import com.wennest.yeemo.vbadge.api.config.YeemoYML;
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

    public BadgeManager(@NotNull VBadge plugin) {
        this.plugin = plugin;
        this.badges = new HashMap<>();
    }

    public void setup() {
        YeemoYML badgeConfig = this.plugin.getConfigManager().configBadge;
        ConfigurationSection cs = badgeConfig.getConfigurationSection("badges");

        if (cs == null) {
            this.plugin.getSLF4JLogger().warn("Unable to load badge from configuration file. Because the badge node root was not found.");
            return;
        }

        Set<String> keys = cs.getKeys(false);
        System.out.println(keys);
        for (String key : keys) {
            String displayName = cs.getString(key + ".displayName");
            String icon = cs.getString(key + ".icon");

            System.out.println(displayName);
            System.out.println(icon);

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
        YeemoYML playerConfig = this.plugin.getConfigManager().configPlayer;
        ConfigurationSection playerConfigSection = playerConfig.getConfigurationSection("players");
        if (playerConfigSection == null) {
            return null;
        }

        return playerConfigSection.getStringList(uuid + ".available").stream()
                .map(this.badges::get)
                .filter(Objects::nonNull)
                .toList();
    }
}
