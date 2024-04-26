package com.wennest.yeemo.vbadge;

import com.wennest.yeemo.vbadge.badge.BadgeManager;
import com.wennest.yeemo.vbadge.command.CommandManager;
import com.wennest.yeemo.vbadge.config.ConfigManager;
import com.wennest.yeemo.vbadge.hook.placeholderapi.VBadgeExpansion;
import lombok.Getter;
import org.bukkit.command.CommandMap;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;

public class VBadge extends JavaPlugin {

    @Getter
    private static VBadge instance;
    @Getter
    private ConfigManager configManager;
    private CommandManager commandManager;
    @Getter
    private BadgeManager badgeManager;


    @Override
    public void onEnable() {
        VBadge.instance = this;
        if (!setup()) {
            this.getServer().getPluginManager().disablePlugin(this);
        }

        this.commandManager = new CommandManager(this);
        this.commandManager.setup();
        this.badgeManager = new BadgeManager(this);
        this.badgeManager.setup();
    }

    @Override
    public void onDisable() {
        this.commandManager.shutdown();
        this.badgeManager.shutdown();
    }

    private boolean setup() {
        // Configuration
        this.configManager = new ConfigManager(this);
        try {
            this.configManager.setup();
        } catch (InvalidConfigurationException exception) {
            this.getSLF4JLogger().error("Could not load config file. Shutdown plugin.", exception);
            return false;
        }

        // Hooks
        if (!this.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            this.getSLF4JLogger().error("Unable to load plugin 'PlaceholderAPI'.");
            return false;
        }
        new VBadgeExpansion(VBadge.getInstance()).register();
        this.getSLF4JLogger().info("Registration extension function in placeholderAPI.");

        return true;
    }
}
