package US.bittiez.TimeVote;

import US.bittiez.TimeVote.Commands.TimeVote;
import US.bittiez.TimeVote.Config.Configurator;
import US.bittiez.TimeVote.UpdateChecker.UpdateChecker;
import US.bittiez.TimeVote.UpdateChecker.UpdateStatus;
import US.bittiez.TimeVote.Vote.Vote;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.logging.Logger;


public class main extends JavaPlugin {
    public static Logger log;

    private Vote vote = new Vote();
    private Configurator configurator = new Configurator();
    private Boolean startingCost = true;
    private BukkitScheduler scheduler = getServer().getScheduler();
    private TimeLapse timeLapse;
    private TimeVote commandHandler;

    private static String genVersionOutdatedMessage(String version, String updatedVersion) {
        return "Your version(" + version + ") of " + ChatColor.GOLD + " TimeVote " + ChatColor.RESET + "is not up to date(" + updatedVersion + "), you can get the latest version at https://github.com/bittiez/TimeVote/releases";
    }

    @Override
    public void onEnable() {
        log = getLogger();
        configurator.setConfig(this);
        configurator.saveDefaultConfig(this);
        commandHandler = new TimeVote(vote, configurator, this, scheduler);

        startingCost = configurator.config.getInt("starting_cost", 500) > 0;

        UpdateStatus update = new UpdateChecker("https://github.com/bittiez/TimeVote/raw/master/src/plugin.yml", getDescription().getVersion()).getStatus();
        if (update.HasUpdate) {
            log.info(genVersionOutdatedMessage(update.LocalVersion, update.RemoteVersion));
        }
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String args[]) {
        return commandHandler.handleCommand(sender, cmd, args);
    }

}
