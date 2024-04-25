package com.wennest.yeemo.vbadge.api.command;

import com.wennest.yeemo.vbadge.VBadge;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

@Getter
public abstract class IAbstractCommand {

    @NotNull
    private final VBadge plugin;
    @NotNull
    private final String[] aliases;
    @Nullable
    private final String permission;

    public IAbstractCommand(@NotNull VBadge plugin, @NotNull String[] aliases, @Nullable String permission) {
        this.plugin = plugin;
        this.aliases = aliases;
        this.permission = permission;
    }

    public String[] labels() {
        return this.aliases;
    }

    @NotNull
    public abstract String description();

    @NotNull
    public abstract String usage();

    public abstract boolean playersOnly();

    @NotNull
    public List<String> getTab(@NotNull Player player, int i, @NotNull String[] args) {
        return Collections.emptyList();
    }

    protected abstract void perform(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args);

    public final void execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (this.playersOnly() && !(sender instanceof Player)) {
            sender.sendMessage(MiniMessage.miniMessage().deserialize("<red>This command must be used from game."));
            return;
        }

        if (!this.hasPermission(sender)) {
            sender.sendMessage(MiniMessage.miniMessage().deserialize("<red>You don't have permissions (" + this.permission + ")!"));
            return;
        }

        this.perform(sender, label, args);
    }

    public final boolean hasPermission(@NotNull CommandSender sender) {
        return this.permission == null || sender.hasPermission(this.permission);
    }

    public final void sendUsage(@NotNull CommandSender sender) {
        Component label;
        Component command = Component.text("");

        if (this instanceof IGeneralCommand) {
            label = Component.text(this.aliases[0]);
        } else if (this instanceof ISubCommand) {
            label = Component.text(((ISubCommand) this).parent.getAliases()[0]);
            command = Component.text(this.aliases[0]);
        } else {
            return;
        }

        sender.sendMessage(
                MiniMessage.miniMessage().deserialize(
                        "<red>Usage: /<label> <command> <usage>",
                        Placeholder.component("label", label),
                        Placeholder.component("command", command),
                        Placeholder.component("usage", Component.text(this.usage()))
                )
        );
    }
}
