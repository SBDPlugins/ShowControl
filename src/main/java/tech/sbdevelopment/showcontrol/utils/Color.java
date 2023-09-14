package tech.sbdevelopment.showcontrol.utils;

public enum Color {
    WHITE(org.bukkit.Color.fromRGB(16777215)),
    SILVER(org.bukkit.Color.fromRGB(12632256)),
    GRAY(org.bukkit.Color.fromRGB(8421504)),
    BLACK(org.bukkit.Color.fromRGB(0)),
    RED(org.bukkit.Color.fromRGB(16711680)),
    MAROON(org.bukkit.Color.fromRGB(8388608)),
    YELLOW(org.bukkit.Color.fromRGB(16776960)),
    OLIVE(org.bukkit.Color.fromRGB(8421376)),
    LIME(org.bukkit.Color.fromRGB(65280)),
    GREEN(org.bukkit.Color.fromRGB(32768)),
    AQUA(org.bukkit.Color.fromRGB(65535)),
    TEAL(org.bukkit.Color.fromRGB(32896)),
    BLUE(org.bukkit.Color.fromRGB(255)),
    NAVY(org.bukkit.Color.fromRGB(128)),
    FUCHSIA(org.bukkit.Color.fromRGB(16711935)),
    PURPLE(org.bukkit.Color.fromRGB(8388736)),
    ORANGE(org.bukkit.Color.fromRGB(16753920));

    private final org.bukkit.Color color;

    Color(org.bukkit.Color color) {
        this.color = color;
    }

    public org.bukkit.Color getBukkitColor() {
        return color;
    }
}
