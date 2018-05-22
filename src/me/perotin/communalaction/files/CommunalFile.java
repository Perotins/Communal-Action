package me.perotin.communalaction.files;

import me.perotin.communalaction.CommunalAction;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class CommunalFile {

    private File file;
    private FileConfiguration config;

    public CommunalFile(FileType type, CommunalAction plugin){
        if(type == FileType.MESSAGES){
            file = new File(plugin.getDataFolder(), "messages.yml");
            config = YamlConfiguration.loadConfiguration(file);
        } else if(type == FileType.LOG) {
            file = new File(plugin.getDataFolder(), "logs.yml");
            config = YamlConfiguration.loadConfiguration(file);
        }
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public String getString(String path, Placeholder placeholder, String replacement){
        String raw  = config.getString(path);
        switch (placeholder) {
            case NAME:
                raw = raw.replace("$name$", replacement);
                break;
            case NUMBER:
                raw = raw.replace("$number$", replacement);
                break;
        }

        return ChatColor.translateAlternateColorCodes('&', raw);
    }

    public void set(String path, Object seriazable){
        config.set(path, seriazable);
    }

    public void save(){
        try {
            this.config.save(file);
        } catch (IOException ex){
            ex.printStackTrace();
        }
    }

    public String getString(String path){
        return ChatColor.translateAlternateColorCodes('&', config.getString(path));
    }

    public enum Placeholder {
        NAME, NUMBER
    }

    public enum FileType {
        MESSAGES, LOG
    }
}
