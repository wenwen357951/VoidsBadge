package com.wennest.yeemo.vbadge.command.list;

import com.wennest.yeemo.vbadge.VBadge;
import com.wennest.yeemo.vbadge.api.command.ISubCommand;
import com.wennest.yeemo.vbadge.badge.Badge;
import com.wennest.yeemo.vbadge.badge.BadgeManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class DisableCommand extends ISubCommand {
    public DisableCommand(@NotNull VBadge plugin) {
        super(plugin, new String[]{"disable"}, "vbadge.user.command.disable");
    }

    @Override
    public @NotNull String description() {
        return "Disable player-owned badge";
    }

    @Override
    public @NotNull String usage() {
        return "<badge>";
    }

    @Override
    public boolean playersOnly() {
        return true;
    }

    @Override
    protected void perform(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length < 1) {
            this.sendUsage(sender);
            return;
        }

        if (!(sender instanceof Player player)) {
            return;
        }

        BadgeManager badgeManager = this.getPlugin().getBadgeManager();
        badgeManager.
    }

    @Override
    public @NotNull List<String> getTab(@NotNull Player player, int index, @NotNull String[] args) {
        if (index == 1) {
            return Bukkit.getServer().getOnlinePlayers().stream().map(Player::getName).toList();
        }

        return Collections.emptyList();
    }
}
