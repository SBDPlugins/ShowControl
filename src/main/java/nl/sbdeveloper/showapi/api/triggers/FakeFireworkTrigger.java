package nl.sbdeveloper.showapi.api.triggers;

import nl.sbdeveloper.showapi.ShowAPIPlugin;
import nl.sbdeveloper.showapi.api.TriggerTask;
import nl.sbdeveloper.showapi.api.TriggerType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class FakeFireworkTrigger extends TriggerTask {
    private Location spawnLoc;
    private float xVelocity;
    private float yVelocity;
    private float zVelocity;

    public FakeFireworkTrigger(String[] data) {
        super(TriggerType.FAKE_FIREWORK, data);

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
            this.xVelocity = Float.parseFloat(data[4]);
            this.yVelocity = Float.parseFloat(data[5]);
            this.zVelocity = Float.parseFloat(data[6]);
        } catch (NumberFormatException ex) {
            Bukkit.getLogger().info("De velocity is incorrect!");
        }
    }

    @Override
    public void trigger() {
        //TODO Fix this trigger
        ShowAPIPlugin.getParticleAPI().getParticles_1_13().DUST().color(255, 0, 0, 5).packet(true, spawnLoc.getX(), spawnLoc.getY(), spawnLoc.getZ(), xVelocity, yVelocity, zVelocity, 0.01, 40);
    }
}
