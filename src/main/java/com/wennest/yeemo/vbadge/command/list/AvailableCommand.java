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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AvailableCommand extends ISubCommand {
    public AvailableCommand(@NotNull VBadge plugin) {
        super(plugin, new String[]{"available"}, "vbadge.user.command.available");
    }

    @Override
    public @NotNull String description() {
        return "Shows the badges available to the player.";
    }

    @Override
    public @NotNull String usage() {
        return "[player]";
    }

    @Override
    public boolean playersOnly() {
        return false;
    }

    @Override
    protected void perform(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        BadgeManager badgeManager = this.getPlugin().getBadgeManager();
        List<Badge> result;
        String message;
        if (sender instanceof Player player && args.length == 0) {
            result = badgeManager.getPlayerAvailableBadges(player);
            message = "<red>You don't have any badges!";
        } else if (args.length > 0) {
            result = badgeManager.getPlayerAvailableBadges(args[0]);
            message = "<red>Player '<player>' does not have any badges!";
        } else {
            sendUsage(sender);
            return;
        }

        if (result == null || result.isEmpty()) {
            sender.sendMessage(
                    MiniMessage.miniMessage().deserialize(
                            message, Placeholder.component(
                                    "player",
                                    Component.text(args.length > 0 ? args[0] : "")
                            )
                    )
            );
            return;
        }
        sender.sendMessage(
                MiniMessage.miniMessage().deserialize(
                        "<br><gold>========== <aqua>Available <gold>=========="
                ));
        result.forEach((badge) -> {
            sender.sendMessage(MiniMessage.miniMessage().deserialize(
                    "<click:suggest_command:" + badge.icon() + "><aqua><name>:</aqua> <icon> <yellow>- <display_name></yellow></click>",
                    Placeholder.component("name", Component.text(badge.name())),
                    Placeholder.component("icon", Component.text(badge.icon())),
                    Placeholder.component("display_name", Component.text(badge.displayName()))
            ));
        });
    }

    @Override
    public @NotNull List<String> getTab(@NotNull Player player, int i, @NotNull String[] args) {
        if (args.length > 0 && args[0].equalsIgnoreCase("available")) {
            return Bukkit.getServer().getOnlinePlayers().stream().map(p -> player.getName()).toList();
        }

        return Collections.emptyList();
    }
}
