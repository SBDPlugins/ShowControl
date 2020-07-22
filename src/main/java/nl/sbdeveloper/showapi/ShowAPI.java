package nl.sbdeveloper.showapi;

import nl.sbdeveloper.showapi.utils.Laser;
import nl.sbdeveloper.showapi.utils.VersionUtil;
import org.bukkit.Location;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.inventivetalent.apihelper.API;
import org.inventivetalent.apihelper.APIManager;

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

    //LASERS
    public static class Lasers {
        private static Map<String, LaserRunnable> lasers = new HashMap<>();

        public static boolean startLaser(String name, Location baseLoc) {
            try {
                lasers.put(name, new LaserRunnable(name, baseLoc));
                lasers.get(name).runTaskTimer(ShowAPIPlugin.getInstance(), 0, 1);
                return true;
            } catch (ReflectiveOperationException e) {
                e.printStackTrace();
            }
            return false;
        }

        public static boolean moveLaser(String name, Location posLoc) {
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

            public void run() {
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
