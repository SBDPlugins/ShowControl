package nl.sbdeveloper.showapi.api.triggers;

import nl.sbdeveloper.showapi.api.TriggerData;
import nl.sbdeveloper.showapi.api.TriggerType;
import org.bukkit.Bukkit;

public class AnimaTrigger extends TriggerData {
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
