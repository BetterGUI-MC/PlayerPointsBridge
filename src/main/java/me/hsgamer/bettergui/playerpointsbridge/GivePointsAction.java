package me.hsgamer.bettergui.playerpointsbridge;

import me.hsgamer.bettergui.BetterGUI;
import me.hsgamer.bettergui.api.action.BaseAction;
import me.hsgamer.bettergui.builder.ActionBuilder;
import me.hsgamer.hscore.common.Validate;
import me.hsgamer.hscore.task.BatchRunnable;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public class GivePointsAction extends BaseAction {

    protected GivePointsAction(ActionBuilder.Input input) {
        super(input);
    }

    @Override
    public void accept(UUID uuid, BatchRunnable.Process process) {
        String parsed = getReplacedString(uuid);
        Optional<Integer> optionalPoint = Validate.getNumber(parsed).map(BigDecimal::intValue);
        if (!optionalPoint.isPresent()) {
            Optional.ofNullable(Bukkit.getPlayer(uuid)).ifPresent(player -> player.sendMessage(ChatColor.RED + "Invalid point amount: " + parsed));
            process.next();
            return;
        }
        int pointsToGive = optionalPoint.get();
        if (pointsToGive > 0) {
            Bukkit.getScheduler().runTask(BetterGUI.getInstance(), () -> {
                if (!PlayerPointsHook.givePoints(uuid, pointsToGive)) {
                    Optional.ofNullable(Bukkit.getPlayer(uuid)).ifPresent(player -> player.sendMessage(ChatColor.RED + "Error: the transaction couldn't be executed. Please inform the staff."));
                }
                process.next();
            });
        } else {
            process.next();
        }
    }
}
