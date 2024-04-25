package com.wennest.yeemo.vbadge.api.command;

import com.wennest.yeemo.vbadge.VBadge;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class ISubCommand extends IAbstractCommand {
    protected IGeneralCommand parent;

    public ISubCommand(@NotNull VBadge plugin, @NotNull String[] aliases, @Nullable String permission) {
        super(plugin, aliases, permission);
    }

    @NotNull
    public IGeneralCommand getParent() {
        return this.parent;
    }

    public void setParent(@NotNull IGeneralCommand parent) {
        this.parent = parent;
    }
}
