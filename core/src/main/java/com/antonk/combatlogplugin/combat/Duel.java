package com.antonk.combatlogplugin.combat;

import org.bukkit.entity.Player;

public class Duel {
    private final Player player1;
    private final Player player2;

    public Duel(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    public Player getOpponent(Player p) {
        if (p.equals(player1)) return player2;
        if (p.equals(player2)) return player1;
        return null;
    }

    public boolean involves(Player p) {
        return p.equals(player1) || p.equals(player2);
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }
}
