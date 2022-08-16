import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class User implements Comparable<User> {
    private String username;
    private long time;
    private String level;
    private int chancesOfUser;
    private String dateAndTime;


    public User() {
    }

    public User(String username, long time, String level, int chancesOfUser, String dateAndTime) {
        this.username = username;
        this.time = time;
        this.level = level;
        this.chancesOfUser = chancesOfUser;
        this.dateAndTime = dateAndTime;
    }

    public String getDate() {

        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd,hh:mm");

        return dateFormat.format(date);
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public int getChancesOfUser() {
        return chancesOfUser;
    }

    public void setChancesOfUser(int chancesOfUser) {
        this.chancesOfUser = chancesOfUser;
    }


    public String getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(String dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    @Override
    public String toString() {
        return username + "      " + dateAndTime + "       " + chancesOfUser + "                " + time + " s";
    }

    @Override
    public int compareTo(User o) {
//        if (this.getTime() < o.getTime()) return -1;
//        if (this.getTime() > o.getTime()) return 1;
//        else return 0;

        int compareResult = Long.compare(this.getTime(), o.getTime());
        if (compareResult == 0) {
            compareResult = Integer.compare(this.getChancesOfUser(), o.getChancesOfUser());
        }
        return compareResult;
    }
}

