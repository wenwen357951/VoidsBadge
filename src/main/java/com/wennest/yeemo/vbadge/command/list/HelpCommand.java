package com.wennest.yeemo.vbadge.command.list;

import com.wennest.yeemo.vbadge.VBadge;
import com.wennest.yeemo.vbadge.api.command.ISubCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class HelpCommand extends ISubCommand {

    public HelpCommand(@NotNull VBadge plugin) {
        super(plugin, new String[]{"help"}, "vbadge.user.command.help");
    }

    @Override
    public @NotNull String description() {
        return "This is vbadge help command.";
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
        sender.sendMessage(
                MiniMessage.miniMessage().deserialize(
                        "<br><gold>========== <aqua>Voids Badge <gold>=========="
                ));
        this.parent.getSubCommands().forEach(subCommand -> {
            if (!subCommand.hasPermission(sender)) {
                return;
            }
            sender.sendMessage(
                    MiniMessage.miniMessage().deserialize(
                            "<aqua>/<parent_label> <label> <usage> <yellow>- <description>",
                            Placeholder.component("parent_label", Component.text(parent.labels()[0])),
                            Placeholder.component("label", Component.text(subCommand.labels()[0])),
                            Placeholder.component("usage", Component.text(subCommand.usage())),
                            Placeholder.component("description", Component.text(subCommand.description()))
                    )
            );
        });
    }
}
