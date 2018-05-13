package me.perotin.communalaction.objects;

import org.bukkit.OfflinePlayer;

import java.util.HashSet;
import java.util.UUID;

public class CommunalVote {



    private Integer voteCount;
    private Integer votePercentageNeeded;
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

    public Integer getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }

    public Integer getVotePercentageNeeded() {
        return votePercentageNeeded;
    }

    public void setVotePercentageNeeded(Integer votePercentageNeeded) {
        this.votePercentageNeeded = votePercentageNeeded;
    }




}
