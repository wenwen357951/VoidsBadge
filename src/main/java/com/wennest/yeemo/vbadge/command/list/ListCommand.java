package com.wennest.yeemo.vbadge.command.list;

import com.wennest.yeemo.vbadge.VBadge;
import com.wennest.yeemo.vbadge.api.command.ISubCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ListCommand extends ISubCommand {
    public ListCommand(@NotNull VBadge plugin) {
        super(plugin, new String[]{"list"}, "vbadge.admin.command.list");
    }

    @Override
    public @NotNull String description() {
        return "List all badges.";
    }

    @Override
    public @NotNull String usage() {
        return "";
    }

    @Override
    public boolean playersOnly() {
        return false;
    }

    @Override
    protected void perform(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        sender.sendMessage(MiniMessage.miniMessage().deserialize(
                "<br><gold>========== <aqua>Badge List <gold>=========="
        ));
        this.getPlugin().getBadgeManager().getBadges().forEach((name, badge) -> {
            sender.sendMessage(MiniMessage.miniMessage().deserialize(
                    "<click:suggest_command:" + badge.icon() + "><aqua><name>:</aqua> <icon> <yellow>- <display_name></yellow></click>",
                    Placeholder.component("name", Component.text(name)),
                    Placeholder.component("icon", Component.text(badge.icon())),
                    Placeholder.component("display_name", Component.text(badge.displayName()))
            ));
        });
    }
}
