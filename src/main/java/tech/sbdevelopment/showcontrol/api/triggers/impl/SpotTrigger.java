package tech.sbdevelopment.showcontrol.api.triggers.impl;

import tech.sbdevelopment.showcontrol.api.InvalidArgumentException;
import tech.sbdevelopment.showcontrol.api.triggers.Trigger;
import tech.sbdevelopment.showcontrol.api.triggers.TriggerIdentifier;
import tech.sbdevelopment.showcontrol.elements.Spots;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

@TriggerIdentifier(value = "spot", minArgs = 5, argDesc = "<name> <world> <x> <y> <z>")
public class SpotTrigger extends Trigger {
    private final String name;
    private final Location newLocation;

    public SpotTrigger(String[] data) throws InvalidArgumentException {
        super(data);

        this.name = data[0];

        World w = Bukkit.getWorld(data[1]);
        if (w == null) {
            throw new InvalidArgumentException("Provided World in SpotTrigger is null!");
        }

        int x;
        int y;
        int z;
        try {
            x = Integer.parseInt(data[2]);
            y = Integer.parseInt(data[3]);
            z = Integer.parseInt(data[4]);
        } catch (NumberFormatException ex) {
            throw new InvalidArgumentException("Provided position in SpotTrigger is invalid!");
        }

        this.newLocation = new Location(w, x, y, z);

        if (!Spots.exists(name)) {
            Spots.start(name, newLocation);
        }
    }

    @Override
    public void trigger() {
        Spots.move(name, newLocation);
        Bukkit.broadcastMessage("[ShowAPI] De trigger " + name + " is genegeerd, omdat de SPOT is disabled.");
    }

    @Override
    public void remove() {
        Spots.remove(name);
    }
}
