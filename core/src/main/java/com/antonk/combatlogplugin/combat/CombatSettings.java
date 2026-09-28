package com.antonk.combatlogplugin.combat;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public record CombatSettings(
        int countdown,
        String bossbarTitle,
        String winMessage,
        String loseMessage,
        String commandBlockedMessage,
        Set<String> allowedCommands
) {

    public static CombatSettings load(FileConfiguration config) {
        Set<String> allowed = new HashSet<>();
        for (String command : config.getStringList("allowed-commands")) {
            allowed.add(command.toLowerCase(Locale.ROOT));
        }
        return new CombatSettings(
                Math.max(1, config.getInt("countdown", 60)),
                config.getString("messages.bossbar-title", "CombatLog: {time}s"),
                config.getString("messages.win", "You killed {player}"),
                config.getString("messages.lose", "You were killed by {player}"),
                config.getString("messages.command-blocked", "You cannot use commands during combat!"),
                allowed
        );
    }
}
