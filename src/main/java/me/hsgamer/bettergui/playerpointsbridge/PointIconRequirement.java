package me.hsgamer.bettergui.playerpointsbridge;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import me.hsgamer.bettergui.config.MessageConfig;
import me.hsgamer.bettergui.object.Requirement;
import me.hsgamer.bettergui.object.variable.LocalVariable;
import me.hsgamer.bettergui.object.variable.LocalVariableManager;
import me.hsgamer.bettergui.util.MessageUtils;
import me.hsgamer.bettergui.util.common.Validate;
import me.hsgamer.bettergui.util.expression.ExpressionUtils;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class PointIconRequirement extends Requirement<Object, Integer> implements LocalVariable {

  private final Map<UUID, Integer> checked = new HashMap<>();

  public PointIconRequirement() {
    super(true);
  }

  @Override
  public Integer getParsedValue(Player player) {
    String parsed = parseFromString(String.valueOf(value).trim(), player);
    if (ExpressionUtils.isValidExpression(parsed)) {
      return ExpressionUtils.getResult(parsed).intValue();
    } else {
      Optional<BigDecimal> number = Validate.getNumber(parsed);
      if (number.isPresent()) {
        return number.get().intValue();
      } else {
        MessageUtils.sendMessage(player,
            MessageConfig.INVALID_NUMBER.getValue().replace("{input}", parsed));
        return 0;
      }
    }
  }

  @Override
  public boolean check(Player player) {
    int points = getParsedValue(player);
    if (points > 0 && !PlayerPointsHook.hasPoints(player, points)) {
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
  public LocalVariableManager<?> getInvolved() {
    return getVariableManager();
  }

  @Override
  public String getReplacement(OfflinePlayer player, String s) {
    if (!player.isOnline()) {
      return "";
    }
    int points = getParsedValue(player.getPlayer());
    if (points > 0 && !PlayerPointsHook.hasPoints(player, points)) {
      return String.valueOf(points);
    }
    return MessageConfig.HAVE_MET_REQUIREMENT_PLACEHOLDER.getValue();
  }
}
