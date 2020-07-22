package nl.sbdeveloper.showapi;

import nl.sbdeveloper.showapi.utils.Laser;
import nl.sbdeveloper.showapi.utils.VersionUtil;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.event.Listener;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.inventivetalent.apihelper.API;
import org.inventivetalent.apihelper.APIManager;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class ShowAPI implements API, Listener {
    Logger logger = Logger.getLogger("ShowAPI");

    //This gets called either by #registerAPI above, or by the API manager if another plugin requires this API
    @Override
    public void load() {

    }

    //This gets called either by #initAPI above or #initAPI in one of the requiring plugins
    @Override
    public void init(Plugin plugin) {
        if (VersionUtil.getVersion() < 9 || VersionUtil.getVersion() > 16) {
            logger.severe("This API only works from 1.9 to 1.16.1.");
            disable(plugin);
            return;
        }

        APIManager.registerEvents(this, this);
    }

    //This gets called either by #disableAPI above or #disableAPI in one of the requiring plugins
    @Override
    public void disable(Plugin plugin) {

    }

    public static class Fireworks {
        public static void spawn(@NotNull Firework fw, Location baseLoc) {
            fw.spawn(baseLoc);
        }

        public static class Firework {
            private FireworkEffect.Builder effectBuilder;
            private int power;

            public Firework() {
                effectBuilder = FireworkEffect.builder();
            }

            public Firework addFlicker() {
                effectBuilder.flicker(true);
                return this;
            }

            public Firework addTrail() {
                effectBuilder.trail(true);
                return this;
            }

            public Firework addColor(Color color) {
                effectBuilder.withColor(color);
                return this;
            }

            public Firework addFade(Color color) {
                effectBuilder.withFade(color);
                return this;
            }

            public Firework setType(FireworkEffect.Type type) {
                effectBuilder.with(type);
                return this;
            }

            public Firework setPower(int power) {
                this.power = power;
                return this;
            }

            private void spawn(@NotNull Location loc) {
                org.bukkit.entity.Firework fw = (org.bukkit.entity.Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
                FireworkMeta fwm = fw.getFireworkMeta();
                fwm.addEffect(effectBuilder.build());
                fwm.setPower(power);
                fw.setFireworkMeta(fwm);
            }
        }
    }

    //SPOTS -> End Crystals
    public static class Spots {
        private static Map<String, SpotRunnable> spots = new HashMap<>();

        /**
         * Spawn a new spot, and start it
         *
         * @param name The name of the spot
         * @param baseLoc The start location
         */
        public static void start(String name, Location baseLoc) {
            spots.put(name, new SpotRunnable(name, baseLoc));
            spots.get(name).runTaskTimer(ShowAPIPlugin.getInstance(), 0, 1);
        }

        /**
         * Move a spot to a location
         *
         * @param name The name of the spot
         * @param posLoc The new location
         *
         * @return true if done, false if it doesn't exists
         */
        public static boolean move(String name, Location posLoc) {
            if (!spots.containsKey(name)) return false;

            spots.get(name).changePositionLocation(posLoc);
            return true;
        }

        private static class SpotRunnable extends BukkitRunnable {
            private final EnderCrystal crystal;
            private final String name;

            private Location posLoc;

            public SpotRunnable(String name, Location baseLoc) {
                this.name = name;

                this.crystal = (EnderCrystal) baseLoc.getWorld().spawnEntity(baseLoc, EntityType.ENDER_CRYSTAL);
                this.crystal.setCustomName(name);
                this.crystal.setShowingBottom(false);
                this.crystal.setCustomNameVisible(false);
                this.crystal.setBeamTarget(baseLoc.clone().add(0, 5, 0));
            }

            @Override
            public void run() {
                if (posLoc == null) return;
                this.crystal.setBeamTarget(posLoc);
            }

            public void changePositionLocation(Location posLoc) {
                this.posLoc = posLoc;
            }

            public synchronized void cancel() throws IllegalStateException {
                spots.remove(name);
                super.cancel();
            }
        }
    }

    //LASERS -> Guardian beams
    public static class Lasers {
        private static Map<String, LaserRunnable> lasers = new HashMap<>();

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
                lasers.get(name).runTaskTimer(ShowAPIPlugin.getInstance(), 0, 1);
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

            lasers.get(name).changePositionLocation(posLoc);
            return true;
        }

        private static class LaserRunnable extends BukkitRunnable {
            private final Laser laser;
            private final String name;
            private final Location baseLoc;

            private Location posLoc;

            public LaserRunnable(String name, Location baseLoc) throws ReflectiveOperationException {
                this.name = name;
                this.baseLoc = baseLoc;
                this.laser = new Laser(baseLoc, baseLoc.add(0, 5, 0), -1, 50);
                this.laser.start(ShowAPIPlugin.getInstance());
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
}
