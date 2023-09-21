package tech.sbdevelopment.showcontrol.api.triggers.impl;

import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import tech.sbdevelopment.showcontrol.api.triggers.Trigger;
import tech.sbdevelopment.showcontrol.api.triggers.TriggerIdentifier;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(force = true)
@TriggerIdentifier(value = "animatronic", minArgs = 1, argDesc = "<name>", item = Material.ARMOR_STAND)
public class AnimaTrigger extends Trigger {
    public AnimaTrigger(String[] data) {
        super(data);
        if (Bukkit.getPluginManager().getPlugin("Animatronics") == null) {
            throw new RuntimeException("Animatronics is not installed, can't run an animatronic trigger!");
        }
    }

    @Override
    public void trigger() {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "anima play " + getDataString());
    }

    @Override
    public List<String> getArgumentTabComplete(Player player, int index, String arg) {
        //This trigger supports one argument!
        if (index > 0) {
            return List.of();
        }

        List<String> animaFiles = new ArrayList<>();
        File directory = new File(Bukkit.getPluginManager().getPlugin("Animatronics").getDataFolder(), "animatronics");
        if (directory.isDirectory()) {
            File[] files = directory.listFiles((dir, name) -> name.endsWith(".anima"));
            if (files != null) {
                animaFiles.addAll(
                        Arrays.stream(files)
                                .map(file -> file.getName().replaceFirst("[.][^.]+$", ""))
                                .collect(Collectors.toList())
                );
            }
        }
        return animaFiles;
    }
}
