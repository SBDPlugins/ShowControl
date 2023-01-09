package nl.sbdeveloper.showcontrol.api.triggers;

import nl.sbdeveloper.showcontrol.api.TriggerTask;
import nl.sbdeveloper.showcontrol.api.TriggerType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;

public class ParticleTrigger extends TriggerTask {
    private Particle type;
    private Location spawnLoc;
    private int count;

    public ParticleTrigger(String[] data) {
        super(TriggerType.PARTICLE, data);

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
            Bukkit.getLogger().info("De positie is incorrect!");
            return;
        }

        this.spawnLoc = new Location(w, x, y, z);

        try {
            this.type = Particle.valueOf(data[4]);
        } catch (IllegalArgumentException ex) {
            Bukkit.getLogger().info("De particle " + data[4] + " bestaat niet!");
            return;
        }

        try {
            this.count = Integer.parseInt(data[5]);
        } catch (NumberFormatException ex) {
            Bukkit.getLogger().info("Het aantal " + data[4] + " is incorrect!");
        }
    }

    @Override
    public void trigger() {
        spawnLoc.getWorld().spawnParticle(type, spawnLoc, count);
    }
}
