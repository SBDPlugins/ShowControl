package tech.sbdevelopment.showcontrol.utils;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

/**
 * Source from:
 * https://github.com/Mindgamesnl/OpenAudioMc/blob/master/plugin/src/main/java/com/craftmend/openaudiomc/spigot/modules/show/util/TimeParser.java
 */
public class TimeUtil {

    public static Long toMilis(String input) {
        long time = 0L;

        // ITS A TIMECODE
        if (input.contains(":")) {
            return LocalTime.parse(input, DateTimeFormatter.ofPattern("HH:mm:ss")).toSecondOfDay() * 1000L;
        }

        input = input.toLowerCase() + "-";

        String[] milisSplit = input.split("ms");
        if (isValid(milisSplit)) {
            time += Long.parseLong(milisSplit[0]);
            return time;
        }

        String[] secondsSplit = input.split("s");
        if (isValid(secondsSplit)) {
            time += Math.round(Double.parseDouble(secondsSplit[0]) * 1000);
            return time;
        }

        String[] minutesSplit = input.split("m");
        if (isValid(minutesSplit)) {
            time += Math.round(Double.parseDouble(minutesSplit[0]) * 60000);
            return time;
        }

        String[] tickSplit = input.split("t");
        if (isValid(tickSplit)) {
            time += Math.round(Integer.parseInt(tickSplit[0]) * 50);
            return time;
        }

        return time;
    }

    private static boolean isValid(String[] array) {
        return array.length > 1 && array[0].length() > 0;
    }

    public static Object makeReadable(Long time) {
        return String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(time),
                TimeUnit.MILLISECONDS.toMinutes(time) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(time)),
                TimeUnit.MILLISECONDS.toSeconds(time) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time)));
    }
}