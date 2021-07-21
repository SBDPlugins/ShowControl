package nl.sbdeveloper.showapi.api;

import nl.sbdeveloper.showapi.api.triggers.*;

public enum TriggerType {
    COMMAND(CommandTrigger.class, 2),
    FIREWORK(FireworkTrigger.class, 6),
    FAKE_FIREWORK(FakeFireworkTrigger.class, 7),
    SPOT(SpotTrigger.class, 6),
//    LASER(LaserTrigger.class, 6),
    ANIMA(AnimaTrigger.class, 2),
    PARTICLE(ParticleTrigger.class, 7);

    private final Class<? extends TriggerTask> trigger;
    private final int minArgs;

    TriggerType(Class<? extends TriggerTask> trigger, int minArgs) {
        this.trigger = trigger;
        this.minArgs = minArgs;
    }

    public Class<? extends TriggerTask> getTrigger() {
        return trigger;
    }

    public int getMinArgs() {
        return minArgs;
    }
}
