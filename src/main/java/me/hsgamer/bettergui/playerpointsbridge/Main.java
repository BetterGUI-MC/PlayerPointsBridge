package me.hsgamer.bettergui.playerpointsbridge;

import me.hsgamer.bettergui.builder.CommandBuilder;
import me.hsgamer.bettergui.builder.RequirementBuilder;
import me.hsgamer.bettergui.manager.VariableManager;
import me.hsgamer.bettergui.object.addon.Addon;

public final class Main extends Addon {

  @Override
  public void onEnable() {
    PlayerPointsHook.setupPlugin();
    RequirementBuilder.register(PointIconRequirement::new, "point");
    CommandBuilder.register(GivePointsCommand::new, "give-point:");
    VariableManager.register("points", (player, s) -> String
        .valueOf(PlayerPointsHook.getPoints(player)));
  }
}
