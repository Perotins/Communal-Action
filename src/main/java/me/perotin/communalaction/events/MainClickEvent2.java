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
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class MainClickEvent2 implements Listener {

    private CommunalAction plugin;
    public static HashMap<UUID, String> voting = new HashMap<>();

    public MainClickEvent2(CommunalAction plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        Bukkit.broadcastMessage("1");

        Player clicker = (Player) event.getWhoClicked();
        if (!voting.containsKey(clicker.getUniqueId())) return;
        Bukkit.broadcastMessage("2");

        String inventoryTitle = plugin.getConfig().getString("inventory-title").replace("$player$", voting.get(clicker.getUniqueId()));
        if (!event.getView().getTitle().equals(inventoryTitle)) return;
        Bukkit.broadcastMessage("3");

        event.setCancelled(true);
        ItemStack currentItem = event.getCurrentItem();
        if (currentItem == null || !currentItem.hasItemMeta() || currentItem.getItemMeta().getDisplayName() == null) return;
        Bukkit.broadcastMessage("Before Handled vote");

        handleVote(clicker, currentItem.getItemMeta().getDisplayName());
    }

    private void handleVote(Player clicker, String choice) {
        Bukkit.broadcastMessage("Handled vote");
        String target = voting.get(clicker.getUniqueId());
        CommunalVote existingVote = findExistingVote(choice, target);

        if (existingVote != null) {
            if (alreadyVoted(clicker, existingVote)) return;
            processVote(clicker, existingVote, choice, target);
        } else {
            createAndProcessNewVote(clicker, choice, target);
        }
    }

    private boolean alreadyVoted(Player clicker, CommunalVote vote) {
        Bukkit.broadcastMessage("Already vote");

        if (vote.getVoters().contains(clicker.getUniqueId())) {
            CommunalFile messages = new CommunalFile(CommunalFile.FileType.MESSAGES, plugin);

            clicker.sendMessage(messages.getString("already-voted"));
            clicker.closeInventory();
            return true;
        }
        return false;
    }

    private CommunalVote findExistingVote(String choice, String target) {
        // Simplified logic to find an existing vote based on the choice and target
        return plugin.getOnGoingVotes().stream()
                .filter(vote -> vote.isForTargetAndChoice(target, choice))
                .findFirst()
                .orElse(null);
    }

    private void processVote(Player clicker, CommunalVote vote, String choice, String target) {
        // Logic to process an existing vote or a new vote
        if (!vote.addVoter(clicker.getUniqueId())) return;

        Bukkit.broadcastMessage("process vote");


        String configKey = extractConfigKey(choice, target);
        if (vote.reachedThreshold()) {
            executePunishment(configKey, target);
        } else {
            notifyVoteSuccess(clicker, target);
        }
    }

    private void createAndProcessNewVote(Player clicker, String choice, String target) {
        String configKey = extractConfigKey(choice, target);
        CommunalVote newVote = plugin.createVote(target, configKey, clicker.getUniqueId());
        if (newVote == null) return;
        Bukkit.broadcastMessage("createAndProcess vote");

        plugin.getOnGoingVotes().add(newVote);
        processVote(clicker, newVote, choice, target);
    }

    private String extractConfigKey(String choice, String target) {
        // Loop through the punishment configurations to find a match for the choice
        for (String key : plugin.getConfig().getConfigurationSection("punishments").getKeys(false)) {
            String inventoryTitle = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("punishments." + key + ".inventory-title").replace("$name$", target));
            if (choice.equals(inventoryTitle)) {
                return key;
            }
        }
        return null; // No matching key found
    }


    private void executePunishment(String configKey, String target) {
        Bukkit.broadcastMessage("Execute punish");

        if (configKey == null || configKey.isEmpty()) return;

        // Broadcast message if configured
        String broadcastMessage = plugin.getConfig().getString("punishments." + configKey + ".broadcast-message").replace("$name$", target);
        if (broadcastMessage != null && !broadcastMessage.isEmpty()) {
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', broadcastMessage));
        }

        // Execute command as punishment
        String command = plugin.getConfig().getString("punishments." + configKey + ".command").replace("$player$", target);
        if (command != null && !command.isEmpty()) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
        }
    }


    private void notifyVoteSuccess(Player clicker, String target) {
        CommunalFile messages = new CommunalFile(CommunalFile.FileType.MESSAGES, plugin);

        String message = messages.getString("voted").replace("$player$", target);
        if (message != null && !message.isEmpty()) {
            clicker.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        }
    }


    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player)) return;
        voting.remove(event.getPlayer().getUniqueId());
    }
}
