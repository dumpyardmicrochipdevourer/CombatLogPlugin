package com.antonk.combatlogplugin.listeners.punishers;

import java.util.Locale;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import com.antonk.combatlogplugin.combat.CombatManager;

public class PreCommandListener implements Listener {
    private final CombatManager combatManager;

    public PreCommandListener(CombatManager combatManager) {
        this.combatManager = combatManager;
    }

    @EventHandler(ignoreCancelled = true)
    public void onCommand(PlayerCommandPreprocessEvent event) {
        Player p = event.getPlayer();

        if (!combatManager.isInCombat(p) || p.hasPermission("combatlog.bypass")) {
            return;
        }
        if (combatManager.getSettings().allowedCommands().contains(commandName(event.getMessage()))) {
            return;
        }
        event.setCancelled(true);
        p.sendMessage(combatManager.getSettings().commandBlockedMessage());
    }

    private String commandName(String message) {
        return message.substring(1).split(" ", 2)[0].toLowerCase(Locale.ROOT);
    }
}
