package com.wennest.yeemo.vbadge.config;

import com.wennest.yeemo.vbadge.VBadge;
import com.wennest.yeemo.vbadge.api.config.YeemoYML;
import org.bukkit.configuration.InvalidConfigurationException;
import org.jetbrains.annotations.NotNull;

public class ConfigManager {
    @NotNull
    private final VBadge plugin;

    public YeemoYML configMain;
    public YeemoYML configBadge;
    public YeemoYML configPlayer;

    public ConfigManager(@NotNull VBadge plugin) {
        this.plugin = plugin;
    }

    public void setup() throws InvalidConfigurationException{
        try {
            this.configMain = YeemoYML.loadOrExtract(plugin, "config.yml");
            this.configBadge = YeemoYML.loadOrExtract(plugin, "badges.yml");
            this.configPlayer = YeemoYML.loadOrExtract(plugin, "players.yml");
        } catch (InvalidConfigurationException exception) {
            this.plugin.getSLF4JLogger().error("Load config file error!", exception);
            throw exception;
        }
    }
}
