[ ![Discord Support](https://www.mediafire.com/convkey/1f30/84f194magcxff186g.jpg) ](https://discord.gg/p5DAvc6)
[ ![Bugs, Issues, Feature Requests](https://www.mediafire.com/convkey/3860/99n15b2cbgvnp416g.jpg) ](../../issues)
[ ![Donate](https://www.mediafire.com/convkey/3ac7/eurlt0tntrc95zh6g.jpg) ](https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=THXHQ5287TBA8)


# TimeVote

Spigot 1.11 plugin to allow players to vote to change to either day or night time.

# Description
Note: This requires Java 1.8+

Players will vote to change the time in the current world to day or night

### Features:
- Require a certain percent of players to vote for it to pass
- Configurable day/night times
- Configurable messages
- Configurable vote length
- Configurable percent of players required to pass the vote
- Optional pay to start a vote `Not yet implemented`

# Usage

- A player starts a vote with `/TimeVote [new|start] [day|night]`
- Players in the same world are notified about a time vote
- Players can use `/TimeVote vote` to vote for whatever the vote was started for (day / night)

# Permissions
[View permissions here](../../blob/master/src/plugin.yml)

# Installation

- Place the jar file in your plugins folder
- Restart your server
- Edit the configuration options
- Use `/TimeVote reload`


# Configuration
[View default configuration file here](../../blob/master/src/config.yml)

# To Do
- Add a message telling you how long the vote has left
- Check on player vote if the player is in the vote world
- Add support for votes from multiple worlds at once
- Add config to disable certain worlds
- Auto vote for person starting vote
- Add [WORLD] to config options