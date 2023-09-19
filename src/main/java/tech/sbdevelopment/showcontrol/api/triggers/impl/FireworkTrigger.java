package tech.sbdevelopment.showcontrol.api.triggers.impl;

import org.bukkit.*;
import tech.sbdevelopment.showcontrol.api.exceptions.InvalidArgumentException;
import tech.sbdevelopment.showcontrol.api.triggers.Trigger;
import tech.sbdevelopment.showcontrol.api.triggers.TriggerIdentifier;
import tech.sbdevelopment.showcontrol.elements.Fireworks;
import tech.sbdevelopment.showcontrol.utils.Color;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

                //Limit value to 0-127
                if (power < 0) power = 0;
                if (power > 127) power = 127;

                firework = firework.setPower(power);
            }
        }

        this.fw = firework;
    }

    @Override
    public void trigger() {
        Fireworks.spawn(fw, spawnLoc);
    }

    @Override
    public List<String> getArgumentTabComplete(int index, String arg) {
        if (index == 0) {
            return Bukkit.getWorlds().stream().map(World::getName).toList();
        } else if (index == 4) {
            if (arg.contains(":")) {
                String[] split = arg.split(":");
                if (split.length != 2) return List.of();

                String key = split[0];

                if (key.equalsIgnoreCase("color")) {
                    return Arrays.stream(Color.values()).map(c -> "color:" + c.name()).toList();
                } else if (key.equalsIgnoreCase("shape")) {
                    return Arrays.stream(FireworkEffect.Type.values()).map(t -> "shape:" + t.name()).toList();
                } else if (key.equalsIgnoreCase("fade")) {
                    return Arrays.stream(Color.values()).map(c -> "fade:" + c.name()).toList();
                } else if (key.equalsIgnoreCase("effect")) {
                    return List.of("effect:trail", "effect:twinkle");
                } else if (key.equalsIgnoreCase("power")) {
                    return IntStream.rangeClosed(0, 127)
                            .mapToObj(i -> "power:" + i)
                            .collect(Collectors.toList());
                }
            } else {
                return List.of("color:", "shape:", "fade:", "effect:", "power:");
            }
        }
        return List.of();
    }
}
