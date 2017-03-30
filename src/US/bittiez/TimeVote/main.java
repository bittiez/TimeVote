package US.bittiez.TimeVote;

import US.bittiez.TimeVote.UpdateChecker.UpdateChecker;
import US.bittiez.TimeVote.UpdateChecker.UpdateStatus;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.logging.Logger;


public class main extends JavaPlugin {
    public static boolean debug = true;
    public static Logger log;
    public FileConfiguration config = getConfig();

    private BukkitScheduler scheduler = getServer().getScheduler();

    @Override
    public void onEnable() {
        log = getLogger();
        createConfig();
        UpdateStatus update = new UpdateChecker("https://github.com/bittiez/TimeVote/raw/master/src/plugin.yml", getDescription().getVersion()).getStatus();
        if(update.HasUpdate){
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
            } else {
                sender.sendMessage("/TimeVote usage: /TimeVote ( reload )");
            }
            return false;
        }
        return false;
    }


    private String genVersionOutdatedMessage(String version, String updatedVersion) {
        return "Your version(" + version + ") of " + ChatColor.GOLD + " TimeVote " + ChatColor.RESET + "is not up to date(" + updatedVersion + "), you can get the latest version at https://github.com/bittiez/TimeVote/releases or https://www.spigotmc.org/resources/totaltracker.38304/";
    }

    private void createConfig() {
        config.options().copyDefaults();
        saveDefaultConfig();
    }
}
