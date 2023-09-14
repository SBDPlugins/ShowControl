package tech.sbdevelopment.showcontrol.api.triggers.impl;

import tech.sbdevelopment.showcontrol.api.exceptions.InvalidArgumentException;
import tech.sbdevelopment.showcontrol.api.triggers.Trigger;
import tech.sbdevelopment.showcontrol.api.triggers.TriggerIdentifier;
import tech.sbdevelopment.showcontrol.elements.Fireworks;
import tech.sbdevelopment.showcontrol.utils.Color;
import org.bukkit.*;

@TriggerIdentifier(value = "firework", minArgs = 5, argDesc = "<world> <x> <y> <z> <configuration ...>", item = Material.FIREWORK_ROCKET)
public class FireworkTrigger extends Trigger {
    private final Fireworks.Firework fw;
    private final Location spawnLoc;

    public FireworkTrigger(String[] data) throws InvalidArgumentException {
        super(data);

        World w = Bukkit.getWorld(data[0]);
        if (w == null) {
            throw new InvalidArgumentException("Provided World in FireworkTrigger is null!");
        }

        int x;
        int y;
        int z;
        try {
            x = Integer.parseInt(data[1]);
            y = Integer.parseInt(data[2]);
            z = Integer.parseInt(data[3]);
        } catch (NumberFormatException ex) {
            throw new InvalidArgumentException("Provided position in FireworkTrigger is invalid!");
        }

        this.spawnLoc = new Location(w, x, y, z);

        Fireworks.Firework firework = new Fireworks.Firework();
        for (int i = 4; i < data.length; i++) {
            if (data[i].split(":").length != 2) continue;

            String key = data[i].split(":")[0];
            String value = data[i].split(":")[1];
            if (key.equalsIgnoreCase("color")) {
                firework = firework.addColor(tech.sbdevelopment.showcontrol.utils.Color.valueOf(value.toUpperCase()).getBukkitColor());
            } else if (key.equalsIgnoreCase("shape")) {
                firework = firework.setType(FireworkEffect.Type.valueOf(value.toUpperCase()));
            } else if (key.equalsIgnoreCase("fade")) {
                firework = firework.addFade(Color.valueOf(value.toUpperCase()).getBukkitColor());
            } else if (key.equalsIgnoreCase("effect")) {
                if (value.equalsIgnoreCase("trail")) {
                    firework = firework.addTrail();
                } else if (value.equalsIgnoreCase("twinkle")) {
                    firework = firework.addFlicker();
                }
            } else if (key.equalsIgnoreCase("power")) {
                int power;
                try {
                    power = Integer.parseInt(value);
                } catch (NumberFormatException ex) {
                    continue;
                }

                firework = firework.setPower(power);
            }
        }

        this.fw = firework;
    }

    @Override
    public void trigger() {
        Fireworks.spawn(fw, spawnLoc);
    }
}
