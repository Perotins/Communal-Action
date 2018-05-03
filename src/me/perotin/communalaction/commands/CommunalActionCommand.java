package me.perotin.communalaction.commands;

import me.perotin.communalaction.CommunalAction;
import me.perotin.communalaction.events.MainClickEvent;
import me.perotin.communalaction.files.CommunalFile;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.UUID;

public class CommunalActionCommand implements CommandExecutor{

    private CommunalAction plugin;
    private static HashSet<UUID> players = new HashSet<>();

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
                if (players.contains(sender.getUniqueId())) {
                    String target = args[0];
                    if (Bukkit.getPlayer(target) != null) {
                        //online
                        sender.openInventory(CommunalAction.getMainInventory(plugin, target));
                        MainClickEvent.voting.put(sender.getUniqueId(), target);

                    } else {
                        // offline
                        sender.sendMessage(messages.getString("punish-online"));
                    }
                } else {
                    // send message confirming it yada yada
                }
            }

        } else {
            // no can do
        }

        return true;
    }
}
