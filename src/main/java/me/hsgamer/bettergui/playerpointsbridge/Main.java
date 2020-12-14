package me.hsgamer.bettergui.playerpointsbridge;

import me.hsgamer.bettergui.api.addon.BetterGUIAddon;
import me.hsgamer.bettergui.builder.ActionBuilder;
import me.hsgamer.bettergui.builder.RequirementBuilder;
import me.hsgamer.bettergui.lib.core.variable.VariableManager;

public final class Main extends BetterGUIAddon {

    @Override
    public void onEnable() {
        PlayerPointsHook.setupPlugin();
        RequirementBuilder.INSTANCE.register(PointRequirement::new, "point");
        ActionBuilder.INSTANCE.register(GivePointsAction::new, "give-point");
        VariableManager.register("points", (original, uuid) -> String.valueOf(PlayerPointsHook.getPoints(uuid)));
    }
}
