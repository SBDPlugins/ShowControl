package tech.sbdevelopment.showcontrol.utils;

import tech.sbdevelopment.showcontrol.ShowControlPlugin;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class YamlFile {
    private FileConfiguration fileConfiguration;
    private File file;
    private final String name;

    public YamlFile(String name) {
        this.name = name;

        if (!ShowControlPlugin.getInstance().getDataFolder().exists()) {
            if (!ShowControlPlugin.getInstance().getDataFolder().mkdir()) {
                Bukkit.getLogger().severe("[ShowAPI] Couldn't generate the pluginfolder!");
                return;
            }
        }

        this.file = new File(ShowControlPlugin.getInstance().getDataFolder(), name + ".yml");
        if (!this.file.exists()) {
            try {
                if (!this.file.createNewFile()) {
                    Bukkit.getLogger().severe("[ShowAPI] Couldn't generate the " + name + ".yml!");
                    return;
                }
                Bukkit.getLogger().info("[ShowAPI] Generating the " + name + ".yml!");
            } catch (IOException e) {
                Bukkit.getLogger().severe("[ShowAPI] Couldn't generate the " + name + ".yml!");
                return;
            }
        }
        this.fileConfiguration = YamlConfiguration.loadConfiguration(this.file);
    }

    public void loadDefaults() {
        Reader defConfigStream1 = new InputStreamReader(Objects.requireNonNull(ShowControlPlugin.getInstance().getResource(name + ".yml"), "Resource is null"), StandardCharsets.UTF_8);
        YamlConfiguration defConfig1 = YamlConfiguration.loadConfiguration(defConfigStream1);
        getFile().setDefaults(defConfig1);
        getFile().options().copyDefaults(true);
        saveFile();
    }

    public FileConfiguration getFile() {
        return this.fileConfiguration;
    }

    public void saveFile() {
        try {
            this.fileConfiguration.save(this.file);
        } catch (IOException e) {
            Bukkit.getLogger().severe("[ShowAPI] Couldn't save the " + name + ".yml!");
        }
    }

    public void reloadConfig() {
        this.fileConfiguration = YamlConfiguration.loadConfiguration(this.file);
    }
}