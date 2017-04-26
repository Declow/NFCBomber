package dk.sdu.mmmi.ap.g14.nfcbomber.database.objects;

import java.util.Date;

public class HighscoreItem {
    private int userTime;
    private int bombTime;
    private Date dateOfScore;

    public HighscoreItem(int userTime, int bombTime, Date dateOfScore) {
        this.userTime = userTime;
        this.bombTime = bombTime;
        this.dateOfScore = dateOfScore;
    }

    public int getUserTime() {
        return userTime;
    }

    public int getBombTime() {
        return bombTime;
    }

    public Date getDateOfScore() {
        return dateOfScore;
    }
}
