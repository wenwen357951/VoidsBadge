package com.wennest.yeemo.vbadge.api.config;

import com.wennest.yeemo.vbadge.VBadge;
import com.wennest.yeemo.vbadge.utils.FileUtil;
import lombok.Getter;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class YeemoYML extends YamlConfiguration {
    @NotNull
    private final File file;
    @Getter
    private boolean isChanged;

    public YeemoYML(@NotNull final String path, @NotNull final String filename) throws InvalidConfigurationException {
        this(new File(path, filename));
    }

    public YeemoYML(@NotNull final File file) throws InvalidConfigurationException {
        FileUtil.create(file);
        this.file = file;
        this.reload();
        this.isChanged = false;
        VBadge.getInstance().getSLF4JLogger().info("Config file: {} has been loaded!", this.file.getName());
    }

    public void save() {
        try {
            this.save(this.file);
        } catch (IOException exception) {
            VBadge.getInstance().getSLF4JLogger().error(
                    "Could not save config: {}", this.file.getName(), exception);
        }
    }

    public boolean saveChanges() {
        if (this.isChanged()) {
            this.save();
            this.isChanged = false;
            return true;
        }

        return false;
    }

    public boolean reload() throws InvalidConfigurationException {
        try {
            this.load(this.file);
            this.isChanged = false;
            return true;
        } catch (IOException exception) {
            VBadge.getInstance().getSLF4JLogger().error(
                    "Could not reload config: {}", this.file.getName(), exception);
            return false;
        }
    }

    public static YeemoYML loadOrExtract(@NotNull VBadge plugin, @NotNull String filePath) throws InvalidConfigurationException {
        if (!plugin.getDataFolder().exists()) {
            FileUtil.mkdir(plugin.getDataFolder());
        }

        if (!filePath.startsWith("/")) {
            filePath = "/" + filePath;
        }

        File file = new File(plugin.getDataFolder() + filePath);
        if (!file.exists()) {
            FileUtil.create(file);
            try {
                InputStream inputStream = Objects.requireNonNull(plugin.getClass().getResourceAsStream(filePath));
                FileUtil.copy(inputStream, file);
            } catch (Exception exception) {
                VBadge.getInstance().getSLF4JLogger().error("Could not load or extract file: {}", file.getName(), exception);
            }
        }

        return new YeemoYML(file);
    }
}
