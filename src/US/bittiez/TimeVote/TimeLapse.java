package US.bittiez.TimeVote;

import org.bukkit.World;
import org.bukkit.plugin.Plugin;

public class TimeLapse {
    private World world;
    private Long setTime;
    private Plugin plugin;
    private Long Increment;


    public TimeLapse(World world, Long setTime, Plugin plugin, Long speed) {
        this.world = world;
        this.setTime = setTime;
        this.plugin = plugin;
        this.Increment = speed;
    }

    public void Start() {
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                Increment();
            }
        }, 5L);
    }

    public void Increment() {
        Long worldTime = world.getTime();

        if (worldTime >= setTime - Increment - 1 && worldTime <= setTime + Increment + 1)
            world.setTime(setTime);
        else {
            world.setTime(worldTime + Increment);
            Start();
        }
    }
}
