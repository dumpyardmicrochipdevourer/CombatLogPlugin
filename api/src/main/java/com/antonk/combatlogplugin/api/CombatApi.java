package com.antonk.combatlogplugin.api;

import org.bukkit.entity.Player;

public interface CombatApi {
    boolean isInCombat(Player player);
    void startCombat(Player player1, Player player2);
    void endCombat(Player player);
    Player getOpponent(Player player);
    void setCountdown(int countdown);
}
