package tech.sbdevelopment.showcontrol.api;

import lombok.Getter;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import tech.sbdevelopment.showcontrol.ShowControlPlugin;
import tech.sbdevelopment.showcontrol.api.exceptions.InvalidTriggerException;
import tech.sbdevelopment.showcontrol.api.exceptions.TooFewArgumentsException;
import tech.sbdevelopment.showcontrol.api.points.ShowCuePoint;
import tech.sbdevelopment.showcontrol.api.triggers.Trigger;
import tech.sbdevelopment.showcontrol.api.triggers.TriggerIdentifier;
import tech.sbdevelopment.showcontrol.data.DataStorage;
import tech.sbdevelopment.showcontrol.utils.YamlFile;
import org.bukkit.Bukkit;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SCAPI {
    @Getter
    private static final Map<String, Class<? extends Trigger>> triggers = new HashMap<>();
    @Getter
    private static final HashMap<String, List<ShowCuePoint>> showsMap = new HashMap<>();
    private static final HashMap<String, ScheduledExecutorService> showTimers = new HashMap<>();

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
        }
    }

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

    public static void create(String name) {
        showsMap.put(name, new ArrayList<>());
        DataStorage.save();
    }

    public static void delete(String name) {
        showsMap.remove(name);

        File data = new File(ShowControlPlugin.getInstance().getDataFolder(), "data/" + name + ".yml");
        data.delete();
    }

    public static boolean exists(String name) {
        return showsMap.containsKey(name);
    }

    public static List<ShowCuePoint> getPoints(String name) {
        if (!exists(name)) return new ArrayList<>();
        return showsMap.get(name);
    }

    public static void addPoint(String name, Long time, Trigger data) {
        if (!exists(name)) return;
        getPoints(name).add(new ShowCuePoint(time, data));
        DataStorage.save();
    }

    public static void removePoint(String name, ShowCuePoint point) {
        if (!exists(name)) return;

        point.getData().remove();
        showsMap.get(name).remove(point);

        YamlFile data = DataStorage.getFiles().get(name);

        data.getFile().set(point.getCueID().toString(), null);
        data.saveFile();
    }

    public static void startShow(String name) {
        if (!exists(name)) return;
        ScheduledExecutorService showTimer = Executors.newSingleThreadScheduledExecutor();
        for (ShowCuePoint point : getPoints(name)) {
            showTimer.schedule(() -> Bukkit.getScheduler().runTask(ShowControlPlugin.getInstance(), () -> point.getData().trigger()), point.getTime(), TimeUnit.MILLISECONDS);
        }
        showTimers.put(name, showTimer);
    }

    public static void cancelShow(String name) {
        if (!exists(name)) return;
        if (!showTimers.containsKey(name)) return;
        ScheduledExecutorService showTimer = showTimers.get(name);
        showTimer.shutdownNow();
    }
}
