package US.bittiez.TimeVote.Vote;

import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

public class Vote {
    private Boolean isRunning = false;
    private World world;
    private int daynight;
    private int votes;
    private Boolean passed = false;
    private ArrayList<UUID> UUIDVotes = new ArrayList<UUID>();


    public void reset() {
        world = null;
        setDayNight(TIME.DAY);
        setRunning(false);
        setVotes(0);
        setPassed(false);
        UUIDVotes = new ArrayList<UUID>();
    }

    public int getDayNight() {
        return this.daynight;
    }

    //SETTERS AND GETTERS
    public void setDayNight(int daynight) {
        this.daynight = daynight;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public void setRunning(Boolean isRunning) {
        this.isRunning = isRunning;
    }

    public Boolean getIsRunning() {
        return isRunning;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    public VoteStatus addVote(Player p) {
        if (UUIDVotes.contains(p.getUniqueId()))
            return new VoteStatus(VOTE_STATUS.ALREADY_VOTED);
        if (p.getWorld() != world)
            return new VoteStatus(VOTE_STATUS.WRONG_WORLD);
        UUIDVotes.add(p.getUniqueId());
        this.votes++;
        return new VoteStatus(VOTE_STATUS.VOTED);
    }

    public int getRequiredVotes(float percent) {
        int playerCount = world.getPlayers().size();
        return (int) Math.floor(playerCount * percent);
    }

    public Boolean getPassed() {
        return passed;
    }

    public void setPassed(Boolean passed) {
        this.passed = passed;
    }

    public class VoteStatus {
        public int status;

        public VoteStatus(int VOTE_STATUS) {
            status = VOTE_STATUS;
        }
    }
}
