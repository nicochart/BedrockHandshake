package fr.factionbedrock.bedrockhandshake.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import fr.factionbedrock.bedrockhandshake.BedrockHandshake;
import fr.factionbedrock.bedrockhandshake.config.BedrockHandshakeConfigLoader;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public final class BedrockHandshakeCommands
{
    public static void register() {CommandRegistrationCallback.EVENT.register(BedrockHandshakeCommands::registerImpl);}

    private static void registerImpl(CommandDispatcher<ServerCommandSource> dispatcher, net.minecraft.registry.RegistryWrapper.WrapperLookup registryAccess, CommandManager.RegistrationEnvironment env) {
        dispatcher.register(literal("bedrockhandshake")
                .requires(source -> source.hasPermissionLevel(2))

                .then(literal("doHandshakeOnIntegratedServer")
                        .then(argument("value", BoolArgumentType.bool())
                                .executes(ctx -> {
                                    boolean value = BoolArgumentType.getBool(ctx, "value");
                                    BedrockHandshakeConfigLoader.updateDoHandshakeOnIntegratedServer(value);
                                    ctx.getSource().sendFeedback(() -> Text.literal("doHandshakeOnIntegratedServer set to " + value), true);
                                    return 1;
                                })
                        )
                )
                .then(literal("toleratedInfractionCount")
                        .then(argument("value", IntegerArgumentType.integer(0))
                                .executes(ctx -> {
                                    int value = IntegerArgumentType.getInteger(ctx, "value");
                                    BedrockHandshakeConfigLoader.updateToleratedInfractionCount(value);
                                    ctx.getSource().sendFeedback(() -> Text.literal("toleratedInfractionCount set to " + value), true);
                                    return 1;
                                })
                        )
                )
                .then(literal("white-list")
                        .then(literal("mods")
                                .then(literal("add")
                                        .then(argument("value", StringArgumentType.word())
                                                .executes(ctx -> {
                                                    String mod = StringArgumentType.getString(ctx, "value");

                                                    List<String> list = new ArrayList<>(BedrockHandshake.MODS_WHITELIST);
                                                    list.add(mod);

                                                    BedrockHandshakeConfigLoader.updateModsWhitelist(list);

                                                    ctx.getSource().sendFeedback(() -> Text.literal("Added mod to whitelist: " + mod), true);
                                                    return 1;
                                                })
                                        )
                                )
                        )
                        .then(literal("packs")
                                .then(literal("add")
                                        .then(argument("value", StringArgumentType.word())
                                                .executes(ctx -> {
                                                    String pack = StringArgumentType.getString(ctx, "value");

                                                    List<String> list = new ArrayList<>(BedrockHandshake.PACKS_WHITELIST);
                                                    list.add(pack);

                                                    BedrockHandshakeConfigLoader.updatePacksWhitelist(list);

                                                    ctx.getSource().sendFeedback(() -> Text.literal("Added pack to whitelist: " + pack), true);
                                                    return 1;
                                                })
                                        )
                                )
                        )
                        .then(literal("players")
                                .then(literal("add")
                                        .then(argument("value", StringArgumentType.word())
                                                .executes(ctx -> {
                                                    String player = StringArgumentType.getString(ctx, "value");

                                                    List<String> list = new ArrayList<>(BedrockHandshake.PLAYERS_WHITELIST);
                                                    list.add(player);

                                                    BedrockHandshakeConfigLoader.updatePlayersWhitelist(list);

                                                    ctx.getSource().sendFeedback(() -> Text.literal("Added player to whitelist: " + player), true);
                                                    return 1;
                                                })
                                        )
                                )
                        )
                )
        );
    }
}

