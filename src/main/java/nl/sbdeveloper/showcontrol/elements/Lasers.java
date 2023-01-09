package nl.sbdeveloper.showcontrol.elements;

import fr.skytasul.guardianbeam.Laser;
import nl.sbdeveloper.showcontrol.ShowControlPlugin;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class Lasers {
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
     * @param name The name of the laser
     * @param baseLoc The start location
     *
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
     * @param name The name of the laser
     * @param posLoc The new location
     *
     * @return true if done, false if it doesn't exists
     */
    public static boolean move(String name, Location posLoc) {
        if (!lasers.containsKey(name)) return false;
        LaserRunnable laser = lasers.get(name);

        new BukkitRunnable() {
            boolean fired = false;
            Location oldLoc = laser.posLoc;

            @Override
            public void run() {
                if (oldLoc.getBlockX() != posLoc.getBlockX()) {
                    if (oldLoc.getX() > posLoc.getX()) { //De x gaat omhoog
                        oldLoc = oldLoc.add(0.01, 0, 0);
                    } else {
                        oldLoc = oldLoc.add(-0.01, 0, 0);
                    }
                    fired = true;
                } else {
                    fired = false;
                }

                if (oldLoc.getBlockY() != posLoc.getBlockY()) {
                    if (oldLoc.getY() > posLoc.getY()) { //De y gaat omhoog
                        oldLoc = oldLoc.add(0, 0.01, 0);
                    } else {
                        oldLoc = oldLoc.add(0, -0.01, 0);
                    }
                    fired = true;
                } else {
                    fired = false;
                }

                if (oldLoc.getBlockZ() != posLoc.getBlockZ()) {
                    if (oldLoc.getZ() > posLoc.getZ()) { //De z gaat omhoog
                        oldLoc = oldLoc.add(0, 0, 0.01);
                    } else {
                        oldLoc = oldLoc.add(0, 0, -0.01);
                    }
                    fired = true;
                } else {
                    fired = false;
                }

                if (!fired) {
                    cancel();
                    return;
                }

                laser.changePositionLocation(oldLoc);
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
