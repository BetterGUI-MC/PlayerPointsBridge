package me.hsgamer.bettergui.playerpointsbridge;

import me.hsgamer.bettergui.api.requirement.TakableRequirement;
import me.hsgamer.bettergui.config.MessageConfig;
import me.hsgamer.bettergui.lib.core.bukkit.utils.MessageUtils;
import me.hsgamer.bettergui.lib.core.expression.ExpressionUtils;
import me.hsgamer.bettergui.lib.core.variable.VariableManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class PointRequirement extends TakableRequirement<Integer> {

    private final Map<UUID, Integer> checked = new HashMap<>();

    public PointRequirement(String name) {
        super(name);
        VariableManager.register(name, (original, uuid) -> {
            int points = getParsedValue(uuid);
            if (points > 0 && !PlayerPointsHook.hasPoints(uuid, points)) {
                return String.valueOf(points);
            }
            return MessageConfig.HAVE_MET_REQUIREMENT_PLACEHOLDER.getValue();
        });
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
    protected void takeChecked(UUID uuid) {
        if (!PlayerPointsHook.takePoints(uuid, checked.remove(uuid))) {
            Optional.ofNullable(Bukkit.getPlayer(uuid)).ifPresent(player -> player.sendMessage(ChatColor.RED + "Error: the transaction couldn't be executed. Please inform the staff."));
        }
    }

    @Override
    public Integer getParsedValue(UUID uuid) {
        String parsed = VariableManager.setVariables(String.valueOf(value).trim(), uuid);
        return Optional.ofNullable(ExpressionUtils.getResult(parsed)).map(BigDecimal::intValue).orElseGet(() -> {
            Optional.ofNullable(Bukkit.getPlayer(uuid)).ifPresent(player -> MessageUtils.sendMessage(player, MessageConfig.INVALID_NUMBER.getValue().replace("{input}", parsed)));
            return 0;
        });
    }

    @Override
    public boolean check(UUID uuid) {
        int points = getParsedValue(uuid);
        if (points > 0 && !PlayerPointsHook.hasPoints(uuid, points)) {
            return false;
        } else {
            checked.put(uuid, points);
            return true;
        }
    }
}
