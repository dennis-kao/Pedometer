package de.j4velin.pedometer.obj;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Rajbir on 22/03/18.
 */

public class DayStepHistory extends StepHistory {
    private float goal;
    private long day;
    final private DateFormat formatter = new SimpleDateFormat("dd/MM/YYYY");

    public String getGoal(){
        String rtn = goal + "%";
        return rtn;
    }
    public  void setGoal(){
        this.goal = ( (float) super.getSteps() / (float) 10000) * 100;}

    public long getDay() {
        return day;
    }
    public void setDay(long day) {
        this.day = day;
    }

    public String getDayString(){
        String dateString;
        Date dateObj = new Date(this.day);
        dateString = this.formatter.format(dateObj);
        return dateString;
    }
}
