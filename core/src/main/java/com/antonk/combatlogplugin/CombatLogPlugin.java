package com.antonk.combatlogplugin;

import com.antonk.combatlogplugin.api.CombatApi;
import com.antonk.combatlogplugin.api.impl.CombatApiImpl;
import com.antonk.combatlogplugin.combat.CombatManager;
import com.antonk.combatlogplugin.combat.CombatSettings;
import com.antonk.combatlogplugin.commands.CommandManager;
import com.antonk.combatlogplugin.listeners.PvpListener;
import com.antonk.combatlogplugin.listeners.punishers.ExitListener;
import com.antonk.combatlogplugin.listeners.punishers.PreCommandListener;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

public final class CombatLogPlugin extends JavaPlugin {

    private CombatManager combatManager;

    @Override
    public void onEnable() {
        getLogger().info("CombatLog is starting...");
        saveDefaultConfig();
        combatManager = new CombatManager(this, CombatSettings.load(getConfig()));
        getServer().getPluginManager().registerEvents(
                new PvpListener(combatManager),this
        );
        getServer().getPluginManager().registerEvents(
                new ExitListener(combatManager),this
        );
        getServer().getPluginManager().registerEvents(
                new PreCommandListener(combatManager),this
        );
        new CommandManager(this,combatManager).registerAll();
        getServer().getServicesManager().register(
                CombatApi.class, new CombatApiImpl(combatManager), this, ServicePriority.Normal
        );
        getLogger().info("CombatLogPlugin successfully started!");
    }

    @Override
    public void onDisable() {
        if (combatManager != null) {
            combatManager.shutdown();
        }
        getLogger().info("CombatLog stopped!");
    }
}
