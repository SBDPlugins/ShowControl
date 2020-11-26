package nl.sbdeveloper.showapi.api;

import nl.sbdeveloper.showapi.api.triggers.*;

public enum TriggerType {
    COMMAND(CommandTrigger.class, 2),
    FIREWORK(FireworkTrigger.class, 6),
    SPOT(SpotTrigger.class, 6),
    LASER(LaserTrigger.class, 6),
    ANIMA(AnimaTrigger.class, 2),
    PARTICLE(ParticleTrigger.class, 7);

    private final Class<? extends TriggerData> trigger;
    private final int minArgs;

    TriggerType(Class<? extends TriggerData> trigger, int minArgs) {
        this.trigger = trigger;
        this.minArgs = minArgs;
    }

    public Class<? extends TriggerData> getTrigger() {
        return trigger;
    }

    public int getMinArgs() {
        return minArgs;
    }
}
