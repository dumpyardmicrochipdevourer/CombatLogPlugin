package com.antonk.combatlogplugin.commands.impl;

import com.antonk.combatlogplugin.combat.CombatManager;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import org.bukkit.entity.Player;

import static io.papermc.paper.command.brigadier.Commands.argument;
import static io.papermc.paper.command.brigadier.Commands.literal;

public class CombatLogCommand {
    private final CombatManager combatManager;

    public CombatLogCommand(CombatManager combatManager) {
        this.combatManager = combatManager;
    }

    public LiteralCommandNode<CommandSourceStack> createCommandNode() {
        LiteralArgumentBuilder<CommandSourceStack> root = literal("combatlog")
                .then(literal("test")
                        .executes(ctx -> {
                            CommandSourceStack source = ctx.getSource();
                            if (source.getExecutor() instanceof Player player) {
                                combatManager.putCombatLog(player, player);
                            } else {
                                source.getSender().sendPlainMessage("Эту команду может выполнить только игрок!");
                            } return 1;
                        }))
                .then(literal("put")
                        .then(argument("player", ArgumentTypes.player())
                                .executes(ctx -> {
                                    Player target = ctx.getArgument("player", PlayerSelectorArgumentResolver.class)
                                            .resolve(ctx.getSource())
                                            .getFirst();
                                    boolean success = combatManager.createDuel(target, target);
                                    if (success) {
                                        ctx.getSource().getSender().sendPlainMessage("CombatLogged " + target.getName());
                                    } else {
                                        ctx.getSource().getSender().sendPlainMessage(String.format("Ошибка! %s в креативе",target.getName()));
                                    }
                                    combatManager.createDuel(target,target);
                                    return success ? 1 : 0;
                                })
                        ))
                .then(literal("remove")
                        .then(argument("player", ArgumentTypes.player())
                                .executes(ctx -> {
                                    Player target = ctx.getArgument("player", PlayerSelectorArgumentResolver.class)
                                            .resolve(ctx.getSource())
                                            .getFirst();
                                    if (combatManager.isInCombat(target)) {
                                        combatManager.removeCombatLog(target);
                                        ctx.getSource().getSender().sendPlainMessage("CombatLog снят с " + target.getName());
                                    } else {
                                        ctx.getSource().getSender().sendPlainMessage("Игрок не в CombatLog'е!");
                                    } return 1;
                                })
                        ));
        return root.build();
    }
}
