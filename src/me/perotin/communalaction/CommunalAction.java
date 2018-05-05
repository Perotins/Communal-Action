package me.perotin.communalaction;

import me.perotin.communalaction.commands.CommunalActionCommand;
import me.perotin.communalaction.events.RestrictPlayerEvent;
import me.perotin.communalaction.files.CommunalFile;
import me.perotin.communalaction.objects.CommunalVote;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;

public class CommunalAction extends JavaPlugin {

    /*
        A plugin that can be used to allow the server population to make descisions together as a group
        Like voting to mute or jail a player.

        Started by Perotin on April 29, 2018
     */

    /*
    TODO list
    1. Refactor getVotees to getVoters
    2. Cleanup mainclickevent logic and maybe try to generalize it
    3. Implement rescind vote somehow
    4. implement broadcast on config option when vote event takes place
     */

    private HashSet<CommunalVote> onGoingVotes;


    @Override
    public void onEnable(){
        this.onGoingVotes = new HashSet<>();
        getCommand("communalaction").setExecutor(new CommunalActionCommand(this));
        Bukkit.getPluginManager().registerEvents(new RestrictPlayerEvent(), this);
        saveDefaultConfig();
        CommunalVote.initializeConstants(this);
    }

    public static Inventory getMainInventory(CommunalAction plugin, String name){
        CommunalFile file = new CommunalFile(CommunalFile.FileType.MESSAGES, plugin);
        Inventory inventory = Bukkit.createInventory(null, plugin.getConfig().getInt("inventory-size"), plugin.getConfig().getString("inventory-title")
                .replace("$name$", name));

        ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
        SkullMeta skullMeta = (SkullMeta) head.getItemMeta();
        skullMeta.setOwner(name);
        skullMeta.setDisplayName(ChatColor.GREEN + name);
        head.setItemMeta(skullMeta);

        inventory.setItem(4, head);
        inventory.setItem(10, constructItem("mute-display", name, Material.REDSTONE_BLOCK));
        inventory.setItem(12, constructItem("kick-display", name, Material.REDSTONE_BLOCK));
        inventory.setItem(14, constructItem("jail-display", name, Material.REDSTONE_BLOCK));
        inventory.setItem(16, constructItem("rescind-display", "", Material.EMERALD_BLOCK));






        return inventory;
    }

    public HashSet<CommunalVote> getOnGoingVotes() {
        return this.onGoingVotes;
    }
    private static ItemStack constructItem(String path, String name, Material material){
        CommunalFile file = new CommunalFile(CommunalFile.FileType.MESSAGES, (CommunalAction) Bukkit.getPluginManager().getPlugin("CommunalAction"));
        ItemStack item = new ItemStack(material);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(file.getString(path, CommunalFile.Placeholder.NAME, name));
        item.setItemMeta(itemMeta);
        return item;
    }
}
