package me.hsgamer.bettergui.playerpointsbridge;

import me.hsgamer.bettergui.builder.ActionBuilder;
import me.hsgamer.bettergui.builder.RequirementBuilder;
import me.hsgamer.hscore.common.StringReplacer;
import me.hsgamer.hscore.expansion.common.Expansion;
import me.hsgamer.hscore.variable.VariableBundle;

public final class Main implements Expansion {
    private final VariableBundle variableBundle = new VariableBundle();

    @Override
    public void onEnable() {
        PlayerPointsHook.setupPlugin();
        RequirementBuilder.INSTANCE.register(PointRequirement::new, "point");
        ActionBuilder.INSTANCE.register(GivePointsAction::new, "give-point");
        variableBundle.register("points", StringReplacer.of((original, uuid) -> String.valueOf(PlayerPointsHook.getPoints(uuid))));
    }

    @Override
    public void onDisable() {
        variableBundle.unregisterAll();
    }
}
