package tech.sbdevelopment.showcontrol.api;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import tech.sbdevelopment.showcontrol.ShowControlPlugin;
import tech.sbdevelopment.showcontrol.api.exceptions.InvalidTriggerException;
import tech.sbdevelopment.showcontrol.api.exceptions.TooFewArgumentsException;
import tech.sbdevelopment.showcontrol.api.points.ShowCuePoint;
import tech.sbdevelopment.showcontrol.api.shows.Show;
import tech.sbdevelopment.showcontrol.api.triggers.Trigger;
import tech.sbdevelopment.showcontrol.api.triggers.TriggerIdentifier;
import tech.sbdevelopment.showcontrol.data.DataStorage;
import tech.sbdevelopment.showcontrol.utils.YamlFile;

import javax.annotation.Nullable;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * This class is the main class of the API.
 */
public class SCAPI {
    /**
     * A map of all default trigger, only used for tab complete.
     */
    @Getter
    private static final Map<String, Trigger> defaultTriggers = new HashMap<>();

    /**
     * A map of all triggers, used for creating new triggers.
     */
    @Getter
    private static final Map<String, Class<? extends Trigger>> triggers = new HashMap<>();

    /**
     * A map of all shows.
     */
    @Getter
    private static final HashMap<String, Show> showsMap = new HashMap<>();

    /**
     * Index all triggers in a package. Call this method in your onEnable method.
     *
     * @param clazz The class to use as starting point, usually your main class.
     * @param packages The packages to index.
     */
    public static void index(Class<?> clazz, String... packages) {
        ShowControlPlugin.getInstance().getLogger().info("Indexing triggers for starting point " + clazz.getSimpleName() + "...");

        FilterBuilder filterBuilder = new FilterBuilder();
        for (String p : packages) filterBuilder.includePackage(p);

        ConfigurationBuilder config = new ConfigurationBuilder()
                .setScanners(Scanners.SubTypes.filterResultsBy(new FilterBuilder().includePattern(Trigger.class.getName())), Scanners.TypesAnnotated)
                .setUrls(ClasspathHelper.forClass(clazz))
                .filterInputsBy(filterBuilder);

        Reflections reflections = new Reflections(config);

        Set<Class<?>> trig = reflections.getTypesAnnotatedWith(TriggerIdentifier.class);
        for (Class<?> trigger : trig) {
            ShowControlPlugin.getInstance().getLogger().info("Found trigger " + trigger.getSimpleName() + ".");

            TriggerIdentifier identifier = trigger.getAnnotation(TriggerIdentifier.class);
            triggers.put(identifier.value(), (Class<? extends Trigger>) trigger);
            try {
                defaultTriggers.put(identifier.value(), (Trigger) trigger.getDeclaredConstructor().newInstance());
            } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                     IllegalAccessException ex) {
                throw new RuntimeException("Could not find default constructor for trigger " + trigger.getSimpleName() + "! Tab complete will not work for this trigger.", ex);
            }
        }
    }

    /**
     * Get the tab complete for a trigger.
     *
     * @param triggerType The trigger type.
     * @param player The player that wants to add the cue, will be null if it's not a player.
     * @param index The current argument index.
     * @param arg The current argument.
     * @return The tab complete value based on the index and argument.
     */
    public static List<String> getTabComplete(String triggerType, @Nullable Player player, int index, String arg) {
        if (!defaultTriggers.containsKey(triggerType)) return new ArrayList<>();
        return defaultTriggers.get(triggerType).getArgumentTabComplete(player, index, arg);
    }

    /**
     * Get a trigger from a data string.
     *
     * @param data The data string.
     * @return The trigger.
     * @param <T> The trigger type.
     * @throws ReflectiveOperationException When the trigger could not be created.
     * @throws InvalidTriggerException When the trigger does not exist.
     * @throws TooFewArgumentsException When the trigger data does not have enough arguments.
     * @throws IllegalArgumentException When the trigger data is invalid.
     */
    public static <T extends Trigger> T getTrigger(String data) throws ReflectiveOperationException, InvalidTriggerException, TooFewArgumentsException, IllegalArgumentException {
        String[] dataSplitter = data.split(" ");
        String[] dataSplitterNew = Arrays.copyOfRange(dataSplitter, 1, dataSplitter.length);

        String triggerType = dataSplitter[0];

        if (!triggers.containsKey(triggerType))
            throw new InvalidTriggerException("Provided trigger " + triggerType + " does not exists!");

        Constructor<T> ctor = (Constructor<T>) triggers.get(triggerType).getConstructor(String[].class);
        TriggerIdentifier identifier = triggers.get(triggerType).getAnnotation(TriggerIdentifier.class);
        if (dataSplitter.length < identifier.minArgs() + 1)
            throw new TooFewArgumentsException(identifier.argDesc());
        return ctor.newInstance(new Object[]{dataSplitterNew});
    }

    /**
     * Create a new show.
     *
     * @param name The name of the show.
     */
    public static void create(String name) {
        showsMap.put(name, new Show(name));
        DataStorage.save();
    }

    /**
     * Delete a show.
     *
     * @param name The name of the show.
     */
    public static void delete(String name) {
        showsMap.remove(name);

        File data = new File(ShowControlPlugin.getInstance().getDataFolder(), "data/" + name + ".yml");
        data.delete();
    }

    /**
     * Check if a show exists.
     *
     * @param name The name of the show.
     * @return If the show exists.
     */
    public static boolean exists(String name) {
        return showsMap.containsKey(name);
    }

    /**
     * Get a show by name.
     *
     * @param name The name of the show.
     * @return An optional of the show.
     */
    public static Optional<Show> getShow(String name) {
        return Optional.of(showsMap.get(name));
    }
}
