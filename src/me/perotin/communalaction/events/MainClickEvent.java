package me.perotin.communalaction.events;

import me.perotin.communalaction.CommunalAction;
import me.perotin.communalaction.files.CommunalFile;
import me.perotin.communalaction.objects.CommunalVote;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class MainClickEvent implements Listener {

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
            boolean broadcast = plugin.getConfig().getBoolean("broadcast-on-vote");
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
                            CommunalFile log = new CommunalFile(CommunalFile.FileType.LOG, plugin);

                            Date date = new Date();
                            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
                            for(String key : plugin.getConfig().getConfigurationSection("punishments").getKeys(false)){
                                if(choice.equals(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("punishments."+key+".inventory-title")
                                        .replace("$name$", target)))){
                                    //Bukkit.broadcastMessage(key);
                                    configKey += key;
                                    type = plugin.getConfig().getString("punishments."+key+".name");

                                }
                            }
                            for(CommunalVote vote : plugin.getOnGoingVotes()){
                                if(vote.getPlayerVoted().getUniqueId().equals(Bukkit.getOfflinePlayer(target).getUniqueId())
                                        && vote.getType().equals(type)){
                                    existingVote = vote;
                                }
                            }

                            if(existingVote != null){
                                if(existingVote.getVoters().stream().anyMatch(uuid -> uuid.equals(clicker.getUniqueId()))){
                                    // already vote
                                    clicker.closeInventory();
                                    clicker.sendMessage(messages.getString("already-voted"));
                                    return;
                                }
                                // found an existing vote
                                CommunalVoteEvent communalVoteEvent = new CommunalVoteEvent(clicker, existingVote);
                                    Bukkit.getPluginManager().callEvent(communalVoteEvent);

                                    if (!communalVoteEvent.isCancelled()) {
                                        //proceed with voting
                                        existingVote.addVote(clicker.getUniqueId());
                                        List<String> voters = existingVote.getVoters().stream().map(UUID::toString)
                                                .collect(Collectors.toList());
                                        log.set(type+"."+target+"."+existingVote.getDate()+".voters", voters);
                                        log.save();
                                        clicker.closeInventory();
                                        if (existingVote.getVoteCount() != -1) {
                                            if (existingVote.getVoters().size() + 1 >= existingVote.getVoteCount()) {
                                                // MUTE THEM!
                                                Bukkit.broadcastMessage(plugin.getConfig().getString("punishments."+configKey+".broadcast-message")
                                                .replace("$name$", target));
                                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), plugin.getConfig().getString("punshments."+configKey+".command")
                                                        .replace("$player$", existingVote.getPlayerVoted().getName()));
                                                plugin.getOnGoingVotes().remove(existingVote);
                                            } else {
                                                clicker.sendMessage(messages.getString("voted").replace("$player$", target));
                                                if(broadcast) {
                                                    Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("broadcast-message")
                                                            .replace("$player$", target)
                                                            .replace("$number$", existingVote.getVoters().size()+"")
                                                            .replace("$type$", type)));
                                                }
                                            }
                                        } else {
                                            if (existingVote.getVoters().size() + 1 >= ((Bukkit.getOnlinePlayers().size() + 1) * (existingVote.getVotePercentageNeeded() / 100))) {
                                                Bukkit.broadcastMessage(plugin.getConfig().getString("punishments."+configKey+".broadcast-message")
                                                        .replace("$name$", target));
                                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), plugin.getConfig().getString("punishments."+configKey+".command")
                                                        .replace("$player$", existingVote.getPlayerVoted().getName()));
                                                plugin.getOnGoingVotes().remove(existingVote);
                                            } else {
                                                clicker.sendMessage(messages.getString("voted").replace("$player$", target));
                                                if(broadcast) {
                                                    Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("broadcast-message")
                                                            .replace("$player$", target)
                                                            .replace("$number$", existingVote.getVoters().size()+"")
                                                            .replace("$type$", type)));
                                                }
                                            }
                                        }


                                    }

                            } else {
                                CommunalVote vote = null;
                                if(plugin.getConfig().get("punishments."+configKey+".vote") instanceof Integer) {
                                    vote = new CommunalVote(Bukkit.getOfflinePlayer(voting.get(clicker.getUniqueId())), clicker.getUniqueId(), plugin.getConfig().getString("punishments."+configKey+".name"), plugin.getConfig().getInt("punishments."+configKey+".vote"), -1, plugin);

                                } else if (plugin.getConfig().get("punishments."+configKey+".vote") instanceof String){
                                    String parse = plugin.getConfig().getString("punishments."+configKey);
                                    Integer percentage = 0;
                                    try {
                                        percentage = Integer.parseInt(parse.substring(0, 1));
                                    } catch (NumberFormatException ex){
                                        ex.printStackTrace();
                                    }
                                    vote = new CommunalVote(Bukkit.getOfflinePlayer(voting.get(clicker.getUniqueId())), clicker.getUniqueId(), plugin.getConfig().getString("punishments."+configKey+".name"), -1, percentage, plugin);



                                }
                                plugin.getOnGoingVotes().add(vote);
                                CommunalVoteEvent communalVoteEvent = new CommunalVoteEvent(clicker, vote);
                                Bukkit.getPluginManager().callEvent(communalVoteEvent);

                                if (!communalVoteEvent.isCancelled()) {
                                    //proceed with voting
                                    clicker.closeInventory();
                                    if (vote.getVoteCount() != -1) {
                                        if (vote.getVoters().size() + 1 >= vote.getVoteCount()) {
                                            // MUTE THEM!
                                            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("punishments."+configKey+".broadcast-message")
                                                    .replace("$player$", target)));
                                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), plugin.getConfig().getString("punishments."+configKey+".command")
                                                    .replace("$player$", vote.getPlayerVoted().getName()));
                                            plugin.getOnGoingVotes().remove(vote);
                                        } else {
                                            clicker.sendMessage(messages.getString("voted").replace("$player$", target));
                                            if(broadcast) {
                                                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("broadcast-message")
                                                        .replace("$player$", target)
                                                        .replace("$number$", 1+"")
                                                        .replace("$type$", type)));
                                            }


                                        }
                                    } else {
                                        if (vote.getVoters().size() + 1 >= ((Bukkit.getOnlinePlayers().size() + 1) * (vote.getVotePercentageNeeded() / 100))) {
                                            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("punishments."+configKey+".broadcast-message")
                                                    .replace("$player$", target)));
                                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), plugin.getConfig().getString("punishments."+configKey+".command")
                                                    .replace("$player$", vote.getPlayerVoted().getName()));
                                            plugin.getOnGoingVotes().remove(vote);
                                        } else {
                                            clicker.sendMessage(messages.getString("voted").replace("$player$", target));
                                            if(broadcast) {
                                                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("broadcast-message")
                                                        .replace("$player$", target)
                                                        .replace("$number$", 1+"")
                                                        .replace("$type$", type)));
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
        }


    @EventHandler
    public void onClose(InventoryCloseEvent event){

        if (voting.containsKey(event.getPlayer().getUniqueId())){
            voting.remove(event.getPlayer().getUniqueId());
        }
    }
}
