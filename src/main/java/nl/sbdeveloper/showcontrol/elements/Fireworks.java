package nl.sbdeveloper.showcontrol.elements;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.meta.FireworkMeta;

public class Fireworks {
    public static void spawn(Firework fw, Location baseLoc) {
        fw.spawn(baseLoc);
    }

    public static class Firework {
        private final FireworkEffect.Builder effectBuilder;
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

        private void spawn(Location loc) {
            org.bukkit.entity.Firework fw = (org.bukkit.entity.Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
            FireworkMeta fwm = fw.getFireworkMeta();
            fwm.addEffect(effectBuilder.build());
            fwm.setPower(power);
            fw.setFireworkMeta(fwm);
        }
    }
}