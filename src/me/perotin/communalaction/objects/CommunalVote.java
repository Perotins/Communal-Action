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






}
