package me.hsgamer.bettergui.playerpointsbridge;

import org.black_ixx.playerpoints.PlayerPoints;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlayerPointsHook {

  private static PlayerPoints playerPoints;

  public static void setupPlugin() {
    playerPoints = (PlayerPoints) Bukkit.getPluginManager().getPlugin("PlayerPoints");
  }

  public static int getPoints(Player player) {
    return playerPoints.getAPI().look(player.getUniqueId());
  }

  public static boolean hasPoints(Player player, int minimum) {
    return playerPoints.getAPI().look(player.getUniqueId()) >= minimum;
  }

  public static boolean takePoints(Player player, int points) {
    return playerPoints.getAPI().take(player.getUniqueId(), points);
  }

  public static boolean givePoints(Player player, int points) {
    return playerPoints.getAPI().give(player.getUniqueId(), points);
  }

}
