package com.wennest.yeemo.vbadge.command.list;

import com.wennest.yeemo.vbadge.VBadge;
import com.wennest.yeemo.vbadge.api.command.ISubCommand;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GrantCommand extends ISubCommand {
    public GrantCommand(@NotNull VBadge plugin, @NotNull String[] aliases, @Nullable String permission) {
        super(plugin, new String[]{"grant"}, "vbadge.admin.command.grant");
    }

    @Override
    public @NotNull String description() {
        return "Add badge to player.";
    }

    @Override
    public @NotNull String usage() {
        return "<player> <badge>";
    }

    @Override
    public boolean playersOnly() {
        return false;
    }

    @Override
    protected void perform(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {

    }
}
