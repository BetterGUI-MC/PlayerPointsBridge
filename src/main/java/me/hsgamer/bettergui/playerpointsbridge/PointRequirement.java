package me.hsgamer.bettergui.playerpointsbridge;

import me.hsgamer.bettergui.BetterGUI;
import me.hsgamer.bettergui.api.requirement.TakableRequirement;
import me.hsgamer.bettergui.builder.RequirementBuilder;
import me.hsgamer.bettergui.config.MessageConfig;
import me.hsgamer.bettergui.util.SchedulerUtil;
import me.hsgamer.bettergui.util.StringReplacerApplier;
import me.hsgamer.hscore.bukkit.utils.MessageUtils;
import me.hsgamer.hscore.common.StringReplacer;
import me.hsgamer.hscore.common.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public class PointRequirement extends TakableRequirement<Integer> {
    protected PointRequirement(RequirementBuilder.Input input) {
        super(input);
        getMenu().getVariableManager().register(getName(), StringReplacer.of((original, uuid) -> {
            int points = getFinalValue(uuid);
            if (points > 0 && !PlayerPointsHook.hasPoints(uuid, points)) {
                return String.valueOf(points);
            }
            return BetterGUI.getInstance().get(MessageConfig.class).getHaveMetRequirementPlaceholder();
        }));
    }

    @Override
    protected boolean getDefaultTake() {
        return true;
    }

    @Override
    protected Object getDefaultValue() {
        return "0";
    }

    @Override
    protected Integer convert(Object o, UUID uuid) {
        String parsed = StringReplacerApplier.replace(String.valueOf(o).trim(), uuid, this);
        return Validate.getNumber(parsed).map(BigDecimal::intValue).orElseGet(() -> {
            Optional.ofNullable(Bukkit.getPlayer(uuid)).ifPresent(player -> MessageUtils.sendMessage(player, BetterGUI.getInstance().get(MessageConfig.class).getInvalidNumber(parsed)));
            return 0;
        });
    }

    @Override
    protected Result checkConverted(UUID uuid, Integer value) {
        if (value > 0 && !PlayerPointsHook.hasPoints(uuid, value)) {
            return Result.fail();
        } else {
            return successConditional((uuid1, process) -> SchedulerUtil.global().run(() -> {
                if (!PlayerPointsHook.takePoints(uuid1, value)) {
                    Optional.ofNullable(Bukkit.getPlayer(uuid)).ifPresent(player -> player.sendMessage(ChatColor.RED + "Error: the transaction couldn't be executed. Please inform the staff."));
                }
                process.next();
            }));
        }
    }
}
