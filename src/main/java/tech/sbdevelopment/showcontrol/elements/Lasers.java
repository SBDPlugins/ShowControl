package tech.sbdevelopment.showcontrol.elements;

import fr.skytasul.guardianbeam.Laser;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;
import tech.sbdevelopment.showcontrol.ShowControlPlugin;

import java.util.HashMap;
import java.util.Map;

public class Lasers {
    @Getter
    private static final Map<String, LaserRunnable> lasers = new HashMap<>();

    /**
     * Check if a laser exists
     *
     * @param name The name of the laser
     * @return true if it exists, false if not
     */
    public static boolean exists(String name) {
        return lasers.containsKey(name);
    }

    /**
     * Spawn a new laser, and start it
     *
     * @param name    The name of the laser
     * @param baseLoc The start location
     * @return true if done, false if an exception
     */
    public static boolean start(String name, Location baseLoc) {
        try {
            lasers.put(name, new LaserRunnable(name, baseLoc));
            lasers.get(name).runTaskTimer(ShowControlPlugin.getInstance(), 0, 1);
            return true;
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Move a laser to a location
     *
     * @param name   The name of the laser
     * @param posLoc The new location
     * @return true if done, false if it doesn't exists
     */
    public static boolean move(String name, Location posLoc) {
        if (!lasers.containsKey(name)) return false;
        LaserRunnable laser = lasers.get(name);

        new BukkitRunnable() {
            private final double speed = 0.1;

            @Override
            public void run() {
                // Calculate the change in x, y, and z for this tick
                double deltaX = (posLoc.getX() - laser.posLoc.getX()) * speed;
                double deltaY = (posLoc.getY() - laser.posLoc.getY()) * speed;
                double deltaZ = (posLoc.getZ() - laser.posLoc.getZ()) * speed;

                // Update the laser's position
                laser.posLoc.add(deltaX, deltaY, deltaZ);
                laser.changePositionLocation(laser.posLoc);

                // Check if the laser has reached the target location
                if (laser.posLoc.distanceSquared(posLoc) < 0.01) {
                    // Laser has reached the target, stop the task
                    this.cancel();
                }
            }
        }.runTaskTimer(ShowControlPlugin.getInstance(), 0L, 1L);
        return true;
    }

    public static void remove(String name) {
        if (!lasers.containsKey(name)) return;

        lasers.get(name).cancel();
        lasers.remove(name);
    }

    private static class LaserRunnable extends BukkitRunnable {
        private final Laser laser;
        private final String name;
        private final Location baseLoc;

        private Location posLoc;

        public LaserRunnable(String name, Location baseLoc) throws ReflectiveOperationException {
            this.name = name;
            this.baseLoc = baseLoc;
            this.laser = new Laser.GuardianLaser(baseLoc, baseLoc.add(0, 5, 0), -1, 50);
            this.laser.start(ShowControlPlugin.getInstance());
            this.posLoc = baseLoc;
        }

        @Override
        public void run() {
            if (posLoc == null) return;

            try {
                laser.moveStart(baseLoc);
                laser.moveEnd(posLoc);
            } catch (ReflectiveOperationException e) {
                e.printStackTrace();
            }
        }

        public void changePositionLocation(Location posLoc) {
            this.posLoc = posLoc;
        }

        public synchronized void cancel() throws IllegalStateException {
            laser.stop();
            lasers.remove(name);
            super.cancel();
        }
    }
}
