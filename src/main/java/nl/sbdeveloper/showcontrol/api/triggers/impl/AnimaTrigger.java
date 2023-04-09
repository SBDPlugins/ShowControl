package nl.sbdeveloper.showcontrol.api.triggers.impl;

import nl.sbdeveloper.showcontrol.api.triggers.Trigger;
import nl.sbdeveloper.showcontrol.api.triggers.TriggerIdentifier;
import org.bukkit.Bukkit;
import org.bukkit.Material;

@TriggerIdentifier(value = "animatronic", minArgs = 1, argDesc = "<name>", item = Material.ARMOR_STAND)
public class AnimaTrigger extends Trigger {
    public AnimaTrigger(String[] data) {
        super(data);
    }

    @Override
    public void trigger() {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "anima play " + getDataString());
    }
}