package nl.sbdeveloper.showapi.api.triggers;

import nl.sbdeveloper.showapi.ShowAPI;
import nl.sbdeveloper.showapi.api.TriggerData;
import nl.sbdeveloper.showapi.api.TriggerType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class SpotTrigger extends TriggerData {
    private String name;
    private Location newLocation;

    public SpotTrigger(String[] data) {
        super(TriggerType.SPOT, data);

        World w = Bukkit.getWorld(data[0]);
        if (w == null) {
            Bukkit.getLogger().info("De wereld is null!");
            return;
        }

        int x;
        int y;
        int z;
        try {
            x = Integer.parseInt(data[1]);
            y = Integer.parseInt(data[2]);
            z = Integer.parseInt(data[3]);
        } catch (NumberFormatException ex) {
            Bukkit.getLogger().info("De positie is null!");
            return;
        }

        this.newLocation = new Location(w, x, y, z);

        this.name = data[4];

        if (!ShowAPI.Lasers.exists(name)) {
            ShowAPI.Lasers.start(name, newLocation);
        }
    }

    @Override
    public void trigger() {
        ShowAPI.Spots.move(name, newLocation);
    }
}
