package com.wennest.yeemo.vbadge.command.list;

import com.wennest.yeemo.vbadge.VBadge;
import com.wennest.yeemo.vbadge.api.command.IGeneralCommand;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class MainCommand extends IGeneralCommand {

    public MainCommand(@NotNull VBadge plugin) {
        super(plugin, new String[]{"vbadge", "badge"});
    }

    @Override
    public @NotNull String description() {
        return "The Voids server badge manager system.";
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
    protected void perform(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) { }
}
