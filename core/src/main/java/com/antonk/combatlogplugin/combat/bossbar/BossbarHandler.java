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
import java.util.UUID;
import java.util.function.Consumer;

public class BossbarHandler {
    private final Plugin plugin;
    private final String titleFormat;
    private final Consumer<Player> onExpire;
    private int countdown;
    private final Map<UUID, BossBar> activeBossBars = new HashMap<>();
    private final Map<UUID, BukkitTask> activeTasks = new HashMap<>();

    public BossbarHandler(Plugin plugin, int countdown, String titleFormat, Consumer<Player> onExpire) {
        this.plugin = plugin;
        this.countdown = countdown;
        this.titleFormat = titleFormat;
        this.onExpire = onExpire;
    }

    public void putCombatLog(Player p) {
        if (activeBossBars.containsKey(p.getUniqueId())) {
            resetTimer(p);
            return;
        }
        BossBar bossBar = Bukkit.createBossBar(
                formatTitle(countdown),
                BarColor.RED,
                BarStyle.SOLID
        );

        bossBar.addPlayer(p);
        activeBossBars.put(p.getUniqueId(), bossBar);
        startTimer(p, countdown);
    }

    private void startTimer(Player p, int seconds) {
        BukkitTask task = new BukkitRunnable() {
            int remaining = seconds;

            @Override
            public void run() {
                BossBar bossBar = activeBossBars.get(p.getUniqueId());
                if (bossBar == null) {
                    cancel();
                    return;
                }
                if (remaining <= 0) {
                    removeCombatLog(p);
                    onExpire.accept(p);
                    return;
                }
                bossBar.setTitle(formatTitle(remaining));
                bossBar.setProgress(remaining / (double) seconds);

                if (remaining <= 10) {
                    bossBar.setColor(BarColor.GREEN);
                } else if (remaining <= seconds / 2) {
                    bossBar.setColor(BarColor.YELLOW);
                } else {
                    bossBar.setColor(BarColor.RED);
                }

                remaining--;
            }
        }.runTaskTimer(plugin, 0L, 20L);
        activeTasks.put(p.getUniqueId(), task);
    }

    public void setCountdown(int countdown) {
        this.countdown = countdown;
    }

    public void resetTimer(Player p) {
        BukkitTask task = activeTasks.get(p.getUniqueId());
        if (task != null) {
            task.cancel();
        }
        startTimer(p, countdown);
    }

    public void removeCombatLog(Player p) {
        BossBar bossBar = activeBossBars.remove(p.getUniqueId());
        if (bossBar != null) {
            bossBar.removeAll();
        }
        BukkitTask task = activeTasks.remove(p.getUniqueId());
        if (task != null) {
            task.cancel();
        }
    }

    public void removeAll() {
        activeBossBars.values().forEach(BossBar::removeAll);
        activeBossBars.clear();
        activeTasks.values().forEach(BukkitTask::cancel);
        activeTasks.clear();
    }

    private String formatTitle(int seconds) {
        return titleFormat.replace("{time}", String.valueOf(seconds));
    }
}
