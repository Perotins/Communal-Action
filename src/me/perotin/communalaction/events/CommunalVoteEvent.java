package me.perotin.communalaction.events;

import me.perotin.communalaction.objects.CommunalVote;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CommunalVoteEvent extends Event implements Cancellable{

    private Player voter;
    private CommunalVote vote;
    private boolean isCancelled;
    private static final HandlerList handlers = new HandlerList();



    public CommunalVoteEvent(Player voter, CommunalVote vote) {
        this.voter = voter;
        this.vote = vote;
        this.isCancelled = false;
    }

    public Player getVoter() {
        return voter;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.isCancelled = b;
    }

    public CommunalVote getVote() {
        return vote;
    }


    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
