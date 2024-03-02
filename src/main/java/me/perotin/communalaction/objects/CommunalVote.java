package me.perotin.communalaction.objects;

import me.perotin.communalaction.CommunalAction;
import me.perotin.communalaction.files.CommunalFile;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.text.SimpleDateFormat;
import java.util.*;

public class CommunalVote {



    private int voteCount;
    private int votePercentageNeeded;
    private OfflinePlayer playerVoted;
    private HashSet<UUID> voters;
    private String type;
    private String date;




    public CommunalVote(OfflinePlayer playerVoted, UUID firstVote, String voteType, Integer voteCount, Integer votePercentageNeeded, CommunalAction plugin){
        // playerVoted is the player being voted to be punished
        // firstVote is the player who first voted for this player to be punished
        // voteType is the .name value from the config (muted, kicked, jailed)
        // voteCount is the raw number threshold for a punishment to be executed, -1 if not used
        // votePercentage is the percantage of online players that must vote for the punishment to be executed
        this.playerVoted = playerVoted;
        this.voters = new HashSet<>();
        voters.add(firstVote);
        this.type = voteType;

        this.voteCount = voteCount;
        this.votePercentageNeeded = votePercentageNeeded;
        CommunalFile log = new CommunalFile(CommunalFile.FileType.LOG, plugin);
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
        this.date = format.format(date);


        List<String> voters = new ArrayList<>();
        voters.add(firstVote.toString());
        log.getConfig().set(voteType+"."+playerVoted.getName()+"."+format.format(date)+".voters", voters);
        log.save();

    }

    public OfflinePlayer getPlayerVoted() {
        return this.playerVoted;
    }



    public boolean hasVoted(UUID uuid) {
        return getVoters().contains(uuid);
    }

    public HashSet<UUID> getVoters() {
        return voters;
    }

    public void addVote(UUID playerVoted) {
        this.voters.add(playerVoted);
    }

    public String getType() {
        return type;
    }

    public int getVoteCount() {
        return voteCount;
    }


    public int getVotePercentageNeeded() {
        return votePercentageNeeded;
    }

    public String getDate(){
        return this.date;
    }


    public boolean reachedThreshold() {
       if (voteCount != -1) {
           return getVoters().size() >= voteCount;
       } else {
           return getVoters().size() >= (getVotePercentageNeeded() / 100) * Bukkit.getOnlinePlayers().size();
       }
    }


    /**
     * Attempts to add a voter's UUID to the set of voters.
     *
     * @param voterUuid The UUID of the voter to add.
     * @return true if the voter was successfully added (i.e., had not already voted), false otherwise.
     */
    public boolean addVoter(UUID voterUuid) {
        return voters.add(voterUuid);
    }


    public boolean isForTargetAndChoice(String targetName, String choice) {
        // Check if the player being voted on matches the targetName and if the vote type matches the choice
        return this.playerVoted.getName().equalsIgnoreCase(targetName) && this.getType().equalsIgnoreCase(choice);
    }





}
