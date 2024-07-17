package me.hsgamer.bettergui.playerpointsbridge;

import me.hsgamer.bettergui.builder.ActionBuilder;
import me.hsgamer.bettergui.util.SchedulerUtil;
import me.hsgamer.hscore.action.common.Action;
import me.hsgamer.hscore.common.StringReplacer;
import me.hsgamer.hscore.common.Validate;
import me.hsgamer.hscore.task.element.TaskProcess;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public class GivePointsAction implements Action {
    private final String value;

    protected GivePointsAction(ActionBuilder.Input input) {
        this.value = input.getValue();
    }

    @Override
    public void apply(UUID uuid, TaskProcess process, StringReplacer stringReplacer) {
        String parsed = stringReplacer.replaceOrOriginal(value, uuid);
        Optional<Integer> optionalPoint = Validate.getNumber(parsed).map(BigDecimal::intValue);
        if (!optionalPoint.isPresent()) {
            Optional.ofNullable(Bukkit.getPlayer(uuid)).ifPresent(player -> player.sendMessage(ChatColor.RED + "Invalid point amount: " + parsed));
            process.next();
            return;
        }
        int pointsToGive = optionalPoint.get();
        if (pointsToGive > 0) {
            SchedulerUtil.global().run(() -> {
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
