package com.wennest.yeemo.vbadge.command;

import com.wennest.yeemo.vbadge.VBadge;
import com.wennest.yeemo.vbadge.api.command.IGeneralCommand;
import com.wennest.yeemo.vbadge.command.list.AvailableCommand;
import com.wennest.yeemo.vbadge.command.list.HelpCommand;
import com.wennest.yeemo.vbadge.command.list.ListCommand;
import com.wennest.yeemo.vbadge.command.list.MainCommand;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class CommandManager {

    @NotNull
    private final VBadge plugin;
    @Getter
    private Set<IGeneralCommand> commands;
    @Getter
    private MainCommand mainCommand;

    public CommandManager(@NotNull VBadge plugin) {
        this.plugin = plugin;
    }

    public void setup() {
        this.commands = new HashSet<>();
        this.mainCommand = new MainCommand(this.plugin);
        this.mainCommand.setDefaultCommand(new HelpCommand(this.plugin));
        this.mainCommand.addSubCommand(new ListCommand(this.plugin));
        this.mainCommand.addSubCommand(new AvailableCommand(this.plugin));
        this.registerCommand(this.mainCommand);
    }

    public void shutdown() {
        for (IGeneralCommand command : new HashSet<>(this.commands)) {
            this.unregisterCommand(command);
            command.clearSubCommand();
        }
        this.commands.clear();
    }

    public void registerCommand(@NotNull IGeneralCommand command) {
        if (this.commands.add(command)) {
            CommandRegister.register(this.plugin, command);
        }
    }

    public void unregisterCommand(@NotNull IGeneralCommand command) {
        if (this.commands.remove(command)) {
            CommandRegister.unregister(this.plugin, command.labels());
        }
    }
}
