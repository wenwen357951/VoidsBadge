package com.wennest.yeemo.vbadge;

import com.wennest.yeemo.vbadge.command.VBadgeCommand;
import com.wennest.yeemo.vbadge.config.ConfigManager;
import com.wennest.yeemo.vbadge.hook.placeholderapi.VBadgeExpansion;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.Objects;

public class VBadge extends JavaPlugin {

    @Getter
    private static VBadge instance;
    @Getter
    private ConfigManager configManager;


    @Override
    public void onEnable() {
        VBadge.instance = this;
        if (!setup()) {
            this.getServer().getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {

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

        CommandMap commandMap;
        // Commands
        try {
            Field commandMapField = this.getServer().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            commandMap = (CommandMap) commandMapField.get(this.getServer());
            commandMap.register(this.getName(), new VBadgeCommand(this, "vbadge"));

        } catch (IllegalAccessException | NoSuchFieldException exception) {
            this.getSLF4JLogger().error("", exception);
        }


        return true;
    }
}
