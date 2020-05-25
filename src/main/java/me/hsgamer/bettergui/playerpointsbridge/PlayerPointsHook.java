package me.hsgamer.bettergui.playerpointsbridge;

import org.black_ixx.playerpoints.PlayerPoints;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class PlayerPointsHook {

  private static PlayerPoints playerPoints;

  public static void setupPlugin() {
    playerPoints = (PlayerPoints) Bukkit.getPluginManager().getPlugin("PlayerPoints");
  }

  public static int getPoints(OfflinePlayer player) {
    return playerPoints.getAPI().look(player.getUniqueId());
  }

  public static boolean hasPoints(OfflinePlayer player, int minimum) {
    return playerPoints.getAPI().look(player.getUniqueId()) >= minimum;
  }

  public static boolean takePoints(OfflinePlayer player, int points) {
    return playerPoints.getAPI().take(player.getUniqueId(), points);
  }

  public static boolean givePoints(OfflinePlayer player, int points) {
    return playerPoints.getAPI().give(player.getUniqueId(), points);
  }

}
