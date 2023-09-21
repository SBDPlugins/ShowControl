package tech.sbdevelopment.showcontrol.api.triggers.impl;

import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import tech.sbdevelopment.showcontrol.api.triggers.Trigger;
import tech.sbdevelopment.showcontrol.api.triggers.TriggerIdentifier;

import java.util.List;

@NoArgsConstructor(force = true)
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
    public List<String> getArgumentTabComplete(Player player, int index, String arg) {
        return List.of();
    }
}
