package me.perotin.communalaction;

import me.perotin.communalaction.commands.CommunalActionCommand;
import me.perotin.communalaction.events.RestrictPlayerEvent;
import me.perotin.communalaction.files.CommunalFile;
import me.perotin.communalaction.objects.CommunalVote;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;

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

    public static List<String> voteTypes;


    @Override
    public void onEnable(){
        this.onGoingVotes = new HashSet<>();
        getCommand("communalaction").setExecutor(new CommunalActionCommand(this));
        Bukkit.getPluginManager().registerEvents(new RestrictPlayerEvent(), this);
        saveDefaultConfig();
        for(String key : getConfig().getKeys(false)){
            voteTypes.add(getConfig().getString(key+".name"));
        }
    }

    public static Inventory getMainInventory(CommunalAction plugin, String name){
        FileConfiguration config = plugin.getConfig();
        Inventory inventory = Bukkit.createInventory(null, plugin.getConfig().getInt("inventory-size"), plugin.getConfig().getString("inventory-title")
                .replace("$name$", name));

        ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
        SkullMeta skullMeta = (SkullMeta) head.getItemMeta();
        skullMeta.setOwner(name);
        skullMeta.setDisplayName(ChatColor.GREEN + name);
        head.setItemMeta(skullMeta);

        inventory.setItem(4, head);

        for(String key : plugin.getConfig().getKeys(false)){
            inventory.setItem(config.getInt(key+".inventory-slot"), constructItem(config.getString(key+".inventory-title"), name, Material.valueOf(config.getString(key+".material"))));

        }







        return inventory;
    }

    public HashSet<CommunalVote> getOnGoingVotes() {
        return this.onGoingVotes;
    }

    private static ItemStack constructItem(String title, String name, Material material){
        ItemStack item = new ItemStack(material);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(title.replace("$name$", name));
        item.setItemMeta(itemMeta);
        return item;
    }

}
