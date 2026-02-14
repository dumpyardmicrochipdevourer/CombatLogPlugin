package com.antonk.combatlogplugin;

import com.antonk.combatlogplugin.api.CombatApi;
import com.antonk.combatlogplugin.api.impl.CombatApiImpl;
import com.antonk.combatlogplugin.combat.CombatManager;
import com.antonk.combatlogplugin.commands.CommandManager;
import com.antonk.combatlogplugin.listeners.PvpListener;
import com.antonk.combatlogplugin.listeners.punishers.ExitListener;
import com.antonk.combatlogplugin.listeners.punishers.PreCommandListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class CombatLogPlugin extends JavaPlugin {

    CombatManager combatManager = new CombatManager(this,60);
    private static CombatApi api;

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
        new CommandManager(this,combatManager).registerAll();
        api = new CombatApiImpl(combatManager);
        getLogger().info("CombatLogPlugin successfully started!");
    }

    @Override
    public void onDisable() {
        getLogger().info("CombatLog stopped!");
    }
}
