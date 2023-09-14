package tech.sbdevelopment.showcontrol.api.triggers.impl;

import tech.sbdevelopment.showcontrol.api.triggers.Trigger;
import tech.sbdevelopment.showcontrol.api.triggers.TriggerIdentifier;
import org.bukkit.Material;

@TriggerIdentifier(value = "flamethrower", minArgs = 5, argDesc = "<world> <x> <y> <z> <delay>", item = Material.FIRE)
public class FlameThrowerTrigger extends Trigger {
    public FlameThrowerTrigger(String[] dataString) {
        super(dataString);
    }

    @Override
    public void trigger() {
        //TODO Implement
    }
}
