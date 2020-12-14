package me.hsgamer.bettergui.playerpointsbridge;

import me.hsgamer.bettergui.api.action.BaseAction;
import me.hsgamer.bettergui.config.MessageConfig;
import me.hsgamer.bettergui.lib.core.bukkit.utils.MessageUtils;
import me.hsgamer.bettergui.lib.core.common.Validate;
import me.hsgamer.bettergui.lib.core.expression.ExpressionUtils;
import me.hsgamer.bettergui.lib.taskchain.TaskChain;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class GivePointsAction extends BaseAction {

    public GivePointsAction(String string) {
        super(string);
    }

    @Override
    public void addToTaskChain(UUID uuid, TaskChain<?> taskChain) {
        int pointsToGive = 0;
        String parsed = getReplacedString(uuid);
        if (Validate.isValidPositiveNumber(parsed)) {
            pointsToGive = Integer.parseInt(parsed);
        } else if (ExpressionUtils.isValidExpression(parsed)) {
            pointsToGive = Objects.requireNonNull(ExpressionUtils.getResult(parsed)).intValue();
        } else {
            Optional.ofNullable(Bukkit.getPlayer(uuid)).ifPresent(player -> MessageUtils.sendMessage(player, MessageConfig.INVALID_NUMBER.getValue().replace("{input}", parsed)));
        }

        if (pointsToGive > 0) {
            int finalPointsToGive = pointsToGive;
            taskChain.sync(() -> {
                if (!PlayerPointsHook.givePoints(uuid, finalPointsToGive)) {
                    Optional.ofNullable(Bukkit.getPlayer(uuid)).ifPresent(player -> player.sendMessage(ChatColor.RED + "Error: the transaction couldn't be executed. Please inform the staff."));
                }
            });
        }
    }
}
