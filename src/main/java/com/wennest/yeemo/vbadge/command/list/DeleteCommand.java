package com.wennest.yeemo.vbadge.command.list;

import com.wennest.yeemo.vbadge.VBadge;
import com.wennest.yeemo.vbadge.api.command.ISubCommand;
import com.wennest.yeemo.vbadge.badge.Badge;
import com.wennest.yeemo.vbadge.badge.BadgeManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class DeleteCommand extends ISubCommand {
    public DeleteCommand(@NotNull VBadge plugin) {
        super(plugin, new String[]{"delete"}, "vbadge.admin.command.delete");
    }

    @Override
    public @NotNull String description() {
        return "Delete the badge.";
    }

    @Override
    public @NotNull String usage() {
        return "<name>";
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
        BadgeManager badgeManager = this.getPlugin().getBadgeManager();
        Badge badge = badgeManager.getBadge(args[0]);

        if (badge == null) {
            sender.sendMessage(
                    MiniMessage.miniMessage().deserialize(
                            "<red>This badge name does not exists."
                    ));
            return;
        }

        if (badgeManager.deleteBadge(badge.name())) {
            sender.sendMessage(
                    MiniMessage.miniMessage().deserialize(
                            "<green>Successfully! Delete the badge '<badge>'.",
                            Placeholder.component("badge", Component.text(badge.icon())
                            )
                    )
            );
        } else {
            sender.sendMessage(
                    MiniMessage.miniMessage().deserialize(
                            "<red>An unexpected error occurred while deleting the badge."
                    )
            );
        }
    }

    @Override
    public @NotNull List<String> getTab(@NotNull Player player, int i, @NotNull String[] args) {
        if (i == 1) {
            return this.getPlugin().getBadgeManager().getBadges().keySet().stream().toList();
        }

        return Collections.emptyList();
    }
}
