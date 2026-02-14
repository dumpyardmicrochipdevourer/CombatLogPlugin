package com.antonk.combatlogplugin.commands;

import com.antonk.combatlogplugin.combat.CombatManager;
import com.antonk.combatlogplugin.commands.impl.CombatLogCommand;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.plugin.Plugin;

public class CommandManager {
    private final Plugin plugin;
    private final CombatManager combatManager;

    public CommandManager(Plugin plugin, CombatManager combatManager) {
        this.plugin = plugin;
        this.combatManager = combatManager;
    }

    public void registerAll() {
        plugin.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            CombatLogCommand combatLogCommand = new CombatLogCommand(combatManager);
            event.registrar().register(combatLogCommand.createCommandNode());
        });
    }
}
