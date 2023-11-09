package tech.sbdevelopment.showcontrol.api.triggers.impl;

import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import tech.sbdevelopment.showcontrol.api.exceptions.InvalidArgumentException;
import tech.sbdevelopment.showcontrol.api.triggers.Trigger;
import tech.sbdevelopment.showcontrol.api.triggers.TriggerIdentifier;
import tech.sbdevelopment.showcontrol.elements.Spots;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(force = true)
@TriggerIdentifier(value = "spot", minArgs = 5, argDesc = "<name> <world> <x> <y> <z> [speed]")
public class SpotTrigger extends Trigger {
    private final String name;
    private final Location newLocation;
    private final double speed;

    public SpotTrigger(String[] data) throws InvalidArgumentException {
        super(data);

        this.name = data[0];

        World w = Bukkit.getWorld(data[1]);
        if (w == null) {
            throw new InvalidArgumentException("Provided World in SpotTrigger is null!");
        }

        int x;
        int y;
        int z;
        try {
            x = Integer.parseInt(data[2]);
            y = Integer.parseInt(data[3]);
            z = Integer.parseInt(data[4]);
        } catch (NumberFormatException ex) {
            throw new InvalidArgumentException("Provided position in SpotTrigger is invalid!");
        }

        this.newLocation = new Location(w, x, y, z);

        try {
            this.speed = data.length >= 6 ? Double.parseDouble(data[5]) : 0.1;
        } catch (NumberFormatException ex) {
            throw new InvalidArgumentException("Provided speed in SpotTrigger is invalid!");
        }

        if (!Spots.exists(name)) {
            Spots.start(name, newLocation);
        }
    }

    @Override
    public void trigger() {
        Spots.move(name, newLocation, speed);
    }

    @Override
    public List<String> getArgumentTabComplete(Player player, int index, String arg) {
        if (index == 0) {
            return new ArrayList<>(Spots.getSpots().keySet());
        } else if (index == 1) {
            return player != null ? List.of(player.getWorld().getName()) : Bukkit.getWorlds().stream().map(World::getName).collect(Collectors.toList());
        } else if (index == 2) {
            return player != null ? List.of(String.valueOf(player.getLocation().getBlockX())) : List.of();
        } else if (index == 3) {
            return player != null ? List.of(String.valueOf(player.getLocation().getBlockY())) : List.of();
        } else if (index == 4) {
            return player != null ? List.of(String.valueOf(player.getLocation().getBlockZ())) : List.of();
        }
        return List.of();
    }

    @Override
    public void remove() {
        Spots.remove(name);
    }
}
