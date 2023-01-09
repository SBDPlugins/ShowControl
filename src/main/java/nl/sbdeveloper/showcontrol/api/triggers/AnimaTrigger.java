package nl.sbdeveloper.showcontrol.api.triggers;

import nl.sbdeveloper.showcontrol.api.TriggerTask;
import nl.sbdeveloper.showcontrol.api.TriggerType;
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
}
