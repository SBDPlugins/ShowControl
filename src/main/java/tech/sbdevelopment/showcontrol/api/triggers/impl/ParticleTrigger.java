package tech.sbdevelopment.showcontrol.api.triggers.impl;

import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;
import tech.sbdevelopment.showcontrol.api.exceptions.InvalidArgumentException;
import tech.sbdevelopment.showcontrol.api.triggers.Trigger;
import tech.sbdevelopment.showcontrol.api.triggers.TriggerIdentifier;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(force = true)
@TriggerIdentifier(value = "particle", minArgs = 6, argDesc = "<world> <x> <y> <z> <type> <count>")
public class ParticleTrigger extends Trigger {
    private final Particle type;
    private final Location spawnLoc;
    private final int count;

    public ParticleTrigger(String[] data) throws InvalidArgumentException {
        super(data);

        World w = Bukkit.getWorld(data[0]);
        if (w == null) {
            throw new InvalidArgumentException("Provided World in ParticleTrigger is null!");
        }

        int x;
        int y;
        int z;
        try {
            x = Integer.parseInt(data[1]);
            y = Integer.parseInt(data[2]);
            z = Integer.parseInt(data[3]);
        } catch (NumberFormatException ex) {
            throw new InvalidArgumentException("Provided position in ParticleTrigger is invalid!");
        }

        this.spawnLoc = new Location(w, x, y, z);

        try {
            this.type = Particle.valueOf(data[4]);
        } catch (IllegalArgumentException ex) {
            throw new InvalidArgumentException("Provided particle " + data[4] + " in ParticleTrigger does not exists!");
        }

        try {
            this.count = Integer.parseInt(data[5]);
        } catch (NumberFormatException ex) {
            throw new InvalidArgumentException("Provided count " + data[5] + " in ParticleTrigger is invalid!");
        }
    }

    @Override
    public void trigger() {
        spawnLoc.getWorld().spawnParticle(type, spawnLoc, count);
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
        } else if (index == 4) {
            return Arrays.stream(Particle.values()).map(Enum::name).collect(Collectors.toList());
        }
        return List.of();
    }
}
