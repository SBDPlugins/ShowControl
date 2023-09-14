package tech.sbdevelopment.showcontrol.utils;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class YamlFile {
    private final JavaPlugin plugin;
    private final String name;
    private FileConfiguration fileConfiguration;
    private File file;

    public YamlFile(JavaPlugin plugin, String name) {
        this.plugin = plugin;
        this.name = name;

        if (!plugin.getDataFolder().exists()) {
            if (!plugin.getDataFolder().mkdir()) {
                Bukkit.getLogger().severe("[" + this.plugin.getName() + "] Couldn't generate the pluginfolder!");
                return;
            }
        }

        this.file = new File(plugin.getDataFolder(), name + ".yml");
        if (!this.file.exists()) {
            try {
                if (!this.file.createNewFile()) {
                    Bukkit.getLogger().severe("[" + this.plugin.getName() + "] Couldn't generate the " + name + ".yml!");
                    return;
                }
                Bukkit.getLogger().info("[" + this.plugin.getName() + "] Generating the " + name + ".yml!");
            } catch (IOException e) {
                Bukkit.getLogger().severe("[" + this.plugin.getName() + "] Couldn't generate the " + name + ".yml!");
                return;
            }
        }
        this.fileConfiguration = YamlConfiguration.loadConfiguration(this.file);
    }

    public void loadDefaults() {
        Reader defConfigStream1 = new InputStreamReader(Objects.requireNonNull(plugin.getResource(name + ".yml"), "Resource is null"), StandardCharsets.UTF_8);
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
            Bukkit.getLogger().severe("[" + this.plugin.getName() + "] Couldn't save the " + name + ".yml!");
        }
    }

    public void reloadConfig() {
        this.fileConfiguration = YamlConfiguration.loadConfiguration(this.file);
    }
}
