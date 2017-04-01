# Basic Ideas

Player can initiate a vote with /timevote new (Day | Night)

**World** announcement occurs saying a vote is going on for (Day | Night), use /timevote vote  
Voting lasts 2(configurable) minutes?


# To-Do
- Add announcement delay in config
- Add confirm message to pay to start vote


## Player confirmation
If payment to start a vote is enabled, the process will look something like:
- `Player` -> /timevote new day
- `Server` checks if vote is already running in that world
- `Server` saves `player` into temp config(not saved, just in memory) with vote type, time, and world
- `Server` -> Create a new TimeVote will cost $5000, if you want to continue type /TimeVote confirm
- If `player` types /timevote confirm within `2 minutes` server checks if timevote is running:
  - If it is running, tell the `player` it's been cancelled
  - If it is not running, make the `player` pay and start the vote!
- During vote the server will announce every `30(Configurable)` seconds until vote is done
- `Players` will type `/TimeVote vote` to vote for it