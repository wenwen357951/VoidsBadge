package com.wennest.yeemo.vbadge.command;

import com.wennest.yeemo.vbadge.VBadge;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.apache.commons.lang3.AnnotationUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class VBadgeCommand extends Command implements CommandExecutor {

    @Getter
    @NotNull
    private final VBadge plugin;
    @NotNull
    private final String name;

    public VBadgeCommand(@NotNull VBadge plugin, @NotNull String name) {
        super(name);
        this.plugin = plugin;
        this.name = name;
    }

    @Override
    @NotNull
    public String getLabel() {
        return this.name;
    }

    @Override
    @NotNull
    public String getDescription() {
        return "This is badge manager command.";
    }

    @Override
    @NotNull
    public String getPermission() {
        return "vbadge.command.badge";
    }

    @Override
    @NotNull
    public String getUsage() {
        return "/" + this.name + " <param1> <param2>";
    }

    @Override
    @NotNull
    public List<String> getAliases() {
        return List.of("badge");
    }

    @Override
    @Nullable
    public Component permissionMessage() {
        return MiniMessage.miniMessage().deserialize("<red>You don't have permission to do this!");
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String command, @NotNull String[] args) {
        if (this.getPermission() != null && !sender.hasPermission(this.getPermission())) {
            sender.sendMessage(Objects.requireNonNull(this.permissionMessage()));
            return false;
        }

        return this.onCommand(sender, this, command, args);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

    }


    @Override
    @NotNull
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {

    }
}
