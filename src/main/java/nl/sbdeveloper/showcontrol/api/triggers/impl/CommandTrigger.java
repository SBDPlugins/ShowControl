package nl.sbdeveloper.showcontrol.api.triggers.impl;

import nl.sbdeveloper.showcontrol.api.triggers.Trigger;
import nl.sbdeveloper.showcontrol.api.triggers.TriggerIdentifier;
import org.bukkit.Bukkit;
import org.bukkit.Material;

@TriggerIdentifier(value = "command", minArgs = 1, argDesc = "<command ...>", item = Material.COMMAND_BLOCK)
public class CommandTrigger extends Trigger {
    public CommandTrigger(String[] data) {
        super(data);
    }

    @Override
    public void trigger() {
        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), getDataString());
    }
}
