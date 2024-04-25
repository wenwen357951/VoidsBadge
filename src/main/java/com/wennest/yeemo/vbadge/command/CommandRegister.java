package com.wennest.yeemo.vbadge.command;

import com.wennest.yeemo.vbadge.VBadge;
import lombok.Getter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommandRegister extends Command {

    @Getter
    @NotNull
    private final VBadge plugin;
    @NotNull
    private final CommandExecutor commandExecutor;
    private TabCompleter tabCompleter;

    protected CommandRegister(@NotNull String[] aliases, @NotNull String description, @NotNull String usage, @NotNull CommandExecutor commandExecutor, @NotNull VBadge plugin) {
        super(aliases[0], description, usage, Arrays.asList(aliases));
        this.plugin = plugin;
        this.commandExecutor = commandExecutor;
    }

    public void setTabCompleter(@NotNull TabCompleter tabCompleter) {
        this.tabCompleter = tabCompleter;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        return this.commandExecutor.onCommand(sender, this, label, args);
    }

    @Override
    @NotNull
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        if (this.tabCompleter != null) {
            List<String> list = this.tabCompleter.onTabComplete(sender, this, alias, args);
            if (list != null) {
                return list;
            }
        }
        return Collections.emptyList();
    }

    public static void register(@NotNull VBadge badge, @NotNull Command command) {
        CommandRegister cmd = new CommandRegister(

        )
    }
}
