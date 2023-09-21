package tech.sbdevelopment.showcontrol.api.triggers.impl;

import lombok.NoArgsConstructor;
import org.bukkit.*;
import org.bukkit.entity.Player;
import tech.sbdevelopment.showcontrol.api.exceptions.InvalidArgumentException;
import tech.sbdevelopment.showcontrol.api.triggers.Trigger;
import tech.sbdevelopment.showcontrol.api.triggers.TriggerIdentifier;
import tech.sbdevelopment.showcontrol.elements.Fireworks;
import tech.sbdevelopment.showcontrol.utils.Color;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@NoArgsConstructor(force = true)
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
    public List<String> getArgumentTabComplete(Player player, int index, String arg) {
        if (index == 0) {
            return player != null ? List.of(player.getWorld().getName()) : Bukkit.getWorlds().stream().map(World::getName).collect(Collectors.toList());
        } else if (index == 1) {
            return player != null ? List.of(String.valueOf(player.getLocation().getBlockX())) : List.of();
        } else if (index == 2) {
            return player != null ? List.of(String.valueOf(player.getLocation().getBlockY())) : List.of();
        } else if (index == 3) {
            return player != null ? List.of(String.valueOf(player.getLocation().getBlockZ())) : List.of();
        } else if (index >= 4) {
            String key = arg.contains(":") ? arg.split(":", -1)[0] : arg;

            if (key.isBlank()) {
                return List.of("color:", "shape:", "fade:", "effect:", "power:");
            } else if ("color".startsWith(key)) {
                return Arrays.stream(Color.values()).map(c -> "color:" + c.name()).collect(Collectors.toList());
            } else if ("shape".startsWith(key)) {
                return Arrays.stream(FireworkEffect.Type.values()).map(t -> "shape:" + t.name()).collect(Collectors.toList());
            } else if ("fade".startsWith(key)) {
                return Arrays.stream(Color.values()).map(c -> "fade:" + c.name()).collect(Collectors.toList());
            } else if ("effect".startsWith(key)) {
                return List.of("effect:trail", "effect:twinkle");
            } else if ("power".startsWith(key)) {
                return IntStream.rangeClosed(0, 127)
                        .mapToObj(i -> "power:" + i)
                        .collect(Collectors.toList());
            }
        }
        return List.of();
    }
}
