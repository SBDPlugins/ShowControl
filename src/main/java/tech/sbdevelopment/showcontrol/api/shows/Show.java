package tech.sbdevelopment.showcontrol.api.shows;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import tech.sbdevelopment.showcontrol.ShowControlPlugin;
import tech.sbdevelopment.showcontrol.api.points.ShowCuePoint;
import tech.sbdevelopment.showcontrol.api.triggers.Trigger;
import tech.sbdevelopment.showcontrol.data.DataStorage;
import tech.sbdevelopment.showcontrol.utils.YamlFile;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * This class represents a show.
 */
@RequiredArgsConstructor
public class Show {
    /**
     * The name of the show.
     */
    @Getter
    private final String name;
    /**
     * A list of all cue points in the show.
     */
    @Getter
    private List<ShowCuePoint> cuePoints = new ArrayList<>();
    /**
     * The timer used to run the show.
     */
    private ScheduledExecutorService timer;

    /**
     * Create a new show.
     *
     * @param name The name of the show.
     * @param cuePoints A list of all cue points in the show.
     */
    public Show(String name, List<ShowCuePoint> cuePoints) {
        this.name = name;
        this.cuePoints = cuePoints;
    }

    /**
     * Add a point to the show.
     *
     * @param time The time of the point.
     * @param data The data of the point.
     */
    public void addPoint(Long time, Trigger data) {
        cuePoints.add(new ShowCuePoint(time, data));
        DataStorage.save();
    }

    /**
     * Remove a point from the show.
     *
     * @param point The point to remove.
     */
    public void removePoint(ShowCuePoint point) {
        point.getData().remove();
        cuePoints.remove(point);

        YamlFile data = DataStorage.getFiles().get(name);

        data.getFile().set(point.getCueID().toString(), null);
        data.saveFile();
    }

    /**
     * Start the show.
     */
    public void start() {
        timer = Executors.newSingleThreadScheduledExecutor();
        for (ShowCuePoint point : cuePoints) {
            timer.schedule(() -> Bukkit.getScheduler().runTask(ShowControlPlugin.getInstance(), () -> point.getData().trigger()), point.getTime(), TimeUnit.MILLISECONDS);
        }
    }

    /**
     * Cancel the show.
     */
    public void cancel() {
        if (timer != null) timer.shutdownNow();
        timer = null;
    }
}
