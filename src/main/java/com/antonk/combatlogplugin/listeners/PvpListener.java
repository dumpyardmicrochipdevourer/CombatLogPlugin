package com.antonk.combatlogplugin.listeners;

import com.antonk.combatlogplugin.combat.CombatManager;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

public class PvpListener implements Listener {
    private final CombatManager combatManager;

    public PvpListener(CombatManager combatManager) {
        this.combatManager = combatManager;
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player victim)) {
            return;
        }
        if(!(event.getEntity() instanceof Player attacker)) {
            return;
        }
        combatManager.createDuel(attacker,victim);
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        Projectile proj = event.getEntity();
        if (!(event.getHitEntity() instanceof Player victim)) {
            return;
        }
        if (!(proj.getShooter() instanceof Player attacker)) {
            return;
        }
        if (!(event.getEntity() instanceof AbstractArrow arrow)) {
            return;
        }
        combatManager.createDuel(attacker,victim);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        Player killer = victim.getKiller();
        combatManager.onPlayerDeath(killer,victim);
    }
}
