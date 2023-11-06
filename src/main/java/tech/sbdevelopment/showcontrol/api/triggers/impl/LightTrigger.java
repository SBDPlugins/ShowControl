package tech.sbdevelopment.showcontrol.api.triggers.impl;

import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.Lightable;
import org.bukkit.entity.Player;
import tech.sbdevelopment.showcontrol.api.exceptions.InvalidArgumentException;
import tech.sbdevelopment.showcontrol.api.triggers.Trigger;
import tech.sbdevelopment.showcontrol.api.triggers.TriggerIdentifier;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(force = true)
@TriggerIdentifier(value = "light", minArgs = 5, argDesc = "<world> <x> <y> <z> <state>", item = Material.REDSTONE_LAMP)
public class LightTrigger extends Trigger {
    private final Location location;
    private final boolean state;

    public LightTrigger(String[] data) throws InvalidArgumentException {
        super(data);

        World w = Bukkit.getWorld(data[0]);
        if (w == null) {
            throw new InvalidArgumentException("Provided World in LightTrigger is null!");
        }

        int x;
        int y;
        int z;
        try {
            x = Integer.parseInt(data[1]);
            y = Integer.parseInt(data[2]);
            z = Integer.parseInt(data[3]);
        } catch (NumberFormatException ex) {
            throw new InvalidArgumentException("Provided position in LightTrigger is invalid!");
        }

        this.location = new Location(w, x, y, z);
        this.location.getBlock().setType(Material.REDSTONE_LAMP);

        this.state = parseBoolean(data[4]);
    }

    @Override
    public void trigger() {
        setLightState(location.getBlock(), state);
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
            return List.of("on", "off");
        }
        return List.of();
    }

    private static boolean parseBoolean(String value) {
        return value.equalsIgnoreCase("true")
                || value.equalsIgnoreCase("on")
                || value.equalsIgnoreCase("yes")
                || value.equalsIgnoreCase("1");
    }

    private static void setLightState(Block b, boolean state) {
        if (!(b.getBlockData() instanceof Lightable)) {
            b.setType(Material.REDSTONE_LAMP);
        }

        Lightable light = (Lightable) b.getBlockData();
        light.setLit(state);
        b.setBlockData(light);
    }
}
