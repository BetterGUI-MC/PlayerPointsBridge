package me.hsgamer.bettergui.playerpointsbridge;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import me.hsgamer.bettergui.BetterGUI;
import me.hsgamer.bettergui.config.impl.MessageConfig.DefaultMessage;
import me.hsgamer.bettergui.object.Icon;
import me.hsgamer.bettergui.object.IconRequirement;
import me.hsgamer.bettergui.object.IconVariable;
import me.hsgamer.bettergui.util.CommonUtils;
import me.hsgamer.bettergui.util.ExpressionUtils;
import me.hsgamer.bettergui.util.Validate;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class PointIconRequirement extends IconRequirement<Object, Integer> implements IconVariable {

  private final Map<UUID, Integer> checked = new HashMap<>();

  public PointIconRequirement(Icon icon) {
    super(icon, true);
  }

  @Override
  public Integer getParsedValue(Player player) {
    String parsed = String.valueOf(value).trim();
    parsed = icon.hasVariables(parsed) ? icon.setVariables(parsed, player) : parsed;
    if (ExpressionUtils.isValidExpression(parsed)) {
      return ExpressionUtils.getResult(parsed).intValue();
    } else {
      if (Validate.isValidInteger(parsed)) {
        return Integer.parseInt(parsed);
      } else {
        CommonUtils.sendMessage(player,
            BetterGUI.getInstance().getMessageConfig().get(DefaultMessage.INVALID_NUMBER)
                .replace("{input}", parsed));
        return 0;
      }
    }
  }

  @Override
  public boolean check(Player player) {
    int points = getParsedValue(player);
    if (points > 0 && !PlayerPointsHook.hasPoints(player, points)) {
      sendFailCommand(player);
      return false;
    } else {
      checked.put(player.getUniqueId(), points);
      return true;
    }
  }

  @Override
  public void take(Player player) {
    if (!PlayerPointsHook.takePoints(player, checked.remove(player.getUniqueId()))) {
      player.sendMessage(ChatColor.RED
          + "Error: the transaction couldn't be executed. Please inform the staff.");
    }
  }

  @Override
  public String getIdentifier() {
    return "require_points";
  }

  @Override
  public Icon getIcon() {
    return this.icon;
  }

  @Override
  public String getReplacement(Player player, String s) {
    int points = getParsedValue(player);
    if (points > 0 && !PlayerPointsHook.hasPoints(player, points)) {
      return String.valueOf(points);
    }
    return BetterGUI.getInstance().getMessageConfig()
        .get(DefaultMessage.HAVE_MET_REQUIREMENT_PLACEHOLDER);
  }
}
