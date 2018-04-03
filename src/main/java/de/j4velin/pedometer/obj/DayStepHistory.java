package de.j4velin.pedometer.obj;

import java.util.Calendar;

/**
 * Created by Rajbir on 22/03/18.
 */

public class DayStepHistory extends StepHistory {
    private float goal;
    private long day;

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
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(0);
        cal.setTimeInMillis(this.day);
        String rtn = "   " + cal.get(Calendar.DAY_OF_MONTH) + "/" + (cal.get(Calendar.MONTH)+1) + "/" + cal.get(Calendar.YEAR);
        return rtn;
    }
}
