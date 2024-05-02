package com.wennest.yeemo.vbadge.hook.placeholderapi;

import com.wennest.yeemo.vbadge.VBadge;
import com.wennest.yeemo.vbadge.badge.Badge;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class VBadgeExpansion extends PlaceholderExpansion {

    @NotNull
    private final VBadge plugin;

    public VBadgeExpansion(@NotNull final VBadge plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getAuthor() {
        //noinspection UnstableApiUsage
        return String.join(", ", this.plugin.getPluginMeta().getAuthors());
    }

    @Override
    public @NotNull String getIdentifier() {
        return "vbadge";
    }

    @Override
    public @NotNull String getVersion() {
        //noinspection UnstableApiUsage
        return this.plugin.getPluginMeta().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
        List<Badge> playerBadge = this.plugin.getBadgeManager().getPlayerEnabledBadge(player.getUniqueId());
        return String.join(" ", playerBadge.stream().map(Badge::icon).toList());

    }
}
