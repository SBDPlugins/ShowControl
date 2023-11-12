package tech.sbdevelopment.showcontrol.utils;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimeUtil {
    public static Long toMillis(String input) {
        long time = 0L;

        if (input.contains(":")) {
            return LocalTime.parse(input, DateTimeFormatter.ofPattern("HH:mm:ss")).toSecondOfDay() * 1000L;
        }

        input = input.toLowerCase() + "-";

        Pattern pattern = Pattern.compile("(\\d+)([a-z]+)");
        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            String value = matcher.group(1);
            String unit = matcher.group(2);

            switch (unit) {
                case "ms":
                    time += Long.parseLong(value);
                    break;
                case "s":
                    time += Math.round(Double.parseDouble(value) * 1000);
                    break;
                case "m":
                    time += Math.round(Double.parseDouble(value) * 60000);
                    break;
                case "t":
                    time += Integer.parseInt(value) * 50L;
                    break;
                default:
                    throw new IllegalArgumentException("Unknown time unit: " + unit);
            }
        }

        return time;
    }

    public static String makeReadable(Long time) {
        return String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(time),
                TimeUnit.MILLISECONDS.toMinutes(time) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(time)),
                TimeUnit.MILLISECONDS.toSeconds(time) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time)));
    }
}