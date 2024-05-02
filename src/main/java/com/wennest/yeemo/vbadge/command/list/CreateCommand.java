package com.wennest.yeemo.vbadge.command.list;

import com.wennest.yeemo.vbadge.VBadge;
import com.wennest.yeemo.vbadge.api.command.ISubCommand;
import com.wennest.yeemo.vbadge.badge.Badge;
import com.wennest.yeemo.vbadge.badge.BadgeManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class CreateCommand extends ISubCommand {
    public CreateCommand(@NotNull VBadge plugin) {
        super(plugin, new String[]{"create"}, "vbadge.admin.command.create");
    }

    @Override
    public @NotNull String description() {
        return "Create the new badge.";
    }

    @Override
    public @NotNull String usage() {
        return "<name> <icon> <displayName>";
    }

    @Override
    public boolean playersOnly() {
        return false;
    }

    @Override
    protected void perform(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length < 3) {
            this.sendUsage(sender);
            return;
        }
        BadgeManager badgeManager = this.getPlugin().getBadgeManager();
        Badge badge = new Badge(args[0], String.join(" ", Arrays.copyOfRange(args, 2, args.length)), args[1]);

        if (badgeManager.getBadges().containsKey(badge.name())) {
            sender.sendMessage(
                    MiniMessage.miniMessage().deserialize(
                            "<red>This badge name is already exists. Please change another name."
                    ));
            return;
        }


        if (badgeManager.createBadge(badge.name(), badge)) {
            sender.sendMessage(
                    MiniMessage.miniMessage().deserialize(
                            "<green>Successfully! Create the new badge '<badge>'.",
                            Placeholder.component("badge", Component.text(badge.icon())
                            )
                    )
            );
        } else {
            sender.sendMessage(
                    MiniMessage.miniMessage().deserialize(
                            "<red>An unexpected error occurred while creating a new badge."
                    )
            );
        }
    }
}
