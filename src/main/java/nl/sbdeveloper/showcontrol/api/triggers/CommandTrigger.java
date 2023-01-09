package nl.sbdeveloper.showcontrol.api.triggers;

import nl.sbdeveloper.showcontrol.api.TriggerTask;
import nl.sbdeveloper.showcontrol.api.TriggerType;
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
