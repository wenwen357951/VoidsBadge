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

public class EnableCommand extends ISubCommand {
    public EnableCommand(@NotNull VBadge plugin) {
        super(plugin, new String[]{"enable"}, "vbadge.user.command.enable");
    }

    @Override
    public @NotNull String description() {
        return "Enable player-owned badge";
    }

    @Override
    public @NotNull String usage() {
        return "<badge>";
    }

    @Override
    public boolean playersOnly() {
        return false;
    }

    @Override
    protected void perform(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length < 2) {
            this.sendUsage(sender);
            return;
        }

        OfflinePlayer player = Bukkit.getOfflinePlayer(args[0]);
        if (!player.hasPlayedBefore()) {
            sender.sendMessage(
                    MiniMessage.miniMessage().deserialize(
                            "<red>This player has not played this server before."
                    )
            );
            return;
        }

        BadgeManager badgeManager = this.getPlugin().getBadgeManager();
        if (!badgeManager.getBadges().containsKey(args[1])) {
            sender.sendMessage(
                    MiniMessage.miniMessage().deserialize(
                            "<red>The badge does not exist."
                    )
            );
            return;
        }

        Badge badge = badgeManager.getBadges().get(args[1]);
        List<Badge> badges = badgeManager.getPlayerAvailableBadges(player.getUniqueId());
        if (badges != null && badges.contains(badge)) {
            sender.sendMessage(
                    MiniMessage.miniMessage().deserialize(
                            "<red>This player already owns this badge."
                    )
            );
            return;
        }

        this.getPlugin().getBadgeManager().grant(player.getUniqueId(), args[1]);
        sender.sendMessage(
                MiniMessage.miniMessage().deserialize(
                        "<green>Successfully! Add the badge '<badge>' to the player '<player>'.",
                        Placeholder.component("badge", Component.text(badge.icon())),
                        Placeholder.component("player", Component.text(Objects.requireNonNull(player.getName()))
                        )
                )
        );
    }

    @Override
    public @NotNull List<String> getTab(@NotNull Player player, int index, @NotNull String[] args) {
        if (index == 1) {
            return Bukkit.getServer().getOnlinePlayers().stream().map(Player::getName).toList();
        }

        if (index == 2) {
            BadgeManager badgeManager = this.getPlugin().getBadgeManager();
            List<Badge> playerBadge = badgeManager.getPlayerAvailableBadges(player);
            List<String> ownBadge;
            if (playerBadge != null) {
                ownBadge = playerBadge.stream().map(Badge::name).toList();
            } else {
                ownBadge = new ArrayList<>();
            }

            return badgeManager.getBadges().keySet().stream()
                    .filter(badge -> !ownBadge.contains(badge))
                    .toList();
        }

        return Collections.emptyList();
    }
}
