package com.antonk.combatlogplugin.listeners.punishers;

import com.antonk.combatlogplugin.combat.CombatManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class ExitListener implements Listener {
    private final CombatManager combatManager;

    public ExitListener(CombatManager combatManager) {
        this.combatManager = combatManager;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player leaver = event.getPlayer();
        if (combatManager.isInCombat(leaver)) {
            combatManager.onPlayerQuit(leaver);
        }
    }
}
