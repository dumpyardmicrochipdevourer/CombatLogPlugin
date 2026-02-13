package com.antonk.combatlogplugin.combat.bossbar;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;

public class BossbarHandler {
    private Plugin plugin;
    private int countdown;
    private final Map<Player, BossBar> activeBossBars = new HashMap<>();
    private final Map<Player,BukkitTask> activeTasks = new HashMap<>();


    public BossbarHandler(Plugin plugin, int countdown) {
        this.plugin = plugin;
        this.countdown = countdown;
    }

    public void putCombatLog(Player p) {
        if (activeBossBars.containsKey(p)) {
            resetTimer(p);
            return;
        }
        BossBar bossBar = Bukkit.createBossBar(
                String.format("CombatLog: %s seconds",countdown),
                BarColor.RED,
                BarStyle.SOLID
        );

        bossBar.addPlayer(p);
        activeBossBars.put(p,bossBar);
        startTimer(p,countdown);
    }

    private void startTimer(Player p, int seconds) {
        BukkitTask task = new BukkitRunnable() {
            int countdown = seconds;

            @Override
            public void run() {
                BossBar bossBar = activeBossBars.get(p);
                if (bossBar == null || countdown <= 0) {
                    removeCombatLog(p);
                    cancel();
                    return;
                }
                bossBar.setTitle("Combat: " + countdown + "s");
                bossBar.setProgress(countdown / (double) seconds);

                if (countdown <= 30) {
                    bossBar.setColor(BarColor.YELLOW);
                } else if (countdown <= 10) {
                    bossBar.setColor(BarColor.GREEN);
                }

                countdown --;
                }
            }.runTaskTimer(plugin, 0L, 20L);
        activeTasks.put(p, task);
    }

    public void setCountdown(int countdown) {
        this.countdown = countdown;
    }

    private void resetTimer(Player p) {
        BukkitTask task = activeTasks.get(p);
        if (task != null) {
            task.cancel();
        }
        startTimer(p,countdown);
    }

    public void removeCombatLog(Player p) {
        BossBar bossBar = activeBossBars.remove(p);
        bossBar.removeAll();
        BukkitTask task = activeTasks.remove(p);
        task.cancel();
    }
}
