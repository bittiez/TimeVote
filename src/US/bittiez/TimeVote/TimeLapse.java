package US.bittiez.TimeVote;

import org.bukkit.World;
import org.bukkit.plugin.Plugin;

public class TimeLapse {
    private World world;
    private Long setTime;
    private Plugin plugin;
    private Long Increment = 100L;


    public TimeLapse(World world, Long setTime, Plugin plugin){
        this.world = world;
        this.setTime = setTime;
        this.plugin = plugin;
    }

    public void Start(){
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                Increment();
            }
        }, 10L);
    }

    public void Increment(){
        Long worldTime = world.getTime();
        if(worldTime >= setTime - (Increment/2) || worldTime  <= setTime + (Increment/2))
            world.setTime(setTime);
        else {
            world.setTime(worldTime + Increment);
            Start();
        }
    }
}
