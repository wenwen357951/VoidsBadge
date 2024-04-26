package com.wennest.yeemo.vbadge.command;

import com.wennest.yeemo.vbadge.VBadge;
import com.wennest.yeemo.vbadge.api.command.IGeneralCommand;
import com.wennest.yeemo.vbadge.utils.Reflex;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.*;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class CommandRegister extends Command {

    @Getter
    @NotNull
    private final VBadge plugin;
    @NotNull
    private final CommandExecutor commandExecutor;
    private TabCompleter tabCompleter;

    protected CommandRegister(
            @NotNull String[] aliases,
            @NotNull String description,
            @NotNull String usage,
            @NotNull CommandExecutor commandExecutor,
            @NotNull VBadge plugin) {
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
    public List<String> tabComplete(
            @NotNull CommandSender sender,
            @NotNull String alias,
            @NotNull String[] args) throws IllegalArgumentException {
        if (this.tabCompleter != null) {
            List<String> list = this.tabCompleter.onTabComplete(sender, this, alias, args);
            if (list != null) {
                return list;
            }
        }
        return Collections.emptyList();
    }

    public static void register(
            @NotNull VBadge plugin,
            @NotNull IGeneralCommand command) {
        CommandRegister cmd = new CommandRegister(
                command.labels(),
                command.description(),
                command.usage(),
                command,
                plugin
        );
        cmd.setTabCompleter(command);
        cmd.setPermission(command.getPermission());

        Server server = plugin.getServer();
        CommandMap commandMap = (CommandMap) Reflex.getFieldValue(server, "commandMap");
        if (commandMap == null) {
            return;
        }

        commandMap.register(plugin.getName(), cmd);
        CommandRegister.addPermission(command.getPermission());
        command.getSubCommands().forEach(subCommand -> CommandRegister.addPermission(subCommand.getPermission()));
    }


    public static void unregister(VBadge plugin, String[] aliases) {
        SimpleCommandMap map = (SimpleCommandMap) Reflex.getFieldValue(plugin.getServer().getPluginManager(), "commandMap");
        if (map == null) {
            return;
        }

        @SuppressWarnings("unchecked")
        HashMap<String, Command> knownCommands = (HashMap<String, Command>) Reflex.getFieldValue(map, "knownCommands");
        if (knownCommands == null) {
            return;
        }

        for (String command : aliases) {
            for (String commandAlias : getAliases(command)) {
                Command cmd = map.getCommand(commandAlias);
                if (cmd == null) {
                    continue;
                }
                if (!cmd.unregister(map)) {
                    VBadge.getInstance().getSLF4JLogger().error("Unable to unregister command: {}", commandAlias);
                }
                knownCommands.remove(commandAlias);
                VBadge.getInstance().getSLF4JLogger().info("Command unregistered: '{}'", commandAlias);
            }
        }
    }

    @NotNull
    public static Set<String> getAliases(@NotNull String cmd) {
        SimpleCommandMap map =
                (SimpleCommandMap) Reflex.getFieldValue(Bukkit.getServer().getPluginManager(), "commandMap");
        if (map == null) return Collections.emptySet();

        for (Command command : map.getCommands()) {
            if (command.getLabel().equalsIgnoreCase(cmd) || command.getAliases().contains(cmd)) {
                Set<String> aliases = new HashSet<>(command.getAliases());
                aliases.add(command.getLabel());

                return aliases;
            }
        }

        return Collections.emptySet();
    }

    private static void addPermission(@Nullable String permissionNode) {
        if (permissionNode == null) {
            return;
        }
        Bukkit.getPluginManager().addPermission(new Permission(permissionNode));
    }
}
