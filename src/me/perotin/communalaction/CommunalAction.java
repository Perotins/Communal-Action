package me.perotin.communalaction;

import me.perotin.communalaction.commands.CommunalActionCommand;
import me.perotin.communalaction.events.MainClickEvent;
import me.perotin.communalaction.events.RestrictPlayerEvent;
import me.perotin.communalaction.objects.CommunalVote;
import me.perotin.communalaction.utils.MetricsLite;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class CommunalAction extends JavaPlugin {

    /*
        A plugin that can be used to allow the server population to make descisions together as a group
        Like voting to mute or jail a player.

        Started by Perotin on April 29, 2018
     */

    /*
    TODO list
    1. Implement rescind vote somehow
    2. Cleanup rest of classes and maybe finish it?
     */

    private HashSet<CommunalVote> onGoingVotes;

     static List<String> voteTypes;


    @Override
    public void onEnable(){
        saveDefaultConfig();

        MetricsLite lite = new MetricsLite(this);
        this.onGoingVotes = new HashSet<>();
        voteTypes = new ArrayList<>();
        if (!new File(getDataFolder(), "messages.yml").exists()) {
            saveResource("messages.yml", false);
        }
        if (!new File(getDataFolder(), "logs.yml").exists()) {
            saveResource("logs.yml", false);
        }

        Bukkit.getPluginManager().registerEvents(new MainClickEvent(this), this);
        Bukkit.getPluginManager().registerEvents(new RestrictPlayerEvent(), this);
        getCommand("communalaction").setExecutor(new CommunalActionCommand(this));
        for(String key : getConfig().getConfigurationSection("punishments").getKeys(false)){
            voteTypes.add(getConfig().getString("punishments."+key+".name"));
        }
        //getConfig().getConfigurationSection("punishmnents").getKeys(false).forEach(key -> voteTypes.add(getConfig().getString("punishments."+key+".name")));


    }

    public Inventory getMainInventory(String name){

      Inventory inventory = Bukkit.createInventory(null, getConfig().getInt("inventory-size"),
               getConfig().getString("inventory-title").replace("$player$", name));



         ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) head.getItemMeta();
        skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer(name));
        skullMeta.setDisplayName(ChatColor.GREEN + name);
        head.setItemMeta(skullMeta);

        inventory.setItem(4, head);

        for(String key : getConfig().getConfigurationSection("punishments").getKeys(false)){
            inventory.setItem(getConfig().getInt("punishments."+key+".inventory-slot"), constructItem(getConfig().getString("punishments."+key+".inventory-title"), name, Material.valueOf(getConfig().getString("punishments."+key+".material"))));


        }







        return inventory;
    }

    public HashSet<CommunalVote> getOnGoingVotes() {
        return this.onGoingVotes;
    }

    private static ItemStack constructItem(String title, String name, Material material){
        ItemStack item = new ItemStack(material);
        ItemMeta itemMeta = item.getItemMeta();
        String newTitle = ChatColor.translateAlternateColorCodes('&', title.replace("$name$", name));


        itemMeta.setDisplayName(newTitle);
        item.setItemMeta(itemMeta);
        return item;
    }

}
