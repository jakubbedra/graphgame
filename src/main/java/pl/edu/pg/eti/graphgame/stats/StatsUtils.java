package pl.edu.pg.eti.graphgame.stats;

import java.sql.Date;

public class StatsUtils {

    public static boolean equalDates(Date d1, Date d2) {
        return d1.getDay() == d2.getDay() &&
                d1.getMonth() == d2.getMonth() &&
                d1.getYear() == d2.getYear();
    }

}
