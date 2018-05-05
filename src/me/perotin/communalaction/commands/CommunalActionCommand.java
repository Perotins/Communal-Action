package me.perotin.communalaction.commands;

import me.perotin.communalaction.CommunalAction;
import me.perotin.communalaction.events.MainClickEvent;
import me.perotin.communalaction.files.CommunalFile;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.UUID;
import java.util.stream.IntStream;

public class CommunalActionCommand implements CommandExecutor{

    private CommunalAction plugin;
    public static HashSet<UUID> players = new HashSet<>();

    public CommunalActionCommand(CommunalAction plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        CommunalFile messages = new CommunalFile(CommunalFile.FileType.MESSAGES, plugin);
        if(commandSender instanceof Player){
            Player sender = (Player) commandSender;
            if(args.length != 1){
                sender.sendMessage(messages.getString("wrong-arguments"));
                return true;
            } else {
                    String target = args[0];
                    if (Bukkit.getPlayer(target) != null) {
                        //online
                        if (players.contains(sender.getUniqueId())) {

                            sender.openInventory(CommunalAction.getMainInventory(plugin, target));
                            MainClickEvent.voting.put(sender.getUniqueId(), target);
                            players.remove(sender.getUniqueId());
                        } else {
                                // send message confirming it

                            IntStream.range(0, 10).forEach(i -> sender.sendMessage(" "));

                                sender.sendMessage(messages.getString("confirming-action")
                                        .replace("$player$", args[0]));
                            TextComponent confirm = new TextComponent(TextComponent.fromLegacyText(messages.getString("confirming-action-2")
                                    .replace("$misc$", messages.getString("cancel"))));
                            confirm.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(messages.getString("click-to-confirm")).create()));
                            confirm.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/votep " + target));
                                sender.spigot().sendMessage(confirm);
                                players.add(sender.getUniqueId());
                            }

                        } else {
                        // offline
                        sender.sendMessage(messages.getString("punish-online"));
                        return true;
                    }
                }
            } else {
            // no can do
            commandSender.sendMessage(messages.getString("player-only"));

            return true;
        }

        return true;
    }
}
