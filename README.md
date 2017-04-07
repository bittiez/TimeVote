[ ![Discord Support](https://www.mediafire.com/convkey/510b/iw2k26exg0qlf076g.jpg) ](https://discord.gg/p5DAvc6)
[ ![Bugs, Issues, Feature Requests](https://www.mediafire.com/convkey/2320/x80qtabf3auhhjr6g.jpg) ](../../issues)
[ ![Donate](https://www.mediafire.com/convkey/910d/z8160kkzvezi4km6g.jpg) ](https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=THXHQ5287TBA8)


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
- Per world time change, the timevote will only change in the world that was voted for
- Players can only vote from the world that is being voted on
- Disable specific worlds in the config

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
- Add support for votes from multiple worlds at once
- Add option to pay to start a vote
