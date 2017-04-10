package US.bittiez.TimeVote;

/**
 * Created by gamer on 3/29/2017.
 */
public class PERMISSIONS {
    public class ADMIN {
        public static final String RECEIVE_UPDATES = "TimeVote.updates";
        public static final String RELOAD_CONFIG = "TimeVote.reload";
    }

    public class PLAYER {
        public static final String START_VOTE = "TimeVote.start";
        public static final String VOTE = "TimeVote.vote";
    }
}
