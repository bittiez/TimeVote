package US.bittiez.TimeVote;

import US.bittiez.TimeVote.UpdateChecker.UpdateChecker;
import US.bittiez.TimeVote.UpdateChecker.UpdateStatus;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.logging.Logger;


public class main extends JavaPlugin {
    public static boolean debug = true;
    public static Logger log;
    public FileConfiguration config = getConfig();

    private Boolean startingCost = true;
    private BukkitScheduler scheduler = getServer().getScheduler();

    @Override
    public void onEnable() {
        log = getLogger();
        createConfig();
        startingCost = config.getInt("starting_cost", 500) > 0;

        UpdateStatus update = new UpdateChecker("https://github.com/bittiez/TimeVote/raw/master/src/plugin.yml", getDescription().getVersion()).getStatus();
        if (update.HasUpdate) {
            genVersionOutdatedMessage(update.LocalVersion, update.RemoteVersion);
        }
    }


    public boolean onCommand(CommandSender sender, Command cmd, String label, String args[]) {
        if (cmd.getName().equalsIgnoreCase("TimeVote")) {
            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("reload") && sender.hasPermission(PERMISSIONS.ADMIN.RELOAD_CONFIG)) {
                    this.reloadConfig();
                    config = getConfig();
                    sender.sendMessage(ChatColor.GOLD + "TimeVote Config reloaded!");
                    return true;
                }
                if ((args[0].equalsIgnoreCase("new") || args[0].equalsIgnoreCase("start")) && sender.hasPermission(PERMISSIONS.PLAYER.START_VOTE)) {
                    if (args.length > 1 && (args[1].equalsIgnoreCase("day") || args[1].equalsIgnoreCase("night"))) {
                        int time = 0;
                        if(args[1].equalsIgnoreCase("day"))
                            time = TIME.DAY;
                        else if(args[1].equalsIgnoreCase("night"))
                            time = TIME.NIGHT;
                        String timeString = timeString(time);

                        if (startingCost) {

                        } else {
                            for (Player p : ((Player) sender).getWorld().getPlayers())
                                for (String m : config.getStringList("starting_vote")) {
                                    sender.sendMessage(
                                            m.replace("[USERNAME]", sender.getName())
                                            .replace("[DAYNIGHT]", timeString)
                                    );
                                }
                        }
                    } else {
                        sender.sendMessage(genUsageString("/TimeVote (new|start) (day|night)", "Starts a new time vote."));
                    }
                    return true;
                }
            } else {
                if (sender.hasPermission(PERMISSIONS.ADMIN.RELOAD_CONFIG))
                    sender.sendMessage(genUsageString("/TimeVote reload", "Reloads the config file."));

                if (sender.hasPermission(PERMISSIONS.PLAYER.START_VOTE))
                    sender.sendMessage(genUsageString("/TimeVote (new|start) (day|night)", "Starts a new time vote."));
                return true;
            }
            return false;
        }
        return false;
    }

    private void setWorldTime(World world, int time) {
        if (time == TIME.DAY) {
            world.setTime(config.getLong("day"));
        } else if (time == TIME.NIGHT) {
            world.setTime(config.getLong("night"));
        } else {
            log.warning("[M1]There was an error trying to set the world time.");
        }
    }

    private String genUsageString(String command, String description) {
        return ChatColor.DARK_BLUE + "[TimeVote] " + ChatColor.BLUE + command + ChatColor.LIGHT_PURPLE + "  || " + description;
    }

    private String genVersionOutdatedMessage(String version, String updatedVersion) {
        return "Your version(" + version + ") of " + ChatColor.GOLD + " TimeVote " + ChatColor.RESET + "is not up to date(" + updatedVersion + "), you can get the latest version at https://github.com/bittiez/TimeVote/releases";
    }

    private void createConfig() {
        config.options().copyDefaults();
        saveDefaultConfig();
    }

    private static String timeString(int time) {
        return time == TIME.DAY ? "day" : "night";
    }

    private class TIME {
        public static final int DAY = 0;
        public static final int NIGHT = 1;
    }
}
