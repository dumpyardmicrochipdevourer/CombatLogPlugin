package com.antonk.combatlogplugin.listeners.punishers;

import com.antonk.combatlogplugin.combat.CombatManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class PreCommandListener implements Listener {
    private final CombatManager combatManager;

    public PreCommandListener(CombatManager combatManager) {
        this.combatManager = combatManager;
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        Player p = event.getPlayer();

        if (combatManager.isInCombat(p)) {
            event.setCancelled(true);
            p.sendMessage("Нельзя использовать команды во время CombatLog!");
        }
    }
}
