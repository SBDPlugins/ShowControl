package nl.sbdeveloper.showapi.api.triggers;

import nl.sbdeveloper.showapi.api.TriggerTask;
import nl.sbdeveloper.showapi.api.TriggerType;
import nl.sbdeveloper.showapi.elements.Spots;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class SpotTrigger extends TriggerTask {
    private final String name;
    private Location newLocation;

    public SpotTrigger(String[] data) {
        super(TriggerType.SPOT, data);

        this.name = data[0];

        World w = Bukkit.getWorld(data[1]);
        if (w == null) {
            Bukkit.getLogger().info("De wereld is null!");
            return;
        }

        int x;
        int y;
        int z;
        try {
            x = Integer.parseInt(data[2]);
            y = Integer.parseInt(data[3]);
            z = Integer.parseInt(data[4]);
        } catch (NumberFormatException ex) {
            Bukkit.getLogger().info("De positie is null!");
            return;
        }

        this.newLocation = new Location(w, x, y, z);

        if (!Spots.exists(name)) {
            Spots.start(name, newLocation);
        }
    }

    @Override
    public void trigger() {
//        ShowAPI.Spots.move(name, newLocation);
        Bukkit.broadcastMessage("[ShowAPI] De trigger " + name + " is genegeerd, omdat de SPOT is disabled.");
    }

    @Override
    public void remove() {
        Spots.remove(name);
    }
}
