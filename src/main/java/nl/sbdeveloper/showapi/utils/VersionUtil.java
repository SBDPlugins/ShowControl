package nl.sbdeveloper.showapi.utils;

import org.bukkit.Bukkit;

public class VersionUtil {
    private static final int VERSION = Integer.parseInt(getMajorVersion(Bukkit.getVersion()).substring(2));

    private static String getMajorVersion(String version) {
        // getVersion()
        int index = version.lastIndexOf("MC:");
        if (index != -1) {
            version = version.substring(index + 4, version.length() - 1);
        } else if (version.endsWith("SNAPSHOT")) {
            // getBukkitVersion()
            index = version.indexOf('-');
            version = version.substring(0, index);
        }

        // 1.13.2, 1.14.4, etc...
        int lastDot = version.lastIndexOf('.');
        if (version.indexOf('.') != lastDot) version = version.substring(0, lastDot);

        return version;
    }

    public static int getVersion() {
        return VERSION;
    }
}
