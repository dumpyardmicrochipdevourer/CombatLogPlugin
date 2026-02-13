package com.antonk.combatlogplugin.combat;

import com.antonk.combatlogplugin.combat.bossbar.BossbarHandler;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

public class CombatManager {
    private final BossbarHandler barHandler;
    private final Map<Player,Duel> activeDuels = new HashMap<>();

    public CombatManager(Plugin plugin, int countdown) {
        barHandler = new BossbarHandler(plugin,countdown);
    }

    public void createDuel(Player attacker, Player victim) {
        Duel existingDuel = activeDuels.get(attacker);
        if (existingDuel == null) {
            existingDuel = activeDuels.get(victim);
        }
        if (existingDuel != null) {
            barHandler.putCombatLog(attacker);
            barHandler.putCombatLog(victim);
        }

        Duel duel = new Duel(attacker,victim);
        activeDuels.put(attacker, duel);
        activeDuels.put(victim,duel);

        barHandler.putCombatLog(attacker);
        barHandler.putCombatLog(victim);
    }

    public Map<Player, Duel> getActiveDuels() {
        return activeDuels;
    }

    public void onPlayerDeath(Player killer, Player victim) {
        Duel duel = getActiveDuels().get(victim);
        if (duel == null) return;

        if (killer != null && duel.involves(killer)) {
            announceWinner(killer, victim);
        } else {
            Player opponent = duel.getOpponent(victim);
            announceWinner(opponent,victim);
        }
        endDuel(duel);
    }

    public void onPlayerQuit(Player leaver) {
        Duel duel = getActiveDuels().get(leaver);
        if (duel == null) return;

        leaver.setHealth(0);
        Player winner = duel.getOpponent(leaver);
        if (winner != null) {
            announceWinner(winner, leaver);
        }
        endDuel(duel);
    }

    public Duel getDuel(Player player) {
        return activeDuels.get(player);
    }

    public boolean isInCombat(Player player) {
        return activeDuels.containsKey(player);
    }

    private void announceWinner(Player winner, Player loser) {
        winner.sendMessage(String.format("Ты победил игрока %s", loser.getName()));
        winner.getWorld().spawnParticle(
                Particle.TOTEM_OF_UNDYING,
                winner.getLocation().add(0, 1, 0),
                50);

        if (loser.isOnline()) {
            loser.sendMessage(String.format("Вас победил игрок %s", winner.getName()));
        }
    }

    private void endDuel(Duel duel) {
        activeDuels.remove(duel.getPlayer1());
        activeDuels.remove(duel.getPlayer2());

        barHandler.removeCombatLog(duel.getPlayer1());
        barHandler.removeCombatLog(duel.getPlayer2());
    }
}
