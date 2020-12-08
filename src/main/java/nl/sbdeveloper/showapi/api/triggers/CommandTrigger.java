package nl.sbdeveloper.showapi.api.triggers;

import nl.sbdeveloper.showapi.api.TriggerData;
import nl.sbdeveloper.showapi.api.TriggerType;
import org.bukkit.Bukkit;

public class CommandTrigger extends TriggerData {
    private final String command;

    public CommandTrigger(String[] data) {
        super(TriggerType.COMMAND, data);

        this.command = getDataString();
    }

    @Override
    public void trigger() {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
    }

    @Override
    public void remove() {} //A command is one time, ignore.
}
