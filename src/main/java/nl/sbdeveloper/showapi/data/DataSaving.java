package nl.sbdeveloper.showapi.data;

import nl.sbdeveloper.showapi.ShowAPIPlugin;
import nl.sbdeveloper.showapi.api.ShowCue;
import nl.sbdeveloper.showapi.api.TriggerData;
import nl.sbdeveloper.showapi.utils.MainUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DataSaving {
    public static void load() {
        boolean newSystem = ShowAPIPlugin.getData().getFile().contains("NewSystem");

        for (String name : ShowAPIPlugin.getData().getFile().getConfigurationSection("Shows").getKeys(false)) {
            List<ShowCue> cues = new ArrayList<>();

            for (String id : ShowAPIPlugin.getData().getFile().getConfigurationSection("Shows." + name).getKeys(false)) {
                UUID cueID = UUID.fromString(id);

                TriggerData data = MainUtil.parseData(ShowAPIPlugin.getData().getFile().getString("Shows." + name + "." + id + ".Type") + " " + ShowAPIPlugin.getData().getFile().getString("Shows." + name + "." + id + ".Data"));

                int ticks;
                if (!newSystem) ticks = ShowAPIPlugin.getData().getFile().getInt("Shows." + name + "." + id + ".Time") * 20;
                else ticks = ShowAPIPlugin.getData().getFile().getInt("Shows." + name + "." + id + ".Time");

                if (!newSystem) {
                    ShowAPIPlugin.getData().getFile().set("Shows." + name + "." + id + ".Time", ticks);
                    ShowAPIPlugin.getData().getFile().set("NewSystem", true);
                    ShowAPIPlugin.getData().saveFile();
                }

                cues.add(new ShowCue(cueID, ShowAPIPlugin.getData().getFile().getInt("Shows." + name + "." + id + ".Time"), data));
            }

            Shows.getShowsMap().put(name, cues);
        }
    }

    public static void save() {
        for (Map.Entry<String, List<ShowCue>> entry : Shows.getShowsMap().entrySet()) {
            for (ShowCue cue : entry.getValue()) {
                ShowAPIPlugin.getData().getFile().set("Shows." + entry.getKey() + "." + cue.getCueID().toString() + ".Time", cue.getTicks());
                ShowAPIPlugin.getData().getFile().set("Shows." + entry.getKey() + "." + cue.getCueID().toString() + ".Type", cue.getData().getType().name());
                ShowAPIPlugin.getData().getFile().set("Shows." + entry.getKey() + "." + cue.getCueID().toString() + ".Data", cue.getData().getDataString());
            }
            ShowAPIPlugin.getData().saveFile();
        }
    }
}
