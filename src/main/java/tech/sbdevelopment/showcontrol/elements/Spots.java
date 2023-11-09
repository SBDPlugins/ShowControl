package tech.sbdevelopment.showcontrol.elements;

import fr.skytasul.guardianbeam.Laser;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;
import tech.sbdevelopment.showcontrol.ShowControlPlugin;

import java.util.HashMap;
import java.util.Map;

public class Spots {
    @Getter
    private static final Map<String, SpotRunnable> spots = new HashMap<>();

    /**
     * Check if a spot exists
     *
     * @param name The name of the spot
     * @return true if it exists, false if not
     */
    public static boolean exists(String name) {
        return spots.containsKey(name);
    }

    /**
     * Spawn a new spot, and start it
     *
     * @param name    The name of the spot
     * @param baseLoc The start location
     * @return true if done, false if an exception
     */
    public static boolean start(String name, Location baseLoc) {
        try {
            spots.put(name, new SpotRunnable(name, baseLoc));
            spots.get(name).runTaskTimer(ShowControlPlugin.getInstance(), 0, 1);
            return true;
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Move a spot to a location
     *
     * @param name   The name of the spot
     * @param posLoc The new location
     * @return true if done, false if it doesn't exists
     */
    public static boolean move(String name, Location posLoc, double speed) {
        if (!spots.containsKey(name)) return false;
        SpotRunnable laser = spots.get(name);

        new BukkitRunnable() {
            @Override
            public void run() {
                // Calculate the change in x, y, and z for this tick
                double deltaX = (posLoc.getX() - laser.posLoc.getX()) * speed;
                double deltaY = (posLoc.getY() - laser.posLoc.getY()) * speed;
                double deltaZ = (posLoc.getZ() - laser.posLoc.getZ()) * speed;

                // Update the spot's position
                laser.posLoc.add(deltaX, deltaY, deltaZ);
                laser.changePositionLocation(laser.posLoc);

                // Check if the spot has reached the target location
                double tolerance = 0.05;
                if (Math.abs(deltaX) < tolerance && Math.abs(deltaY) < tolerance && Math.abs(deltaZ) < tolerance) {
                    // Spot movement is very small, stop the task
                    this.cancel();
                }
            }
        }.runTaskTimer(ShowControlPlugin.getInstance(), 0L, 1L);
        return true;
    }

    public static void remove(String name) {
        if (!spots.containsKey(name)) return;

        spots.get(name).cancel();
        spots.remove(name);
    }

    private static class SpotRunnable extends BukkitRunnable {
        private final Laser spot;
        private final String name;

        private Location posLoc;

        public SpotRunnable(String name, Location baseLoc) throws ReflectiveOperationException {
            this.name = name;
            this.spot = new Laser.CrystalLaser(baseLoc.add(0, 1, 0), baseLoc, -1, 50);
            this.spot.start(ShowControlPlugin.getInstance());
            this.posLoc = baseLoc.add(0, 1, 0);
        }

        @Override
        public void run() {
            if (posLoc == null) return;

            try {
                spot.moveEnd(posLoc);
            } catch (ReflectiveOperationException e) {
                e.printStackTrace();
            }
        }

        public void changePositionLocation(Location posLoc) {
            this.posLoc = posLoc;
        }

        public synchronized void cancel() throws IllegalStateException {
            spot.stop();
            spots.remove(name);
            super.cancel();
        }
    }
}
