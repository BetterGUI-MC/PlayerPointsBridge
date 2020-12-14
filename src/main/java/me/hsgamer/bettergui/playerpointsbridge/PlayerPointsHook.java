package me.hsgamer.bettergui.playerpointsbridge;

import org.black_ixx.playerpoints.PlayerPoints;
import org.bukkit.Bukkit;

import java.util.UUID;

public class PlayerPointsHook {

    private static PlayerPoints playerPoints;

    private PlayerPointsHook() {
        // EMPTY
    }

    public static void setupPlugin() {
        playerPoints = (PlayerPoints) Bukkit.getPluginManager().getPlugin("PlayerPoints");
    }

    public static int getPoints(UUID uuid) {
        return playerPoints.getAPI().look(uuid);
    }

    public static boolean hasPoints(UUID uuid, int minimum) {
        return playerPoints.getAPI().look(uuid) >= minimum;
    }

    public static boolean takePoints(UUID uuid, int points) {
        return playerPoints.getAPI().take(uuid, points);
    }

    public static boolean givePoints(UUID uuid, int points) {
        return playerPoints.getAPI().give(uuid, points);
    }

}
