package me.perotin.communalaction.events;

import me.perotin.communalaction.CommunalAction;
import me.perotin.communalaction.files.CommunalFile;
import me.perotin.communalaction.objects.CommunalVote;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

public class MainClickEvent {

    private CommunalAction plugin;
    public static HashMap<UUID, String> voting = new HashMap<>();

    public MainClickEvent(CommunalAction plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            CommunalFile messages = new CommunalFile(CommunalFile.FileType.MESSAGES, plugin);
            Player clicker = (Player) event.getWhoClicked();
            if (voting.containsKey(clicker.getUniqueId())) {
                Inventory clicked = event.getClickedInventory();
                if (clicked.getName().equals(plugin.getConfig().getString("inventory-title").replace("$player$", voting.get(clicker.getUniqueId())))) {
                    // voting
                    event.setCancelled(true);
                    ItemStack currentItem = event.getCurrentItem();
                    if (currentItem.hasItemMeta()) {
                        if (currentItem.getItemMeta().getDisplayName() != null) {
                            String choice = currentItem.getItemMeta().getDisplayName();
                            String target = voting.get(clicker.getUniqueId());
                            CommunalVote existingVote = null;
                            // pair up choice with internal workings
                            String configKey = "";
                            String type = "";
                            for(String key : plugin.getConfig().getKeys(false)){
                                if(choice.equals(plugin.getConfig().getString(key+".inventory-title")
                                        .replace("$name$", target))){
                                    configKey = key;
                                    type = plugin.getConfig().getString(key+".name");

                                }
                            }
                            for(CommunalVote vote : plugin.getOnGoingVotes()){
                                if(vote.getPlayerVoted().getUniqueId().equals(Bukkit.getOfflinePlayer(target).getUniqueId())
                                        && vote.getType().equals(type)){
                                    existingVote = vote;
                                }
                            }

                            if(existingVote != null){
                                // found an existing vote
                                CommunalVoteEvent communalVoteEvent = new CommunalVoteEvent(clicker, existingVote);
                                    Bukkit.getPluginManager().callEvent(communalVoteEvent);

                                    if (!communalVoteEvent.isCancelled()) {
                                        //proceed with voting
                                        existingVote.addVote(clicker.getUniqueId());
                                        if (existingVote.getVoteCount() == null) {
                                            if (existingVote.getVoters().size() + 1 >= existingVote.getVoteCount()) {
                                                // MUTE THEM!
                                                Bukkit.broadcastMessage(plugin.getConfig().getString(configKey+".broadcast-message")
                                                .replace("$name$", target));
                                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), plugin.getConfig().getString(configKey+".command")
                                                        .replace("$player$", existingVote.getPlayerVoted().getName()));
                                                plugin.getOnGoingVotes().remove(existingVote);
                                            }
                                        } else {
                                            if (existingVote.getVoters().size() + 1 >= ((Bukkit.getOnlinePlayers().size() + 1) * (existingVote.getVotePercentageNeeded() / 100))) {
                                                Bukkit.broadcastMessage(plugin.getConfig().getString(configKey+".broadcast-message")
                                                        .replace("$name$", target));
                                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), plugin.getConfig().getString(configKey+".command")
                                                        .replace("$player$", existingVote.getPlayerVoted().getName()));
                                                plugin.getOnGoingVotes().remove(existingVote);
                                            }
                                        }


                                    }

                            } else {
                                CommunalVote vote = null;
                                if(plugin.getConfig().get(configKey) instanceof Integer) {
                                    vote = new CommunalVote(Bukkit.getOfflinePlayer(voting.get(clicker.getUniqueId())), clicker.getUniqueId(), plugin.getConfig().getString(configKey+".name"), plugin.getConfig().getInt(configKey), null);

                                } else if (plugin.getConfig().get(configKey) instanceof String){
                                    String parse = plugin.getConfig().getString(configKey);
                                    Integer percentage = 0;
                                    try {
                                        percentage = Integer.parseInt(parse.substring(0, 1));
                                    } catch (NumberFormatException ex){
                                        ex.printStackTrace();
                                    }
                                    vote = new CommunalVote(Bukkit.getOfflinePlayer(voting.get(clicker.getUniqueId())), clicker.getUniqueId(), plugin.getConfig().getString(configKey+".name"), null, percentage);


                                }
                                plugin.getOnGoingVotes().add(vote);
                                CommunalVoteEvent communalVoteEvent = new CommunalVoteEvent(clicker, vote);
                                Bukkit.getPluginManager().callEvent(communalVoteEvent);

                                if (!communalVoteEvent.isCancelled()) {
                                    //proceed with voting
                                    if (existingVote.getVoteCount() == null) {
                                        if (existingVote.getVoters().size() + 1 >= existingVote.getVoteCount()) {
                                            // MUTE THEM!
                                            Bukkit.broadcastMessage(plugin.getConfig().getString(configKey+".broadcast-message")
                                                    .replace("$name$", target));
                                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), plugin.getConfig().getString(configKey+".command")
                                                    .replace("$player$", existingVote.getPlayerVoted().getName()));
                                            plugin.getOnGoingVotes().remove(existingVote);
                                        }
                                    } else {
                                        if (existingVote.getVoters().size() + 1 >= ((Bukkit.getOnlinePlayers().size() + 1) * (existingVote.getVotePercentageNeeded() / 100))) {
                                            Bukkit.broadcastMessage(plugin.getConfig().getString(configKey+".broadcast-message")
                                                    .replace("$name$", target));
                                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), plugin.getConfig().getString(configKey+".command")
                                                    .replace("$player$", existingVote.getPlayerVoted().getName()));
                                            plugin.getOnGoingVotes().remove(existingVote);
                                        }
                                    }


                                }
                            }
                        }
                        }
                    }
                }
            }
        }


    @EventHandler
    public void onClose(InventoryCloseEvent event){

        if (voting.containsKey(event.getPlayer().getUniqueId())){
            voting.remove(event.getPlayer().getUniqueId());
        }
    }
}
