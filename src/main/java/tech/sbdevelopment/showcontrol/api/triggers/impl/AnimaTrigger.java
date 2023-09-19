package tech.sbdevelopment.showcontrol.api.triggers.impl;

import tech.sbdevelopment.showcontrol.api.triggers.Trigger;
import tech.sbdevelopment.showcontrol.api.triggers.TriggerIdentifier;
import org.bukkit.Bukkit;
import org.bukkit.Material;

import java.util.List;

@TriggerIdentifier(value = "animatronic", minArgs = 1, argDesc = "<name>", item = Material.ARMOR_STAND)
public class AnimaTrigger extends Trigger {
    public AnimaTrigger(String[] data) {
        super(data);
    }

    @Override
    public void trigger() {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "anima play " + getDataString());
    }

    @Override
    public List<String> getArgumentTabComplete(int index, String arg) {
        return List.of(); //TODO Return list of animatronics if possible
    }
}
