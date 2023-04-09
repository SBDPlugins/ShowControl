package nl.sbdeveloper.showcontrol.api;

import lombok.Getter;
import nl.sbdeveloper.showcontrol.api.triggers.Trigger;
import nl.sbdeveloper.showcontrol.api.triggers.TriggerIdentifier;
import org.bukkit.Bukkit;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ShowAPI {
    @Getter
    private static final Map<String, Class<? extends Trigger>> triggers = new HashMap<>();

    public static void index(Class<?> clazz, String... packages) {
        Bukkit.getLogger().info("Indexing triggers for starting point " + clazz.getSimpleName() + "...");

        FilterBuilder filterBuilder = new FilterBuilder();
        for (String p : packages) filterBuilder.includePackage(p);

        ConfigurationBuilder config = new ConfigurationBuilder()
                .setScanners(Scanners.SubTypes.filterResultsBy(new FilterBuilder().includePattern(Trigger.class.getName())), Scanners.TypesAnnotated)
                .setUrls(ClasspathHelper.forClass(clazz))
                .filterInputsBy(filterBuilder);

        Reflections reflections = new Reflections(config);

        Set<Class<?>> trig = reflections.getTypesAnnotatedWith(TriggerIdentifier.class);
        for (Class<?> trigger : trig) {
            Bukkit.getLogger().info("Found trigger " + trigger.getSimpleName() + ".");

            TriggerIdentifier identifier = trigger.getAnnotation(TriggerIdentifier.class);
            triggers.put(identifier.value(), (Class<? extends Trigger>) trigger);
        }
    }

    public static <T extends Trigger> T getTrigger(String data) throws ReflectiveOperationException, InvalidTriggerException, TooFewArgumentsException, IllegalArgumentException {
        String[] dataSplitter = data.split(" ");
        String[] dataSplitterNew = Arrays.copyOfRange(dataSplitter, 1, dataSplitter.length);

        String triggerType = dataSplitter[0];

        if (!triggers.containsKey(triggerType)) throw new InvalidTriggerException("Provided trigger " + triggerType + " does not exists!");

        Constructor<T> ctor = (Constructor<T>) triggers.get(triggerType).getConstructor(String[].class);
        if (dataSplitter.length < triggers.get(triggerType).getAnnotation(TriggerIdentifier.class).minArgs() + 1) throw new TooFewArgumentsException("Provided triggerdata " + data + " has too few arguments!");
        return ctor.newInstance(new Object[] { dataSplitterNew });
    }
}
