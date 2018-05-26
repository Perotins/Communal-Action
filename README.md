# Communal Action

A problem that every Minecraft server faces is moderating itself. Finding good moderators is trouble enough, but finding moderators that can always be online is even more troubling. This is why Communal Action was made.

This plugin will effectively give your player base the ability to moderate itself. The way that this is done is that players vote other players to be punished by predefined punishments. This plugin is very expansive in that you can create unlimited punishments all doing very different things.

**Example use case:**
Player 1 is hacking and no moderators are on. 5 other players get annoyed by this and vote to have player 1 jailed. This allows time for a player to get a moderator while not having their experience ruined by a hacker.

**Can players abuse this?**
Yes. Giving power to the general population of your server can always lead to bad things if not treated with care. Communal Action logs the time and who votes for each time a player is voted to be punished. It is up to you to instill harsh punishments for falsely voting someone to be punished. This plugin freezes players as they vote and stops all incoming chat to them to let them know that they can be punished for false voting and it shouldn't be taken lightly. The plugin also asks them to make sure they have sufficient proof before voting, which is something that I recommend you require before voting.

**What versions should this work with?**
Anything 1.7 and up should be fine. If something is not working it is probably because you're using a material that does not exist for the minecraft version you are using (see config.yml).


If you ever mess up your config.yml you can copy the defaults here.

```yaml
# Welcome to Communal Action's configuration.yml file!
# If you need any assistance, I am very reachable over Spigot!


# Should Communal Action allow players to punish themselves?
punish-self: false

# must be a multiple of 9, size of the inventory shown when doing /ca
inventory-size: 27

# name of the inventory
inventory-title: "Chose a punishment for $player$!"

# Should we broadcast whenever a player has been voted to be punished? Note, this can lead to people "bandwagoning" and voting for the fun of it.
# All votes are logged and I encourage you to track down people who vote without evidence and punish them yourselves.
broadcast-on-vote: false

# If you set the above message to true, then this is the message sent whenever a vote is casted.
# If you have the above set to false, then skip over this.
broadcast-message: "&e$player$ &chas $number$ vote to be $type$!"


# WARNING: This is where the core configurations of Communal Action come into play! Bear with me here.
# There are a few ways for Communal Action to decide when a vote should end
# The first is after a certain amount of votes is reached (i/e 5 votes and action takes place)
# The second is that a % of the online players have voted (i/e 50% of the server voted)

# So after reading the two choices above, how do you configure it to your liking?
# If you want it to use percentages, you would do "50%". Make sure to include the percentage sign!
# If you want it to go by player count only, then just include the number i/e 5 players
# The command portion is the command that you want Communal Action to run when voted
# The broadcast option is if the plugin should broadcast to the entire server than <player> is being voted to be <muted/jailed/kicked>
# The inventory title and inventory slot pertain to the main inventory
# The name part is how the plugin recognizes this form of punishment, so leave it plain and simple without chatcolors etc
# The name section is put in the $type$ in the broadcast-message string above.
# The material part also pertains to the inventory, to see all valid materials see this link https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Material.html


# NOTE! If you use percentages and generally have a low amount of players online, this could mean that one player could mute/jail/kick someone
# All by themselves. I'd generally say to use plain numbers, but it is up to you.
punishments:
   mute:
      vote: 5
      command: "mute $player$ 300"
      broadcast: true
      broadcast-message: "&4[&c$player$&4]&c has been voted to be muted for 5 minutes!"
      inventory-title: "&eMute &f$name$"
      inventory-slot: 10
      name: "muted"
      material: "REDSTONE_BLOCK"
   kick:
      vote: 3
      command: "kick $player$"
      broadcast: true
      broadcast-message: "&4[&c$player$&4]&c has been voted to be kicked!"
      inventory-title: "&eKick &f$name$"
      inventory-slot: 12
      name: "kicked"
      material: "REDSTONE_BLOCK"
   jail:
      vote: 7
      command: "jail $player$ 300"
      broadcast: true
      broadcast-message: "&4[&c$player$&4]&c has been voted to be jailed for 5 minutes!"
      inventory-title: "&eJail &f$name$"
      inventory-slot: 14
      name: "jailed"
      material: "REDSTONE_BLOCK"

```

There are several components that make up a punishment in Communal Action.

The first is what type of voting you want to use.
The first type of vote is raw numbers. This is a preset number that once reached in votes will punish the player.

The second is a percentage of the total online players. This allows for dynamic voting that increases or decreases the threshold for punishing based on the amount of players online.

The next part is the command that should be run when the threshold is reached.

The rest is self-explanatory. If you have questions feel free to message me here.

There is also a messages.yml file which you can edit any message from.

The command is:
/communalaction <player>
*Aliases include: ** *ca, votep, votepunish*
**Permission: ** *communalaction.use*

To reload the config it is:
/ca reload
**Permission:** communalaction.admin



**More images & fine tuning of this page coming soon. I am active on Spigot and will respond to messages quickly. **



# Download
https://www.spigotmc.org/resources/communal-action-innovative-moderation-unique.57130/ (Always updated)
https://www.mc-market.org/resources/7306/