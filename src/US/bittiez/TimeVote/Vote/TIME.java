package US.bittiez.TimeVote.Vote;

public class TIME {
    public static final int DAY = 0;
    public static final int NIGHT = 1;
    public static final int CLEAR_WEATHER = 2;

    public static String timeString(int time) {
        if (time == TIME.DAY)
            return "day";
        if (time == TIME.NIGHT)
            return "night";
        if (time == TIME.CLEAR_WEATHER)
            return "clear weather";
        return "";
    }
}
