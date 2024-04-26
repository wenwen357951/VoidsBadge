package com.wennest.yeemo.vbadge.api.command;

import com.wennest.yeemo.vbadge.VBadge;
import com.wennest.yeemo.vbadge.utils.StringUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public abstract class IGeneralCommand extends IAbstractCommand implements CommandExecutor, TabExecutor {

    private final Map<String, ISubCommand> subCommands;
    private ISubCommand defaultCommand;

    public IGeneralCommand(@NotNull VBadge plugin, @NotNull String[] aliases) {
        this(plugin, aliases, null);
    }

    public IGeneralCommand(@NotNull VBadge plugin, @NotNull String[] aliases, @Nullable String permission) {
        super(plugin, aliases, permission);
        this.subCommands = new LinkedHashMap<>();
    }

    public void addSubCommand(@NotNull ISubCommand subCommand) {
        for (String alias : subCommand.getAliases()) {
            this.subCommands.put(alias, subCommand);
        }
        subCommand.setParent(this);
    }

    public void setDefaultCommand(@NotNull ISubCommand subCommand) {
        this.addSubCommand(subCommand);
        this.defaultCommand = subCommand;
    }

    public void clearSubCommand() {
        this.subCommands.clear();
    }

    @NotNull
    public Collection<ISubCommand> getSubCommands() {
        return new LinkedHashSet<>(this.subCommands.values());
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (this.subCommands.isEmpty() || (args.length == 0 && this.defaultCommand == null)) {
            this.execute(sender, label, args);
            return true;
        }

        ISubCommand subCommand = this.defaultCommand;
        if (args.length > 0 && this.subCommands.containsKey(args[0])) {
            subCommand = this.subCommands.get(args[0]);
        }

        if (subCommand == null) {
            return false;
        }
        subCommand.execute(
                sender,
                label,
                args.length > 0
                        ? Arrays.copyOfRange(args, 1, args.length)
                        : args
        );
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(
            @NotNull CommandSender sender,
            @NotNull Command command,
            @NotNull String label,
            @NotNull String[] args) {
        if (!(sender instanceof Player) || args.length == 0) {
            return Collections.emptyList();
        }

        if (this.subCommands.isEmpty()) {
            if (!this.hasPermission(sender)) {
                return Collections.emptyList();
            }
            List<String> list = this.getTab((Player) sender, args.length, args);
            return StringUtil.getByFirstLetters(args[args.length - 1], list);
        }

        if (args.length == 1) {
            List<String> suggest = new ArrayList<>();
            for (ISubCommand subCommand : this.getSubCommands()) {
                if (subCommand.hasPermission(sender)) {
                    suggest.addAll(Arrays.asList(subCommand.getAliases()));
                }
            }
            return StringUtil.getByFirstLetters(args[0], suggest);
        }

        ISubCommand sub = this.subCommands.get(args[0]);
        if (sub == null || !sub.hasPermission(sender)) {
            return Collections.emptyList();
        }

        List<String> list = sub.getTab((Player) sender, args.length - 1, args);
        return StringUtil.getByFirstLetters(args[args.length - 1], list);
    }
}
