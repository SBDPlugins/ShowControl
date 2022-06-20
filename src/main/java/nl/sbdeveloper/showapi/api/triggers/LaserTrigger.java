package nl.sbdeveloper.showapi.api.triggers;

import nl.sbdeveloper.showapi.api.TriggerTask;
import nl.sbdeveloper.showapi.api.TriggerType;
import nl.sbdeveloper.showapi.elements.Lasers;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class LaserTrigger extends TriggerTask {
    private final String name;
    private Location newLocation;

    //TODO Fix laser for 1.17
    public LaserTrigger(String[] data) {
        super(TriggerType.LASER, data);

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

        if (!Lasers.exists(name)) {
            Lasers.start(name, newLocation);
        }
    }

    @Override
    public void trigger() {
        Bukkit.broadcastMessage("[ShowAPI] De trigger " + name + " is genegeerd, omdat de LASER is disabled.");
//        ShowAPI.Lasers.move(name, newLocation);
    }

    @Override
    public void remove() {
        Lasers.remove(name);
    }
}
