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


                            if (choice.equals(messages.getString("mute-display"))) {
                                // put a vote in for mute


                                for (CommunalVote vote : plugin.getOnGoingVotes()) {
                                    if (vote.getType() == CommunalVote.CommunalVoteType.MUTE
                                            && vote.getPlayerVoted().getUniqueId().equals(Bukkit.getOfflinePlayer(target).getUniqueId())) {
                                        existingVote = vote;
                                    }
                                }
                                // if existing vote

                                if (existingVote != null) {
                                    CommunalVoteEvent communalVoteEvent = new CommunalVoteEvent(clicker, existingVote);
                                    Bukkit.getPluginManager().callEvent(communalVoteEvent);

                                    if (!communalVoteEvent.isCancelled()) {
                                        //proceed with voting
                                        existingVote.addVote(clicker.getUniqueId());
                                        if (CommunalVote.MUTEVOTEPERCENTAGE == -1) {
                                            if (existingVote.getVotees().size() + 1 >= CommunalVote.MUTEVOTECOUNT) {
                                                // MUTE THEM!
                                                Bukkit.broadcastMessage(messages.getString("muted-player-broadcast")
                                                        .replace("$player$", existingVote.getPlayerVoted().getName()));
                                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), plugin.getConfig().getString("mute-vote.command")
                                                        .replace("$player$", existingVote.getPlayerVoted().getName()));
                                                plugin.getOnGoingVotes().remove(existingVote);
                                            }
                                        } else {
                                            if (existingVote.getVotees().size() + 1 >= ((Bukkit.getOnlinePlayers().size() + 1) * (CommunalVote.MUTEVOTEPERCENTAGE / 100))) {
                                                Bukkit.broadcastMessage(messages.getString("muted-player-broadcast")
                                                        .replace("$player$", existingVote.getPlayerVoted().getName()));
                                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), plugin.getConfig().getString("mute-vote.command")
                                                        .replace("$player$", existingVote.getPlayerVoted().getName()));
                                                plugin.getOnGoingVotes().remove(existingVote);
                                            }
                                        }


                                    }

                                } else {
                                    CommunalVote vote = new CommunalVote(Bukkit.getOfflinePlayer(voting.get(clicker.getUniqueId())), clicker.getUniqueId(), CommunalVote.CommunalVoteType.MUTE);
                                    plugin.getOnGoingVotes().add(vote);
                                    CommunalVoteEvent communalVoteEvent = new CommunalVoteEvent(clicker, vote);
                                    Bukkit.getPluginManager().callEvent(communalVoteEvent);

                                    if (!communalVoteEvent.isCancelled()) {
                                        //proceed with voting
                                        if (CommunalVote.MUTEVOTEPERCENTAGE == -1) {
                                            if (existingVote.getVotees().size() + 1 >= CommunalVote.MUTEVOTECOUNT) {
                                                // MUTE THEM!
                                                Bukkit.broadcastMessage(messages.getString("muted-player-broadcast")
                                                        .replace("$player$", existingVote.getPlayerVoted().getName()));
                                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), plugin.getConfig().getString("mute-vote.command")
                                                        .replace("$player$", existingVote.getPlayerVoted().getName()));
                                                plugin.getOnGoingVotes().remove(existingVote);
                                            }
                                        } else {
                                            if (existingVote.getVotees().size() + 1 >= ((Bukkit.getOnlinePlayers().size() + 1) * (CommunalVote.MUTEVOTEPERCENTAGE / 100))) {
                                                Bukkit.broadcastMessage(messages.getString("muted-player-broadcast")
                                                        .replace("$player$", existingVote.getPlayerVoted().getName()));
                                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), plugin.getConfig().getString("mute-vote.command")
                                                        .replace("$player$", existingVote.getPlayerVoted().getName()));
                                                plugin.getOnGoingVotes().remove(existingVote);
                                            }
                                        }

                                    }
                                }
                            }
                            else if (choice.equals(messages.getString("jail-display"))) {
                                // put a vote in for mute


                                for (CommunalVote vote : plugin.getOnGoingVotes()) {
                                    if (vote.getType() == CommunalVote.CommunalVoteType.JAIL
                                            && vote.getPlayerVoted().equals(Bukkit.getOfflinePlayer(target).getUniqueId())) {
                                        existingVote = vote;
                                    }
                                }
                                // if existing vote

                                if (existingVote != null) {
                                    CommunalVoteEvent communalVoteEvent = new CommunalVoteEvent(clicker, existingVote);
                                    Bukkit.getPluginManager().callEvent(communalVoteEvent);

                                    if (!communalVoteEvent.isCancelled()) {
                                        //proceed with voting
                                        existingVote.addVote(clicker.getUniqueId());
                                        if (CommunalVote.JAILVOTEPERCENTAGE == -1) {
                                            if (existingVote.getVotees().size() + 1 >= CommunalVote.JAILVOTECOUNT) {
                                                // jail THEM!
                                                Bukkit.broadcastMessage(messages.getString("jail-player-broadcast")
                                                        .replace("$player$", existingVote.getPlayerVoted().getName()));
                                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), plugin.getConfig().getString("jail-vote.command")
                                                        .replace("$player$", existingVote.getPlayerVoted().getName()));
                                                plugin.getOnGoingVotes().remove(existingVote);
                                            }
                                        } else {
                                            if (existingVote.getVotees().size() + 1 >= ((Bukkit.getOnlinePlayers().size() + 1) * (CommunalVote.JAILVOTEPERCENTAGE / 100))) {
                                                Bukkit.broadcastMessage(messages.getString("jail-player-broadcast")
                                                        .replace("$player$", existingVote.getPlayerVoted().getName()));
                                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), plugin.getConfig().getString("jail-vote.command")
                                                        .replace("$player$", existingVote.getPlayerVoted().getName()));
                                                plugin.getOnGoingVotes().remove(existingVote);
                                            }
                                        }


                                    }

                                } else {
                                    CommunalVote vote = new CommunalVote(Bukkit.getOfflinePlayer(voting.get(clicker.getUniqueId())), clicker.getUniqueId(), CommunalVote.CommunalVoteType.JAIL);
                                    plugin.getOnGoingVotes().add(vote);
                                    CommunalVoteEvent communalVoteEvent = new CommunalVoteEvent(clicker, vote);
                                    Bukkit.getPluginManager().callEvent(communalVoteEvent);

                                    if (!communalVoteEvent.isCancelled()) {
                                        //proceed with voting
                                        if (CommunalVote.JAILVOTEPERCENTAGE == -1) {
                                            if (existingVote.getVotees().size() + 1 >= CommunalVote.JAILVOTECOUNT) {
                                                // Jail THEM!
                                                Bukkit.broadcastMessage(messages.getString("jail-player-broadcast")
                                                        .replace("$player$", existingVote.getPlayerVoted().getName()));
                                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), plugin.getConfig().getString("jail-vote.command")
                                                        .replace("$player$", existingVote.getPlayerVoted().getName()));
                                                plugin.getOnGoingVotes().remove(existingVote);
                                            }
                                        } else {
                                            if (existingVote.getVotees().size() + 1 >= ((Bukkit.getOnlinePlayers().size() + 1) * (CommunalVote.JAILVOTEPERCENTAGE / 100))) {
                                                Bukkit.broadcastMessage(messages.getString("jail-player-broadcast")
                                                        .replace("$player$", existingVote.getPlayerVoted().getName()));
                                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), plugin.getConfig().getString("jail-vote.command")
                                                        .replace("$player$", existingVote.getPlayerVoted().getName()));
                                                plugin.getOnGoingVotes().remove(existingVote);
                                            }
                                        }

                                    }
                                }
                            }
                            else if (choice.equals(messages.getString("kick-display"))) {
                                // put a vote in for mute


                                for (CommunalVote vote : plugin.getOnGoingVotes()) {
                                    if (vote.getType() == CommunalVote.CommunalVoteType.KICK
                                            && vote.getPlayerVoted().equals(Bukkit.getOfflinePlayer(target).getUniqueId())) {
                                        existingVote = vote;
                                    }
                                }
                                // if existing vote

                                if (existingVote != null) {
                                    CommunalVoteEvent communalVoteEvent = new CommunalVoteEvent(clicker, existingVote);
                                    Bukkit.getPluginManager().callEvent(communalVoteEvent);

                                    if (!communalVoteEvent.isCancelled()) {
                                        //proceed with voting
                                        existingVote.addVote(clicker.getUniqueId());
                                        if (CommunalVote.KICKVOTEPERCENTAGE == -1) {
                                            if (existingVote.getVotees().size() + 1 >= CommunalVote.KICKVOTECOUNT) {
                                                // jail THEM!
                                                Bukkit.broadcastMessage(messages.getString("kicked-player-broadcast")
                                                        .replace("$player$", existingVote.getPlayerVoted().getName()));
                                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), plugin.getConfig().getString("kick-vote.command")
                                                        .replace("$player$", existingVote.getPlayerVoted().getName()));
                                                plugin.getOnGoingVotes().remove(existingVote);
                                            }
                                        } else {
                                            if (existingVote.getVotees().size() + 1 >= ((Bukkit.getOnlinePlayers().size() + 1) * (CommunalVote.KICKVOTEPERCENTAGE / 100))) {
                                                Bukkit.broadcastMessage(messages.getString("kicked-player-broadcast")
                                                        .replace("$player$", existingVote.getPlayerVoted().getName()));
                                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), plugin.getConfig().getString("kick-vote.command")
                                                        .replace("$player$", existingVote.getPlayerVoted().getName()));
                                                plugin.getOnGoingVotes().remove(existingVote);
                                            }
                                        }


                                    }

                                } else {
                                    CommunalVote vote = new CommunalVote(Bukkit.getOfflinePlayer(voting.get(clicker.getUniqueId())), clicker.getUniqueId(), CommunalVote.CommunalVoteType.KICK);
                                    plugin.getOnGoingVotes().add(vote);
                                    CommunalVoteEvent communalVoteEvent = new CommunalVoteEvent(clicker, vote);
                                    Bukkit.getPluginManager().callEvent(communalVoteEvent);

                                    if (!communalVoteEvent.isCancelled()) {
                                        //proceed with voting
                                        if (CommunalVote.KICKVOTEPERCENTAGE == -1) {
                                            if (existingVote.getVotees().size() + 1 >= CommunalVote.KICKVOTECOUNT) {
                                                // Jail THEM!
                                                Bukkit.broadcastMessage(messages.getString("kicked-player-broadcast")
                                                        .replace("$player$", existingVote.getPlayerVoted().getName()));
                                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), plugin.getConfig().getString("KICK-vote.command")
                                                        .replace("$player$", existingVote.getPlayerVoted().getName()));
                                                plugin.getOnGoingVotes().remove(existingVote);
                                            }
                                        } else {
                                            if (existingVote.getVotees().size() + 1 >= ((Bukkit.getOnlinePlayers().size() + 1) * (CommunalVote.KICKVOTEPERCENTAGE / 100))) {
                                                Bukkit.broadcastMessage(messages.getString("kicked-player-broadcast")
                                                        .replace("$player$", existingVote.getPlayerVoted().getName()));
                                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), plugin.getConfig().getString("kick-vote.command")
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

    }
    @EventHandler
    public void onClose(InventoryCloseEvent event){

        if (voting.containsKey(event.getPlayer().getUniqueId())){
            voting.remove(event.getPlayer().getUniqueId());
        }
    }
}
