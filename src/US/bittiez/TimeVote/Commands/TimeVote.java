package US.bittiez.TimeVote.Commands;

import US.bittiez.TimeVote.Config.Configurator;
import US.bittiez.TimeVote.PERMISSIONS;
import US.bittiez.TimeVote.Utils;
import US.bittiez.TimeVote.Vote.TIME;
import US.bittiez.TimeVote.Vote.VOTE_STATUS;
import US.bittiez.TimeVote.Vote.Vote;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TimeVote {
    private static String thisCommmand = "timevote";
    private static List<String> newTimeVoteParams = new ArrayList<>(Arrays.asList(new String[]{
            "new",
            "start",
    }));
    private static List<String> playerVoteParams = new ArrayList<>(Arrays.asList(new String[]{
            "vote",
    }));
    private static List<String> timeVoteTypeParams = new ArrayList<>(Arrays.asList(new String[]{
            "day",
            "night",
            "clear",
    }));
    private Vote vote;
    private Configurator configurator;
    private Plugin plugin;
    private BukkitScheduler scheduler;

    public TimeVote(Vote vote, Configurator configurator, Plugin plugin, BukkitScheduler scheduler) {

        this.vote = vote;
        this.configurator = configurator;
        this.plugin = plugin;
        this.scheduler = scheduler;
    }

    public boolean handleCommand(CommandSender sender, Command cmd, String args[]) {
        if (cmd.getName().equalsIgnoreCase(thisCommmand)) {
            if (args.length > 0) { // Has arguments
                if ((
                        (newTimeVoteParams.contains(args[0]) && timeVoteTypeParams.contains(args[1]))
                                || timeVoteTypeParams.contains(args[0]))
                        && sender.hasPermission(PERMISSIONS.PLAYER.START_VOTE)
                        && sender instanceof Player) {
                    if (configurator.config.getInt("starting_cost", 0) > 0) {
                        newTimeVote(args, (Player) sender);
                    } else {
                        newTimeVote(args, (Player) sender);
                    }
                }
                if (args[0].equalsIgnoreCase("reload") && sender.hasPermission(PERMISSIONS.ADMIN.RELOAD_CONFIG)) {
                    reloadConfig(sender);
                } else if (playerVoteParams.contains(args[0]) && sender.hasPermission(PERMISSIONS.PLAYER.VOTE)) {
                    if (vote.getIsRunning())
                        playerVote((Player) sender);
                    else if (!vote.getIsRunning()) // Vote is not running
                        sender.sendMessage(Utils.colorize(configurator.config.getString("err_not_in_progress")));
                }
            } else { // No arguments sent
                if (sender.hasPermission(PERMISSIONS.PLAYER.VOTE) && sender instanceof Player && vote.getIsRunning())
                    playerVote((Player) sender);
                else if (!vote.getIsRunning()) // Vote is not running
                    sender.sendMessage(Utils.colorize(configurator.config.getString("err_not_in_progress")));
                else
                    usage(sender);
            }
            return true;
        }
        return false;
    }

    public void newTimeVote(String args[], Player sender) {
        if (vote.getIsRunning()) {
            sender.sendMessage(Utils.colorize(configurator.config.getString("err_vote_in_progress")));
            return;
        }

        String voteTypeArg;
        if (timeVoteTypeParams.contains(args[0]))
            voteTypeArg = args[0];
        else
            voteTypeArg = args[1];

        //The vote is not running, lets set up the vote options
        if (voteTypeArg.equalsIgnoreCase("day"))
            vote.setDayNight(TIME.DAY);
        else if (voteTypeArg.equalsIgnoreCase("night"))
            vote.setDayNight(TIME.NIGHT);
        else if (voteTypeArg.equalsIgnoreCase("clear"))
            vote.setDayNight(TIME.CLEAR_WEATHER);
        vote.setWorld(sender.getWorld());

        for (String disabledWorld : configurator.config.getStringList("disabled_worlds")) {
            if (disabledWorld.equals(vote.getWorld().getName())) {
                sender.sendMessage(Utils.colorize(configurator.config.getString("err_world_disabled")));
                vote.reset();
                return;
            }
        }

        vote.setRunning(true);
        vote.addVote(sender);
        scheduler.scheduleSyncDelayedTask(plugin, () -> {
            vote.setRunning(false);
            vote.setPassed(vote.getVotes() >= vote.getRequiredVotes((float) configurator.config.getDouble("vote_percent", 0.20)));

            String voteEnded = configurator.config.getString("vote_ended");
            voteEnded = Utils.convertPlaceHolders(voteEnded,
                    "",
                    vote.getDayNight(),
                    vote.getRequiredVotes((float) configurator.config.getDouble("vote_percent", 0.20)),
                    vote.getVotes(),
                    Integer.parseInt(configurator.config.getLong("vote_length") + ""),
                    vote.getWorld().getName(),
                    vote.getPassed() ? "passed" : "failed"
            );

            Utils.announceToWorld(Utils.colorize(voteEnded), vote.getWorld());

            if (vote.getPassed())
                Utils.setWorldTime(vote.getWorld(), vote.getDayNight(), configurator, plugin);
            vote.reset();
        }, configurator.config.getLong("vote_length") * 20L); //Vote length in seconds * 20 ticks / second

        for (String m : configurator.config.getStringList("starting_vote"))
            Utils.announceToWorld(Utils.colorize(
                    Utils.convertPlaceHolders(m,
                            sender.getName(),
                            vote.getDayNight(),
                            vote.getRequiredVotes((float) configurator.config.getDouble("vote_percent", 0.20)),
                            vote.getVotes(),
                            Integer.parseInt(configurator.config.getLong("vote_length") + ""),
                            vote.getWorld().getName(),
                            vote.getPassed() ? "passed" : "failed"
                    )
            ), vote.getWorld());

    }

    public void playerVote(Player sender) {
        int voteStatus = vote.addVote(sender).status;
        if (voteStatus == VOTE_STATUS.VOTED) {
            sender.sendMessage(Utils.colorize(configurator.config.getString("you_voted")));
            String voteAnnouncement = Utils.colorize(configurator.config.getString("you_voted_announcement")
                    .replace("[USERNAME]", sender.getName())
                    .replace("[VOTES]", vote.getVotes() + "")
                    .replace("[REQVOTES]", "" + vote.getRequiredVotes((float) configurator.config.getDouble("vote_percent", 0.20))));
            if (voteAnnouncement.length() > 0)
                Utils.announceToWorld(voteAnnouncement, vote.getWorld());
        } else if (voteStatus == VOTE_STATUS.ALREADY_VOTED) {
            sender.sendMessage(Utils.colorize(configurator.config.getString("you_already_voted")));
        } else if (voteStatus == VOTE_STATUS.WRONG_WORLD) {
            sender.sendMessage(Utils.colorize(configurator.config.getString("err_wrong_world").replace("[WORLD]", vote.getWorld().getName())));
        }
    }

    public void reloadConfig(CommandSender sender) {
        configurator.reloadPluginDefaultConfig(plugin);
        sender.sendMessage(ChatColor.GOLD + "TimeVote Config reloaded!");
    }


    public void usage(CommandSender sender) {
        if (sender.hasPermission(PERMISSIONS.ADMIN.RELOAD_CONFIG))
            sender.sendMessage(Utils.genUsageString("/TimeVote reload", "Reloads the config file."));

        if (sender.hasPermission(PERMISSIONS.PLAYER.START_VOTE))
            sender.sendMessage(Utils.genUsageString("/TimeVote (day|night|clear)", "Starts a new time vote."));
    }

}
