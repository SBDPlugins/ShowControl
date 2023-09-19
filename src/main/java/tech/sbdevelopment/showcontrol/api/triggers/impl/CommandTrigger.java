package tech.sbdevelopment.showcontrol.api.triggers.impl;

import tech.sbdevelopment.showcontrol.api.triggers.Trigger;
import tech.sbdevelopment.showcontrol.api.triggers.TriggerIdentifier;
import org.bukkit.Bukkit;
import org.bukkit.Material;

import java.util.List;

@TriggerIdentifier(value = "command", minArgs = 1, argDesc = "<command ...>", item = Material.COMMAND_BLOCK)
public class CommandTrigger extends Trigger {
    public CommandTrigger(String[] data) {
        super(data);
    }

    @Override
    public void trigger() {
        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), getDataString());
    }

    @Override
    public List<String> getArgumentTabComplete(int index, String arg) {
        return List.of();
    }
}
