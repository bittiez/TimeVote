package US.bittiez.TimeVote;

import US.bittiez.TimeVote.Config.Configurator;
import US.bittiez.TimeVote.Vote.TIME;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class Utils {
    public static String colorize(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public static void announceToWorld(String announcement, World world) {
        for (Player p : world.getPlayers())
            p.sendMessage(announcement);
    }

    public static String convertPlaceHolders(String text, String UserName, int daynight, int requiredVotes, int currentVotes, int time, String world, String status) {
        text = text
                .replace("[USERNAME]", UserName)
                .replace("[DAYNIGHT]", TIME.timeString(daynight))
                .replace("[REQVOTES]", requiredVotes + "")
                .replace("[VOTES]", currentVotes + "")
                .replace("[TIME]", time + "")
                .replace("[WORLD]", world)
                .replace("[STATUS]", status);

        return text;
    }

    public static String genUsageString(String command, String description) {
        return ChatColor.DARK_BLUE + "[TimeVote] " + ChatColor.BLUE + command + ChatColor.LIGHT_PURPLE + "  || " + description;
    }

    public static void setWorldTime(World world, int time, Configurator configurator, Plugin plugin) {
        TimeLapse timeLapse;
        if (time == TIME.DAY) {
            timeLapse = new TimeLapse(world, configurator.config.getLong("day"), plugin, configurator.config.getLong("time_lapse_speed", 300L));
            timeLapse.Start();
        } else if (time == TIME.NIGHT) {
            timeLapse = new TimeLapse(world, configurator.config.getLong("night"), plugin, configurator.config.getLong("time_lapse_speed", 300L));
            timeLapse.Start();
        } else if (time == TIME.CLEAR_WEATHER) {
            world.setStorm(false);
        } else {
            main.log.warning("[M1]There was an error trying to set the world time.");
        }
    }
}
