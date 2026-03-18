package fr.factionbedrock.bedrockhandshake.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import fr.factionbedrock.bedrockhandshake.BedrockHandshake;
import fr.factionbedrock.bedrockhandshake.config.BedrockHandshakeConfigLoader;
import fr.factionbedrock.bedrockhandshake.util.BedrockHandshakeHelper;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

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
                                                    if (!list.contains(mod))
                                                    {
                                                        list.add(mod);
                                                        BedrockHandshakeConfigLoader.updateModsWhitelist(list);
                                                        ctx.getSource().sendFeedback(() -> Text.literal("Added mod to whitelist: ").append(BedrockHandshakeHelper.textCopyable(mod)), true);
                                                    }
                                                    else
                                                    {
                                                        ctx.getSource().sendFeedback(() -> Text.literal("Mod already in whitelist").styled(style -> style.withFormatting(Formatting.RED)), true);
                                                    }

                                                    return 1;
                                                })
                                        )
                                )
                                .then(literal("remove")
                                        .then(CommandManager.argument("value", StringArgumentType.word())
                                                .suggests(whitelistSuggestionProvider(BedrockHandshake.MODS_WHITELIST))
                                                .executes(ctx -> {
                                                    String mod = StringArgumentType.getString(ctx, "value");

                                                    List<String> list = new ArrayList<>(BedrockHandshake.MODS_WHITELIST);
                                                    if (list.contains(mod))
                                                    {
                                                        list.remove(mod);
                                                        BedrockHandshakeConfigLoader.updateModsWhitelist(list);
                                                        ctx.getSource().sendFeedback(() -> Text.literal("Removed mod from whitelist: ").append(BedrockHandshakeHelper.textCopyable(mod)), true);
                                                    }
                                                    else
                                                    {
                                                        ctx.getSource().sendFeedback(() -> Text.literal("Mod not found in whitelist").styled(style -> style.withFormatting(Formatting.RED)), true);
                                                    }

                                                    return 1;
                                                })
                                        )
                                )
                                .then(literal("list")
                                        .executes(ctx -> listWhitelist(ctx.getSource(), "Mods", BedrockHandshake.MODS_WHITELIST))
                                )
                        )
                        .then(literal("packs")
                                .then(literal("add")
                                        .then(argument("value", StringArgumentType.word())
                                                .executes(ctx -> {
                                                    String pack = StringArgumentType.getString(ctx, "value");

                                                    List<String> list = new ArrayList<>(BedrockHandshake.PACKS_WHITELIST);
                                                    if (!list.contains(pack))
                                                    {
                                                        list.add(pack);
                                                        BedrockHandshakeConfigLoader.updatePacksWhitelist(list);
                                                        ctx.getSource().sendFeedback(() -> Text.literal("Added pack to whitelist: ").append(BedrockHandshakeHelper.textCopyable(pack)), true);
                                                    }
                                                    else
                                                    {
                                                        ctx.getSource().sendFeedback(() -> Text.literal("Pack already in whitelist").styled(style -> style.withFormatting(Formatting.RED)), true);
                                                    }
                                                    return 1;
                                                })
                                        )
                                )
                                .then(CommandManager.literal("remove")
                                        .then(CommandManager.argument("value", StringArgumentType.word())
                                                .suggests(whitelistSuggestionProvider(BedrockHandshake.PACKS_WHITELIST))
                                                .executes(ctx -> {
                                                    String pack = StringArgumentType.getString(ctx, "value");

                                                    List<String> list = new ArrayList<>(BedrockHandshake.PACKS_WHITELIST);
                                                    if (list.contains(pack))
                                                    {
                                                        list.remove(pack);
                                                        BedrockHandshakeConfigLoader.updatePacksWhitelist(list);
                                                        ctx.getSource().sendFeedback(() -> Text.literal("Removed pack from whitelist: ").append(BedrockHandshakeHelper.textCopyable(pack)), true);
                                                    }
                                                    else
                                                    {
                                                        ctx.getSource().sendFeedback(() -> Text.literal("Pack not found in whitelist").styled(style -> style.withFormatting(Formatting.RED)), true);
                                                    }

                                                    return 1;
                                                })
                                        )
                                )
                                .then(CommandManager.literal("list")
                                        .executes(ctx -> listWhitelist(ctx.getSource(), "Packs", BedrockHandshake.PACKS_WHITELIST))
                                )
                        )
                        .then(literal("players")
                                .then(literal("add")
                                        .then(argument("value", StringArgumentType.word())
                                                .executes(ctx -> {
                                                    String player = StringArgumentType.getString(ctx, "value");

                                                    List<String> list = new ArrayList<>(BedrockHandshake.PLAYERS_WHITELIST);
                                                    if (!list.contains(player))
                                                    {
                                                        list.add(player);
                                                        BedrockHandshakeConfigLoader.updatePlayersWhitelist(list);
                                                        ctx.getSource().sendFeedback(() -> Text.literal("Added player to whitelist: ").append(BedrockHandshakeHelper.textCopyable(player)), true);
                                                    }
                                                    else
                                                    {
                                                        ctx.getSource().sendFeedback(() -> Text.literal("Player already in whitelist").styled(style -> style.withFormatting(Formatting.RED)), true);
                                                    }

                                                    return 1;
                                                })
                                        )
                                )
                                .then(CommandManager.literal("remove")
                                        .then(CommandManager.argument("value", StringArgumentType.word())
                                                .suggests(whitelistSuggestionProvider(BedrockHandshake.PLAYERS_WHITELIST))
                                                .executes(ctx -> {
                                                    String player = StringArgumentType.getString(ctx, "value");

                                                    List<String> list = new ArrayList<>(BedrockHandshake.PLAYERS_WHITELIST);
                                                    if (list.contains(player))
                                                    {
                                                        list.remove(player);
                                                        BedrockHandshakeConfigLoader.updatePlayersWhitelist(list);
                                                        ctx.getSource().sendFeedback(() -> Text.literal("Removed player from whitelist: ").append(BedrockHandshakeHelper.textCopyable(player)), true);
                                                    }
                                                    else
                                                    {
                                                        ctx.getSource().sendFeedback(() -> Text.literal("Player not found in whitelist").styled(style -> style.withFormatting(Formatting.RED)), true);
                                                    }

                                                    return 1;
                                                })
                                        )
                                )
                                .then(CommandManager.literal("list")
                                        .executes(ctx -> listWhitelist(ctx.getSource(), "Players", BedrockHandshake.PLAYERS_WHITELIST))
                                )
                        )
                )
        );
    }

    private static SuggestionProvider<ServerCommandSource> whitelistSuggestionProvider(List<String> list)
    {
        return (context, builder) -> CommandSource.suggestMatching(list, builder);
    }

    private static int listWhitelist(ServerCommandSource source, String name, List<String> list)
    {
        if (list.isEmpty()) {source.sendFeedback(() -> Text.literal(name + " whitelist is empty"), false);}
        else
        {
            MutableText text = Text.literal(name + " whitelist (" + list.size() + ") : ");

            for (int i = 0; i < list.size(); i++)
            {
                String entry = list.get(i);
                text.append(BedrockHandshakeHelper.textCopyable(entry));

                if (i < list.size() - 1) {text.append(", ");}
            }

            source.sendFeedback(() -> text, false);
        }
        return 1;
    }
}

