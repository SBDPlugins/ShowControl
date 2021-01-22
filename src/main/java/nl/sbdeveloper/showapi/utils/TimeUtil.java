package nl.sbdeveloper.showapi.utils;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimeUtil {
    private static final int s = 1000;
    private static final int m = s * 60;
    private static final int h = m * 60;
    private static final int d = h * 24;
    private static final int w = d * 7;
    private static final int y = (int)(d * 365.25);

    public static String showTime(int seconds) {
        LocalTime timeOfDay = LocalTime.ofSecondOfDay(seconds / 20);
        return timeOfDay.toString();
    }

    public static int parseTicks(String str) {
        try {
            LocalTime localTime = LocalTime.parse(str);
            return localTime.toSecondOfDay();
        } catch (DateTimeParseException ex) {
            Pattern pattern = Pattern.compile("^(-?(?:\\d+)?\\.?\\d+) *(ticks?|tick?|t|seconds?|secs?|s|minutes?|mins?|m|hours?|hrs?|h|days?|d|weeks?|w|years?|yrs?|y)?$");
            Matcher matcher = pattern.matcher(str);

            if (!matcher.find()) return 0;

            float n = Float.parseFloat(matcher.group(1));
            switch (matcher.group(2).toLowerCase()) {
                case "years":
                case "year":
                case "yrs":
                case "yr":
                case "y":
                    return (int)(n * y) / 20000;
                case "weeks":
                case "week":
                case "w":
                    return (int)(n * w) / 20000;
                case "days":
                case "day":
                case "d":
                    return (int)(n * d) / 20000;
                case "hours":
                case "hour":
                case "hrs":
                case "hr":
                case "h":
                    return (int)(n * h) / 20000;
                case "minutes":
                case "minute":
                case "mins":
                case "min":
                case "m":
                    return (int)(n * m) / 20000;
                case "seconds":
                case "second":
                case "secs":
                case "sec":
                case "s":
                    return (int)(n * s) / 20000;
                case "ticks":
                case "tick":
                case "ts":
                case "t":
                    return (int) n / 20000;
                default:
                    return 0;
            }
        }
    }
}
