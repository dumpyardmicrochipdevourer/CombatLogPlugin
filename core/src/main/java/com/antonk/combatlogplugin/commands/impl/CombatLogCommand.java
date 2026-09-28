package com.antonk.combatlogplugin.commands.impl;

import java.util.List;

import org.bukkit.entity.Player;

import com.antonk.combatlogplugin.combat.CombatManager;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import static io.papermc.paper.command.brigadier.Commands.argument;
import static io.papermc.paper.command.brigadier.Commands.literal;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;

public class CombatLogCommand {
    private static final String ADMIN_PERMISSION = "combatlog.admin";

    private final CombatManager combatManager;

    public CombatLogCommand(CombatManager combatManager) {
        this.combatManager = combatManager;
    }

    public LiteralCommandNode<CommandSourceStack> createCommandNode() {
        LiteralArgumentBuilder<CommandSourceStack> root = literal("combatlog")
                .requires(source -> source.getSender().hasPermission(ADMIN_PERMISSION))
                .then(literal("test")
                        .executes(ctx -> {
                            CommandSourceStack source = ctx.getSource();
                            if (source.getExecutor() instanceof Player player) {
                                combatManager.putCombatLog(player, player);
                            } else {
                                source.getSender().sendPlainMessage("Only players can run this command!");
                            } return 1;
                        }))
                .then(literal("put")
                        .then(argument("player", ArgumentTypes.player())
                                .executes(ctx -> {
                                    Player target = resolveTarget(ctx);
                                    if (target == null) {
                                        return 0;
                                    }
                                    boolean success = combatManager.createDuel(target, target);
                                    if (success) {
                                        ctx.getSource().getSender().sendPlainMessage("CombatLogged " + target.getName());
                                    } else {
                                        ctx.getSource().getSender().sendPlainMessage(String.format("Error! %s is in creative mode",target.getName()));
                                    }
                                    return success ? 1 : 0;
                                })
                        ))
                .then(literal("remove")
                        .then(argument("player", ArgumentTypes.player())
                                .executes(ctx -> {
                                    Player target = resolveTarget(ctx);
                                    if (target == null) {
                                        return 0;
                                    }
                                    if (combatManager.isInCombat(target)) {
                                        combatManager.removeCombatLog(target);
                                        ctx.getSource().getSender().sendPlainMessage("Combat removed from " + target.getName());
                                    } else {
                                        ctx.getSource().getSender().sendPlainMessage("Player is not in combat!");
                                    } return 1;
                                })
                        ));
        return root.build();
    }

    private Player resolveTarget(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        List<Player> players = ctx.getArgument("player", PlayerSelectorArgumentResolver.class)
                .resolve(ctx.getSource());
        if (players.isEmpty()) {
            ctx.getSource().getSender().sendPlainMessage("Player not found!");
            return null;
        }
        return players.getFirst();
    }
}
