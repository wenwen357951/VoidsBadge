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

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class RevokeCommand extends ISubCommand {
    public RevokeCommand(@NotNull VBadge plugin) {
        super(plugin, new String[]{"revoke"}, "vbadge.admin.command.revoke");
    }

    @Override
    public @NotNull String description() {
        return "Remove badge to player.";
    }

    @Override
    public @NotNull String usage() {
        return "<player> <badge>";
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
        List<Badge> playerBadges = badgeManager.getPlayerAvailableBadges(player.getUniqueId());
        if (playerBadges == null || !playerBadges.contains(badge)) {
            sender.sendMessage(
                    MiniMessage.miniMessage().deserialize(
                            "<red>This player does not owns this badge."
                    )
            );
            return;
        }

        this.getPlugin().getBadgeManager().revoke(player.getUniqueId(), args[1]);
        sender.sendMessage(
                MiniMessage.miniMessage().deserialize(
                        "<green>Successfully! Remove the badge '<badge>' from the player '<player>'.",
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
            List<Badge> badges = this.getPlugin().getBadgeManager().getPlayerAvailableBadges(player);
            if (badges != null) {
                return badges.stream().map(Badge::name).toList();
            }
        }

        return Collections.emptyList();
    }
}
