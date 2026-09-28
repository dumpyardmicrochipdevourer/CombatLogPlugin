package com.antonk.combatlogplugin.listeners;

import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import com.antonk.combatlogplugin.combat.CombatManager;

public class PvpListener implements Listener {
    private final CombatManager combatManager;

    public PvpListener(CombatManager combatManager) {
        this.combatManager = combatManager;
    }

    @EventHandler(ignoreCancelled = true)
    public void onHit(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player victim)) {
            return;
        }
        Player attacker = resolveAttacker(event.getDamager());
        if (attacker == null || attacker.equals(victim)) {
            return;
        }
        combatManager.createDuel(attacker, victim);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        Player killer = victim.getKiller();
        combatManager.onPlayerDeath(killer, victim);
    }

    private Player resolveAttacker(Entity damager) {
        if (damager instanceof Player player) {
            return player;
        }
        if (damager instanceof AbstractArrow arrow && arrow.getShooter() instanceof Player shooter) {
            return shooter;
        }
        return null;
    }
}
