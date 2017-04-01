package US.bittiez.TimeVote;

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

    //SETTERS AND GETTERS
    public void setDayNight(int daynight) {
        this.daynight = daynight;
    }

    public int getDayNight() {
        return this.daynight;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public World getWorld() {
        return world;
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

    public Boolean addVote(Player p) {
        if(UUIDVotes.contains(p.getUniqueId()))
            return false;
        UUIDVotes.add(p.getUniqueId());
        this.votes++;
        return true;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    public int getRequiredVotes(float percent) {
        int playerCount = world.getPlayerCount();
        return (int) Math.ceil(playerCount * (percent / 100.0f));
    }

    public Boolean getPassed() {
        return passed;
    }

    public void setPassed(Boolean passed) {
        this.passed = passed;
    }
}
