package US.bittiez.TimeVote;

import US.bittiez.TimeVote.Config.Configurator;
import US.bittiez.TimeVote.UpdateChecker.UpdateChecker;
import US.bittiez.TimeVote.UpdateChecker.UpdateStatus;
import US.bittiez.TimeVote.Vote.VOTE_STATUS;
import US.bittiez.TimeVote.Vote.Vote;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.logging.Logger;


public class main extends JavaPlugin {
    public static boolean debug = true;
    public static Logger log;

    private Vote vote = new Vote();
    private Configurator configurator = new Configurator();
    private Boolean startingCost = true;
    private BukkitScheduler scheduler = getServer().getScheduler();

    @Override
    public void onEnable() {
        log = getLogger();
        configurator.setConfig(this);
        configurator.saveDefaultConfig(this);
        startingCost = configurator.config.getInt("starting_cost", 500) > 0;

        UpdateStatus update = new UpdateChecker("https://github.com/bittiez/TimeVote/raw/master/src/plugin.yml", getDescription().getVersion()).getStatus();
        if (update.HasUpdate) {
            genVersionOutdatedMessage(update.LocalVersion, update.RemoteVersion);
        }
    }


    public boolean onCommand(CommandSender sender, Command cmd, String label, String args[]) {
        if (cmd.getName().equalsIgnoreCase("TimeVote")) {
            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("reload") && sender.hasPermission(PERMISSIONS.ADMIN.RELOAD_CONFIG)) {
                    configurator.reloadPluginDefaultConfig(this);
                    startingCost = configurator.config.getInt("starting_cost", 500) > 0;
                    sender.sendMessage(ChatColor.GOLD + "TimeVote Config reloaded!");
                    return true;
                }
                if ((args[0].equalsIgnoreCase("new") || args[0].equalsIgnoreCase("start")) && sender.hasPermission(PERMISSIONS.PLAYER.START_VOTE) && sender instanceof Player) {
                    if (args.length > 1 && (args[1].equalsIgnoreCase("day") || args[1].equalsIgnoreCase("night"))) {
                        if (vote.getIsRunning()) {
                            sender.sendMessage(colorize(configurator.config.getString("err_vote_in_progress")));
                            return true;
                        }

                        //The vote is not running, lets set up the vote options
                        if (args[1].equalsIgnoreCase("day"))
                            vote.setDayNight(TIME.DAY);
                        else if (args[1].equalsIgnoreCase("night"))
                            vote.setDayNight(TIME.NIGHT);

                        vote.setWorld(((Player) sender).getWorld());

                        if (startingCost) {
                            //CHECK IF PAYMENT GOES THROUGH
                            startVote((Player) sender);
                        } else {
                            if (debug)
                                log.info(sender.getName() + " started a new timevote!");
                            startVote((Player) sender);

                        }
                    } else {
                        sender.sendMessage(genUsageString("/TimeVote (new|start) (day|night)", "Starts a new time vote."));
                    }
                    return true;
                }
                if (args[0].equalsIgnoreCase("vote") && sender.hasPermission(PERMISSIONS.PLAYER.VOTE) && sender instanceof Player) {
                    if (!vote.getIsRunning()) {
                        sender.sendMessage(colorize(configurator.config.getString("err_not_in_progress")));
                        return true;
                    }
                    int voteStatus = vote.addVote((Player) sender).status;
                    if (voteStatus == VOTE_STATUS.VOTED) {
                        sender.sendMessage(colorize(configurator.config.getString("you_voted")));
                    } else if(voteStatus == VOTE_STATUS.ALREADY_VOTED) {
                        sender.sendMessage(colorize(configurator.config.getString("you_already_voted")));
                    } else if(voteStatus == VOTE_STATUS.WRONG_WORLD) {
                        sender.sendMessage(colorize(configurator.config.getString("err_wrong_world").replace("[WORLD]", vote.getWorld().getName())));
                    }
                    return true;
                }
                return true;
            } else {
                if (sender.hasPermission(PERMISSIONS.ADMIN.RELOAD_CONFIG))
                    sender.sendMessage(genUsageString("/TimeVote reload", "Reloads the config file."));

                if (sender.hasPermission(PERMISSIONS.PLAYER.START_VOTE))
                    sender.sendMessage(genUsageString("/TimeVote (new|start) (day|night)", "Starts a new time vote."));
                return true;
            }
        }
        return false;
    }

    private void startVote(Player sender) {
        vote.setRunning(true);

        //Set the end vote time -------------------------------------------------
        scheduler.scheduleSyncDelayedTask(this, new Runnable() {
            @Override
            public void run() {
                vote.setRunning(false);
                vote.setPassed(vote.getVotes() >= vote.getRequiredVotes((float) configurator.config.getDouble("vote_percent", 0.20)));

                String voteEnded = configurator.config.getString("vote_ended");
                voteEnded = voteEnded.replace("[STATUS]", vote.getPassed() ? "passed" : "failed")
                        .replace("[VOTES]", "" + vote.getVotes())
                        .replace("[REQVOTES]", "" + vote.getRequiredVotes((float) configurator.config.getDouble("vote_percent", 0.20)))
                        .replace("[DAYNIGHT]", TIME.timeString(vote.getDayNight()));
                voteEnded = colorize(voteEnded);

                for (Player p : vote.getWorld().getPlayers())
                    p.sendMessage(voteEnded);

                if (vote.getPassed()) {
                    setWorldTime(vote.getWorld(), vote.getDayNight());
                }
                vote.reset();
            }
        }, configurator.config.getLong("vote_length") * 20L); //Vote length in seconds * 20 ticks / second

        //Let all players know about the vote
        String timeString = TIME.timeString(vote.getDayNight()); //Either "day" or "night"
        for (Player p : ((Player) sender).getWorld().getPlayers())
            for (String m : configurator.config.getStringList("starting_vote")) {
                p.sendMessage(colorize(
                        m.replace("[USERNAME]", sender.getName())
                                .replace("[DAYNIGHT]", timeString)
                                .replace("[VOTES]", "" + vote.getRequiredVotes((float) configurator.config.getDouble("vote_percent", 0.20)))
                                .replace("[TIME]", configurator.config.getLong("vote_length") + "")
                ));
            }

    }

    private String colorize(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    private void setWorldTime(World world, int time) {
        if (time == TIME.DAY) {
            world.setTime(configurator.config.getLong("day"));
        } else if (time == TIME.NIGHT) {
            world.setTime(configurator.config.getLong("night"));
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

}
