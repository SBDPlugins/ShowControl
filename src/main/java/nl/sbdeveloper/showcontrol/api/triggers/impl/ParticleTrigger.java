package nl.sbdeveloper.showcontrol.api.triggers.impl;

import nl.sbdeveloper.showcontrol.api.InvalidArgumentException;
import nl.sbdeveloper.showcontrol.api.triggers.Trigger;
import nl.sbdeveloper.showcontrol.api.triggers.TriggerIdentifier;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;

@TriggerIdentifier(value = "particle", minArgs = 6, argDesc = "<world> <x> <y> <z> <type> <count>")
public class ParticleTrigger extends Trigger {
    private final Particle type;
    private final Location spawnLoc;
    private final int count;

    public ParticleTrigger(String[] data) throws InvalidArgumentException {
        super(data);

        World w = Bukkit.getWorld(data[0]);
        if (w == null) {
            throw new InvalidArgumentException("Provided World in ParticleTrigger is null!");
        }

        int x;
        int y;
        int z;
        try {
            x = Integer.parseInt(data[1]);
            y = Integer.parseInt(data[2]);
            z = Integer.parseInt(data[3]);
        } catch (NumberFormatException ex) {
            throw new InvalidArgumentException("Provided position in ParticleTrigger is invalid!");
        }

        this.spawnLoc = new Location(w, x, y, z);

        try {
            this.type = Particle.valueOf(data[4]);
        } catch (IllegalArgumentException ex) {
            throw new InvalidArgumentException("Provided particle " + data[4] + " in ParticleTrigger does not exists!");
        }

        try {
            this.count = Integer.parseInt(data[5]);
        } catch (NumberFormatException ex) {
            throw new InvalidArgumentException("Provided count " + data[5] + " in ParticleTrigger is invalid!");
        }
    }

    @Override
    public void trigger() {
        spawnLoc.getWorld().spawnParticle(type, spawnLoc, count);
    }
}
