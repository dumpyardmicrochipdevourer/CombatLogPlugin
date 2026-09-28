package com.antonk.combatlogplugin.combat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.GameMode;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.antonk.combatlogplugin.combat.bossbar.BossbarHandler;

public class CombatManager {
    private final CombatSettings settings;
    private final BossbarHandler barHandler;
    private final Map<UUID, Duel> combatants = new HashMap<>();

    public CombatManager(Plugin plugin, CombatSettings settings) {
        this.settings = settings;
        barHandler = new BossbarHandler(
                plugin,
                settings.countdown(),
                settings.bossbarTitle(),
                expired -> combatants.remove(expired.getUniqueId())
        );
    }

    public CombatSettings getSettings() {
        return settings;
    }

    public boolean createDuel(Player attacker, Player victim) {
        if (isInCreative(attacker) || isInCreative(victim)) {
            return false;
        }
        putCombatLog(attacker, victim);
        return true;
    }

    public void putCombatLog(Player attacker, Player victim) {
        tag(attacker, victim);
        if (!attacker.equals(victim)) {
            tag(victim, attacker);
        }
    }

    public void onPlayerDeath(Player killer, Player victim) {
        Duel duel = combatants.get(victim.getUniqueId());
        if (duel == null) return;

        Player winner = killer != null && !killer.equals(victim) && isInCombat(killer)
                ? killer
                : duel.getOpponent(victim);
        removeCombatLog(victim);
        if (winner != null && !winner.equals(victim)) {
            announceWinner(winner, victim);
        }
    }

    public void onPlayerQuit(Player leaver) {
        Duel duel = combatants.get(leaver.getUniqueId());
        if (duel == null) return;

        Player winner = duel.getOpponent(leaver);
        // Бой снимаем до смерти, иначе onPlayerDeath объявит победителя второй раз
        removeCombatLog(leaver);
        leaver.setHealth(0);
        if (winner != null && !winner.equals(leaver)) {
            announceWinner(winner, leaver);
        }
    }

    public Duel getDuel(Player player) {
        return combatants.get(player.getUniqueId());
    }

    public boolean isInCombat(Player player) {
        return combatants.containsKey(player.getUniqueId());
    }

    public void removeCombatLog(Player p) {
        end(p);
        for (Duel duel : new ArrayList<>(combatants.values())) {
            if (duel.getPlayer2().equals(p)) {
                end(duel.getPlayer1());
            }
        }
    }

    public void setCountdown(int countdown) {
        barHandler.setCountdown(countdown);
    }

    public void shutdown() {
        barHandler.removeAll();
        combatants.clear();
    }

    private void tag(Player player, Player opponent) {
        combatants.put(player.getUniqueId(), new Duel(player, opponent));
        barHandler.putCombatLog(player);
    }

    private void end(Player player) {
        combatants.remove(player.getUniqueId());
        barHandler.removeCombatLog(player);
    }

    private void announceWinner(Player winner, Player loser) {
        winner.sendMessage(settings.winMessage().replace("{player}", loser.getName()));
        winner.getWorld().spawnParticle(
                Particle.TOTEM_OF_UNDYING,
                winner.getLocation().add(0, 1, 0),
                50);

        if (loser.isOnline()) {
            loser.sendMessage(settings.loseMessage().replace("{player}", winner.getName()));
        }
    }

    private boolean isInCreative(Player p) {
        return p.getGameMode().equals(GameMode.CREATIVE);
    }
}
