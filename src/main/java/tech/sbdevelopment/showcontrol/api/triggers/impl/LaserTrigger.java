package tech.sbdevelopment.showcontrol.api.triggers.impl;

import tech.sbdevelopment.showcontrol.api.exceptions.InvalidArgumentException;
import tech.sbdevelopment.showcontrol.api.triggers.Trigger;
import tech.sbdevelopment.showcontrol.api.triggers.TriggerIdentifier;
import tech.sbdevelopment.showcontrol.elements.Lasers;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

@TriggerIdentifier(value = "laser", minArgs = 5, argDesc = "<name> <world> <x> <y> <z>")
public class LaserTrigger extends Trigger {
    private final String name;
    private final Location newLocation;

    public LaserTrigger(String[] data) throws InvalidArgumentException {
        super(data);

        this.name = data[0];

        World w = Bukkit.getWorld(data[1]);
        if (w == null) {
            throw new InvalidArgumentException("Provided World in LaserTrigger is null!");
        }

        int x;
        int y;
        int z;
        try {
            x = Integer.parseInt(data[2]);
            y = Integer.parseInt(data[3]);
            z = Integer.parseInt(data[4]);
        } catch (NumberFormatException ex) {
            throw new InvalidArgumentException("Provided position in LaserTrigger is invalid!");
        }

        this.newLocation = new Location(w, x, y, z);

        if (!Lasers.exists(name)) {
            Lasers.start(name, newLocation);
        }
    }

    @Override
    public void trigger() {
        Lasers.move(name, newLocation);
    }

    @Override
    public void remove() {
        Lasers.remove(name);
    }
}
