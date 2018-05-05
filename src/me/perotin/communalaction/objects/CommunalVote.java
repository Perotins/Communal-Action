package me.perotin.communalaction.objects;

import me.perotin.communalaction.CommunalAction;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashSet;
import java.util.UUID;

public class CommunalVote {


    public static int MUTEVOTECOUNT;
    public static int MUTEVOTEPERCENTAGE;
    public static int KICKVOTEPERCENTAGE;
    public static int KICKVOTECOUNT;
    public static int JAILVOTECOUNT;
    public static int JAILVOTEPERCENTAGE;

    private OfflinePlayer playerVoted;
    private HashSet<UUID> votees;
    private CommunalVoteType type;



    public CommunalVote(OfflinePlayer playerVoted, UUID firstVote, CommunalVoteType voteType){
        this.playerVoted = playerVoted;
        this.votees = new HashSet<>();
        votees.add(firstVote);
        this.type = voteType;
    }

    public OfflinePlayer getPlayerVoted() {
        return this.playerVoted;
    }

    public static void initializeConstants(CommunalAction plugin){
        FileConfiguration config = plugin.getConfig();
        if(config.get("mute-vote") instanceof String){
            if(!config.getString("mute-vote").contains("%")){
                throw new IllegalArgumentException("Mute vote must have a percent sign if wanting to use percentages!");
            } else {
                try {
                    MUTEVOTECOUNT = -1;
                    MUTEVOTEPERCENTAGE = Integer.parseInt(config.getString("mute-vote").substring(0, 2));
                } catch (NumberFormatException ex){
                    Bukkit.getLogger().severe("Error with mute-vote configuration in plugins/CommunalAction/config.yml!");
                    ex.printStackTrace();
                }

            }
        } else if(config.get("mute-vote") instanceof Integer){
                MUTEVOTEPERCENTAGE = -1;
                MUTEVOTECOUNT = config.getInt("mute-vote");
        } else {
            throw new NumberFormatException("Error with mute-vote configuration in plugins/CommunalAction/config.yml!");
        }

        if(config.get("kick-vote") instanceof String){
            if(!config.getString("kick-vote").contains("%")){
                throw new IllegalArgumentException("Kick vote must have a percent sign if wanting to use percentages!");
            } else {
                try {
                    KICKVOTECOUNT = -1;
                    KICKVOTEPERCENTAGE = Integer.parseInt(config.getString("kick-vote").substring(0, 2));
                } catch (NumberFormatException ex){
                    Bukkit.getLogger().severe("Error with kick-vote configuration in plugins/CommunalAction/config.yml!");
                    ex.printStackTrace();
                }

            }
        } else if(config.get("kick-vote") instanceof Integer){
            KICKVOTEPERCENTAGE = -1;
            KICKVOTECOUNT = config.getInt("kick-vote");
        } else {
            throw new NumberFormatException("Error with kick-vote configuration in plugins/CommunalAction/config.yml!");
        }

        if(config.get("jail-vote") instanceof String){
            if(!config.getString("jail-vote").contains("%")){
                throw new IllegalArgumentException("jail vote must have a percent sign if wanting to use percentages!");
            } else {
                try {
                    JAILVOTECOUNT = -1;
                    JAILVOTEPERCENTAGE = Integer.parseInt(config.getString("jail-vote").substring(0, 2));
                } catch (NumberFormatException ex){
                    Bukkit.getLogger().severe("Error with jail-vote configuration in plugins/CommunalAction/config.yml!");
                    ex.printStackTrace();
                }

            }
        } else if(config.get("jail-vote") instanceof Integer){
            JAILVOTEPERCENTAGE = -1;
            JAILVOTECOUNT = config.getInt("jail-vote");
        } else {
            throw new NumberFormatException("Error with jail-vote configuration in plugins/CommunalAction/config.yml!");
        }
    }



    public HashSet<UUID> getVotees() {
        return votees;
    }

    public void addVote(UUID playerVoted) {
        this.votees.add(playerVoted);
    }

    public CommunalVoteType getType() {
        return type;
    }



    public enum CommunalVoteType {
        MUTE, JAIL, KICK
    }

}
