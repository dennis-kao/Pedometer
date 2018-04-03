package de.j4velin.pedometer.obj;

import java.util.Calendar;
import java.util.Comparator;

/**
 * Created by Rajbir on 22/03/18.
 */

public class DayStepHistory extends StepHistory {
    private float goal;
    private long day;

    public String getGoalStr(){
        if(goal == 0){
            setGoal();
        }
        String rtn = goal + "%";
        return rtn;
    }
    public float getGoal() {
        return goal;
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


    /*Comparator for sorting the list by day*/
    public static Comparator<DayStepHistory> DayDateComparator = new Comparator<DayStepHistory>() {

        public int compare(DayStepHistory m1, DayStepHistory m2) {
            int day1 = (int) m1.getDay();
            int day2 = (int) m2.getDay();

            return day1 - day2;

        }};

    /*Comparator for sorting the list by goal achieved*/
    public static Comparator<DayStepHistory> DayGoalComparator = new Comparator<DayStepHistory>() {

        public int compare(DayStepHistory m1, DayStepHistory m2) {
            int day1 = (int) m1.getGoal();
            int day2 = (int) m2.getGoal();

            return day1 - day2;

        }};
}
