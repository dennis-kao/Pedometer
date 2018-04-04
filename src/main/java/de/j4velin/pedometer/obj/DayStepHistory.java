package de.j4velin.pedometer.obj;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by Rajbir on 22/03/18.
 */

public class DayStepHistory extends StepHistory {
    private float goal;
    private long day;


    public float getGoal() {
        return goal;
    }
    public  void setGoal() {
        float temp = ((float) super.getSteps() / (float) 10000) * 100;
        // formatting to only capture up to the 2nd decimal place
        String distString;
        NumberFormat formatter = new DecimalFormat("#.##");
        distString = formatter.format(temp);

        this.goal = Float.parseFloat(distString);

    }
    public long getDay() {
        return day;
    }
    public void setDay(long day) {
        this.day = day;
    }

    public String getDayString(){
        Date date = new Date(this.day);
        DateFormat formatter = new SimpleDateFormat("dd/MM/YYYY");
        return formatter.format(date).toString();
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