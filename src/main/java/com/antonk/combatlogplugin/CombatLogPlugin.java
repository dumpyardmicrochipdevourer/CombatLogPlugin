package com.antonk.combatlogplugin;

import com.antonk.combatlogplugin.combat.CombatManager;
import com.antonk.combatlogplugin.listeners.PvpListener;
import com.antonk.combatlogplugin.listeners.punishers.ExitListener;
import com.antonk.combatlogplugin.listeners.punishers.PreCommandListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class CombatLogPlugin extends JavaPlugin {
    CombatManager combatManager = new CombatManager(this,60);

    @Override
    public void onEnable() {
        getLogger().info("CombatLog is starting...");
        getServer().getPluginManager().registerEvents(
                new PvpListener(combatManager),this
        );
        getServer().getPluginManager().registerEvents(
                new ExitListener(combatManager),this
        );
        getServer().getPluginManager().registerEvents(
                new PreCommandListener(combatManager),this
        );
        getLogger().info("CombatLogPlugin successfully started!");
    }

    @Override
    public void onDisable() {
        getLogger().info("CombatLog stopped!");
    }
}
