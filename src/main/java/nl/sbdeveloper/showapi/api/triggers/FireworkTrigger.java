package nl.sbdeveloper.showapi.api.triggers;

import nl.sbdeveloper.showapi.ShowAPI;
import nl.sbdeveloper.showapi.api.TriggerTask;
import nl.sbdeveloper.showapi.api.TriggerType;
import nl.sbdeveloper.showapi.utils.Color;
import org.bukkit.Bukkit;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.World;

public class FireworkTrigger extends TriggerTask {
    private ShowAPI.Fireworks.Firework fw;
    private Location spawnLoc;

    public FireworkTrigger(String[] data) {
        super(TriggerType.FIREWORK, data);

        World w = Bukkit.getWorld(data[0]);
        if (w == null) {
            Bukkit.getLogger().info("De wereld is null!");
            return;
        }

        int x;
        int y;
        int z;
        try {
            x = Integer.parseInt(data[1]);
            y = Integer.parseInt(data[2]);
            z = Integer.parseInt(data[3]);
        } catch (NumberFormatException ex) {
            Bukkit.getLogger().info("De positie is incorrect!");
            return;
        }

        this.spawnLoc = new Location(w, x, y, z);

        ShowAPI.Fireworks.Firework firework = new ShowAPI.Fireworks.Firework();
        for (int i = 4; i < data.length; i++) {
            if (data[i].split(":").length != 2) continue;

            String key = data[i].split(":")[0];
            String value = data[i].split(":")[1];
            if (key.equalsIgnoreCase("color")) {
                firework = firework.addColor(Color.valueOf(value.toUpperCase()).getBukkitColor());
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
        ShowAPI.Fireworks.spawn(fw, spawnLoc);
    }

    @Override
    public void remove() {} //Firework is one-time, ignore.
}
