package nl.sbdeveloper.showapi.api.triggers;

import nl.sbdeveloper.showapi.api.TriggerTask;
import nl.sbdeveloper.showapi.api.TriggerType;
import org.bukkit.Bukkit;

public class AnimaTrigger extends TriggerTask {
    private final String name;

    public AnimaTrigger(String[] data) {
        super(TriggerType.ANIMA, data);

        this.name = getDataString();
    }

    @Override
    public void trigger() {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "anima play " + name);
    }

    @Override
    public void remove() {
        //TODO Remove the anima?
    }
}
