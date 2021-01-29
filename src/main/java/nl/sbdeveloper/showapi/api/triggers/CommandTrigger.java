package nl.sbdeveloper.showapi.api.triggers;

import nl.sbdeveloper.showapi.api.TriggerTask;
import nl.sbdeveloper.showapi.api.TriggerType;
import org.bukkit.Bukkit;

public class CommandTrigger extends TriggerTask {
    private final String command;

    public CommandTrigger(String[] data) {
        super(TriggerType.COMMAND, data);

        this.command = getDataString();
    }

    @Override
    public void trigger() {
        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), command);
    }
}
