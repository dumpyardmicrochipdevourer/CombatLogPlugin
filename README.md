# ⚔️ CombatLog

**A lightweight Paper plugin that tags players in PvP, shows a combat countdown and punishes logging out mid-fight**

![Minecraft 1.21.11](https://img.shields.io/badge/Minecraft-1.21.11-green.svg)
![Paper Required](https://img.shields.io/badge/Paper-Required-blue.svg)
![Java 21](https://img.shields.io/badge/Java-21-orange.svg)

---

![Screenshot](.github/assets/bossbar-red.png)
![Screenshot](.github/assets/bossbar-yellow.png)
![Screenshot](.github/assets/bossbar-green.png)

*Combat boss bar: red at the start, yellow after half of the time, green for the last 10 seconds*

---

## ✨ Features

- ⚔️ **PvP tagging** - Melee hits and arrows/tridents put both players in combat
- ⏱️ **Boss bar countdown** - Every new hit resets the timer
- 🚪 **Logout punishment** - Leaving the server in combat kills the player, the opponent gets the kill
- 🚫 **Command blocking** - Commands are blocked in combat, with a whitelist and a bypass permission
- 👥 **Multiple opponents** - Hitting a third player tags them too, each player has their own timer
- 🎨 **Creative ignored** - Players in creative mode never enter combat
- ⚙️ **Configurable** - Countdown, messages and allowed commands in `config.yml`
- 🔌 **Developer API** - Check or control combat from other plugins

## 📦 Installation

1. Download `core-X.X.X.jar` from [Releases](../../releases) (not `original-core-X.X.X.jar`)
2. Place in your `plugins/` folder
3. Restart server
4. Optionally edit `plugins/CombatLogPlugin/config.yml`

**Requirements:** Paper 1.21.11 and Java 21+

## ⚙️ Configuration

```yaml
# Combat duration in seconds after the last hit
countdown: 60

# Commands allowed in combat (without /)
allowed-commands: []

messages:
  bossbar-title: "CombatLog: {time}s"
  win: "You killed {player}"
  lose: "You were killed by {player}"
  command-blocked: "You cannot use commands during combat!"
```

`win` is sent to the player who got the kill, `lose` to the one who died (including a player who logged out in combat).
`{time}` and `{player}` are replaced automatically. Commands are compared by exactly what the player typed,
so `msg` does not cover `/tell` or `/minecraft:msg` - add every variant you want to allow.

## 🎮 Commands

| Command | Description | Permission |
|---------|-------------|------------|
| `/combatlog test` | Put yourself in combat | `combatlog.admin` |
| `/combatlog put <player>` | Put a player in combat | `combatlog.admin` |
| `/combatlog remove <player>` | Take a player out of combat | `combatlog.admin` |

## 🔑 Permissions

| Permission | Description | Default |
|------------|-------------|---------|
| `combatlog.admin` | Use `/combatlog` | op |
| `combatlog.bypass` | Commands are not blocked in combat | op |

## 🔌 Developer API

The plugin registers `CombatApi` in Bukkit's `ServicesManager`:

```java
CombatApi api = getServer().getServicesManager().load(CombatApi.class);

if (api != null && api.isInCombat(player)) {
    Player opponent = api.getOpponent(player); // null if not in combat
}
```

| Method | Description |
|--------|-------------|
| `isInCombat(Player)` | Is the player in combat |
| `startCombat(Player, Player)` | Put two players in combat |
| `endCombat(Player)` | End combat for a player and for those whose last opponent they are |
| `getOpponent(Player)` | Last opponent of the player, or `null` |
| `setCountdown(int)` | Change the countdown for timers started from now on |

Depend on the `api` module with `provided` scope and add `depend: [CombatLogPlugin]` to your `plugin.yml`.

## 🔨 Building

```bash
mvn clean package
```

The plugin jar is `core/target/core-X.X.X.jar`, the `api` module is shaded into it.
