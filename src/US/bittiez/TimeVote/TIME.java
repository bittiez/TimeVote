package US.bittiez.TimeVote;

public class TIME {
    public static final int DAY = 0;
    public static final int NIGHT = 1;

    public static String timeString(int time) {
        return time == TIME.DAY ? "day" : "night";
    }
}
