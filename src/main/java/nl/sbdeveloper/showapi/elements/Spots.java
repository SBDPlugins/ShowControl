package nl.sbdeveloper.showapi.elements;

import fr.skytasul.guardianbeam.Laser;
import nl.sbdeveloper.showapi.ShowAPIPlugin;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class Spots {
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
     * @param name The name of the spot
     * @param baseLoc The start location
     *
     * @return true if done, false if an exception
     */
    public static boolean start(String name, Location baseLoc) {
        try {
            spots.put(name, new SpotRunnable(name, baseLoc));
            spots.get(name).runTaskTimer(ShowAPIPlugin.getInstance(), 0, 1);
            return true;
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Move a spot to a location
     * @param name The name of the spot
     * @param posLoc The new location
     *
     * @return true if done, false if it doesn't exists
     */
    public static boolean move(String name, Location posLoc) {
        if (!spots.containsKey(name)) return false;
        SpotRunnable laser = spots.get(name);

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
        }.runTaskTimer(ShowAPIPlugin.getInstance(), 0L, 1L);
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
        private final Location baseLoc;

        private Location posLoc;

        public SpotRunnable(String name, Location baseLoc) throws ReflectiveOperationException {
            this.name = name;
            this.baseLoc = baseLoc;
            this.spot = new Laser.CrystalLaser(baseLoc, baseLoc.add(0, 5, 0), -1, 50);
            this.spot.start(ShowAPIPlugin.getInstance());
        }

        @Override
        public void run() {
            if (posLoc == null) return;

            try {
                spot.moveStart(baseLoc);
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
