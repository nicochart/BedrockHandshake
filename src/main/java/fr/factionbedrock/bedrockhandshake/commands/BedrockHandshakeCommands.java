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
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import java.util.ArrayList;
import java.util.List;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public final class BedrockHandshakeCommands
{
    public static void register() {CommandRegistrationCallback.EVENT.register(BedrockHandshakeCommands::registerImpl);}

    private static void registerImpl(CommandDispatcher<CommandSourceStack> dispatcher, net.minecraft.core.HolderLookup.Provider registryAccess, Commands.CommandSelection env) {
        dispatcher.register(literal("bedrockhandshake")
                .requires(source -> source.hasPermission(2))

                .then(literal("doHandshakeOnIntegratedServer")
                        .then(argument("value", BoolArgumentType.bool())
                                .executes(ctx -> {
                                    boolean value = BoolArgumentType.getBool(ctx, "value");
                                    BedrockHandshakeConfigLoader.updateDoHandshakeOnIntegratedServer(value);
                                    ctx.getSource().sendSuccess(() -> Component.literal("doHandshakeOnIntegratedServer set to " + value), true);
                                    return 1;
                                })
                        )
                )
                .then(literal("toleratedInfractionCount")
                        .then(argument("value", IntegerArgumentType.integer(0))
                                .executes(ctx -> {
                                    int value = IntegerArgumentType.getInteger(ctx, "value");
                                    BedrockHandshakeConfigLoader.updateToleratedInfractionCount(value);
                                    ctx.getSource().sendSuccess(() -> Component.literal("toleratedInfractionCount set to " + value), true);
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
                                                        ctx.getSource().sendSuccess(() -> Component.literal("Added mod to whitelist: ").append(BedrockHandshakeHelper.textCopyable(mod)), true);
                                                    }
                                                    else
                                                    {
                                                        ctx.getSource().sendSuccess(() -> Component.literal("Mod already in whitelist").withStyle(style -> style.applyFormat(ChatFormatting.RED)), true);
                                                    }

                                                    return 1;
                                                })
                                        )
                                )
                                .then(literal("remove")
                                        .then(Commands.argument("value", StringArgumentType.word())
                                                .suggests(whitelistSuggestionProvider(BedrockHandshake.MODS_WHITELIST))
                                                .executes(ctx -> {
                                                    String mod = StringArgumentType.getString(ctx, "value");

                                                    List<String> list = new ArrayList<>(BedrockHandshake.MODS_WHITELIST);
                                                    if (list.contains(mod))
                                                    {
                                                        list.remove(mod);
                                                        BedrockHandshakeConfigLoader.updateModsWhitelist(list);
                                                        ctx.getSource().sendSuccess(() -> Component.literal("Removed mod from whitelist: ").append(BedrockHandshakeHelper.textCopyable(mod)), true);
                                                    }
                                                    else
                                                    {
                                                        ctx.getSource().sendSuccess(() -> Component.literal("Mod not found in whitelist").withStyle(style -> style.applyFormat(ChatFormatting.RED)), true);
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
                                                        ctx.getSource().sendSuccess(() -> Component.literal("Added pack to whitelist: ").append(BedrockHandshakeHelper.textCopyable(pack)), true);
                                                    }
                                                    else
                                                    {
                                                        ctx.getSource().sendSuccess(() -> Component.literal("Pack already in whitelist").withStyle(style -> style.applyFormat(ChatFormatting.RED)), true);
                                                    }
                                                    return 1;
                                                })
                                        )
                                )
                                .then(Commands.literal("remove")
                                        .then(Commands.argument("value", StringArgumentType.word())
                                                .suggests(whitelistSuggestionProvider(BedrockHandshake.PACKS_WHITELIST))
                                                .executes(ctx -> {
                                                    String pack = StringArgumentType.getString(ctx, "value");

                                                    List<String> list = new ArrayList<>(BedrockHandshake.PACKS_WHITELIST);
                                                    if (list.contains(pack))
                                                    {
                                                        list.remove(pack);
                                                        BedrockHandshakeConfigLoader.updatePacksWhitelist(list);
                                                        ctx.getSource().sendSuccess(() -> Component.literal("Removed pack from whitelist: ").append(BedrockHandshakeHelper.textCopyable(pack)), true);
                                                    }
                                                    else
                                                    {
                                                        ctx.getSource().sendSuccess(() -> Component.literal("Pack not found in whitelist").withStyle(style -> style.applyFormat(ChatFormatting.RED)), true);
                                                    }

                                                    return 1;
                                                })
                                        )
                                )
                                .then(Commands.literal("list")
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
                                                        ctx.getSource().sendSuccess(() -> Component.literal("Added player to whitelist: ").append(BedrockHandshakeHelper.textCopyable(player)), true);
                                                    }
                                                    else
                                                    {
                                                        ctx.getSource().sendSuccess(() -> Component.literal("Player already in whitelist").withStyle(style -> style.applyFormat(ChatFormatting.RED)), true);
                                                    }

                                                    return 1;
                                                })
                                        )
                                )
                                .then(Commands.literal("remove")
                                        .then(Commands.argument("value", StringArgumentType.word())
                                                .suggests(whitelistSuggestionProvider(BedrockHandshake.PLAYERS_WHITELIST))
                                                .executes(ctx -> {
                                                    String player = StringArgumentType.getString(ctx, "value");

                                                    List<String> list = new ArrayList<>(BedrockHandshake.PLAYERS_WHITELIST);
                                                    if (list.contains(player))
                                                    {
                                                        list.remove(player);
                                                        BedrockHandshakeConfigLoader.updatePlayersWhitelist(list);
                                                        ctx.getSource().sendSuccess(() -> Component.literal("Removed player from whitelist: ").append(BedrockHandshakeHelper.textCopyable(player)), true);
                                                    }
                                                    else
                                                    {
                                                        ctx.getSource().sendSuccess(() -> Component.literal("Player not found in whitelist").withStyle(style -> style.applyFormat(ChatFormatting.RED)), true);
                                                    }

                                                    return 1;
                                                })
                                        )
                                )
                                .then(Commands.literal("list")
                                        .executes(ctx -> listWhitelist(ctx.getSource(), "Players", BedrockHandshake.PLAYERS_WHITELIST))
                                )
                        )
                )
        );
    }

    private static SuggestionProvider<CommandSourceStack> whitelistSuggestionProvider(List<String> list)
    {
        return (context, builder) -> SharedSuggestionProvider.suggest(list, builder);
    }

    private static int listWhitelist(CommandSourceStack source, String name, List<String> list)
    {
        if (list.isEmpty()) {source.sendSuccess(() -> Component.literal(name + " whitelist is empty"), false);}
        else
        {
            MutableComponent text = Component.literal(name + " whitelist (" + list.size() + ") : ");

            for (int i = 0; i < list.size(); i++)
            {
                String entry = list.get(i);
                text.append(BedrockHandshakeHelper.textCopyable(entry));

                if (i < list.size() - 1) {text.append(", ");}
            }

            source.sendSuccess(() -> text, false);
        }
        return 1;
    }
}

