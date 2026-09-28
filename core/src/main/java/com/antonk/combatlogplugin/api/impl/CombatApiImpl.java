package com.antonk.combatlogplugin.api.impl;

import com.antonk.combatlogplugin.api.CombatApi;
import com.antonk.combatlogplugin.combat.CombatManager;
import com.antonk.combatlogplugin.combat.Duel;
import org.bukkit.entity.Player;

public class CombatApiImpl implements CombatApi {
    private final CombatManager combatManager;

    public CombatApiImpl(CombatManager combatManager) {
        this.combatManager = combatManager;
    }

    public boolean isInCombat(Player player) {
        return combatManager.isInCombat(player);
    }

    public void startCombat(Player player1, Player player2) {
        combatManager.createDuel(player1,player2);
    }
    public void endCombat(Player player) {
        combatManager.removeCombatLog(player);
    }
    public Player getOpponent(Player player) {
        Duel duel = combatManager.getDuel(player);
        return duel == null ? null : duel.getOpponent(player);
    }

    @Override
    public void setCountdown(int countdown) {
        combatManager.setCountdown(countdown);
    }
}
