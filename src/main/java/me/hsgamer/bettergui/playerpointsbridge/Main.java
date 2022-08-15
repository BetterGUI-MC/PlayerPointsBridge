package me.hsgamer.bettergui.playerpointsbridge;

import me.hsgamer.bettergui.builder.ActionBuilder;
import me.hsgamer.bettergui.builder.RequirementBuilder;
import me.hsgamer.hscore.bukkit.addon.PluginAddon;
import me.hsgamer.hscore.variable.VariableManager;

public final class Main extends PluginAddon {

    @Override
    public void onEnable() {
        PlayerPointsHook.setupPlugin();
        RequirementBuilder.INSTANCE.register(PointRequirement::new, "point");
        ActionBuilder.INSTANCE.register(GivePointsAction::new, "give-point");
        VariableManager.register("points", (original, uuid) -> String.valueOf(PlayerPointsHook.getPoints(uuid)));
    }
}
