package me.perotin.communalaction.events;

import me.perotin.communalaction.commands.CommunalActionCommand;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class RestrictPlayerEvent implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent event){
        if(CommunalActionCommand.players.contains(event.getPlayer().getUniqueId())){
            event.setTo(event.getFrom());

        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event){
        for(Player player : Bukkit.getOnlinePlayers()) {
            if (CommunalActionCommand.players.contains(player.getUniqueId())) {
                event.getRecipients().remove(player);
            }
        }
        if(CommunalActionCommand.players.contains(event.getPlayer().getUniqueId())){
            event.setCancelled(true);
        }
    }
}
