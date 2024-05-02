package com.wennest.yeemo.vbadge.command.list;

import com.wennest.yeemo.vbadge.VBadge;
import com.wennest.yeemo.vbadge.api.command.ISubCommand;
import com.wennest.yeemo.vbadge.badge.Badge;
import com.wennest.yeemo.vbadge.badge.BadgeManager;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class ActivateCommand extends ISubCommand {
    public ActivateCommand(@NotNull VBadge plugin) {
        super(plugin, new String[]{"activate"}, "vbadge.user.command.activate");
    }

    @Override
    public @NotNull String description() {
        return "Enable badge display";
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
        if (args.length < 1) {
            this.sendUsage(sender);
            return;
        }

        if (!(sender instanceof Player player)) {
            return;
        }

        BadgeManager badgeManager = this.getPlugin().getBadgeManager();
        Badge badge = badgeManager.getBadge(args[0]);
        if (badge == null || !badgeManager.isExists(args[0])) {
            sender.sendMessage(
                    MiniMessage.miniMessage().deserialize(
                            "<red>The badge does not exist."
                    )
            );
            return;
        }

        List<Badge> playerBadge = badgeManager.getPlayerAvailableBadges(player.getUniqueId());
        if (!playerBadge.contains(badge)) {
            sender.sendMessage(
                    MiniMessage.miniMessage().deserialize(
                            "<red>You don't have this badge."
                    )
            );
            return;
        }

        List<Badge> playerEnableBadge = badgeManager.getPlayerDisabledBadge(player);
        if (!playerEnableBadge.contains(badge)) {
            sender.sendMessage(
                    MiniMessage.miniMessage().deserialize(
                            "<red>The badge is activated."
                    )
            );
            return;
        }

        badgeManager.activate(player, badge);
        sender.sendMessage(
                MiniMessage.miniMessage().deserialize(
                        "<green>Successfully activated this badge"
                )
        );
    }

    @Override
    public @NotNull List<String> getTab(@NotNull Player player, int index, @NotNull String[] args) {
        if (index == 1) {
            return this.getPlugin().getBadgeManager().getPlayerDisabledBadge(player).stream()
                    .map(Badge::name)
                    .toList();
        }

        return Collections.emptyList();
    }
}
