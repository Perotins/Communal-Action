package me.perotin.communalaction.objects;

import org.bukkit.OfflinePlayer;

import java.util.HashSet;
import java.util.UUID;

public class CommunalVote {



    private int voteCount;
    private int votePercentageNeeded;
    private OfflinePlayer playerVoted;
    private HashSet<UUID> voters;
    private String type;




    public CommunalVote(OfflinePlayer playerVoted, UUID firstVote, String voteType, Integer voteCount, Integer votePercentageNeeded){
        this.playerVoted = playerVoted;
        this.voters = new HashSet<>();
        voters.add(firstVote);
        this.type = voteType;

        this.voteCount = voteCount;
        this.votePercentageNeeded = votePercentageNeeded;

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






}
