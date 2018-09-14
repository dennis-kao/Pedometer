package de.j4velin.pedometer.obj;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Comparator;

/**
 * Created by Rajbir on 22/03/18.
 */

public class DayStepHistory extends StepHistory {
    private float goalCompleted;
    private long day;
    public static String formatter = "EEEEEE, MMMM d, yyyy";

    public float getGoalCompleted() {
        return goalCompleted;
    }
    public  void setGoal() {
        float temp = ((float) super.getSteps() / (float) 10000) * 100;
        // formatting to only capture up to the 2nd decimal place
        String distString;
        NumberFormat formatter = new DecimalFormat("#.##");
        distString = formatter.format(temp);

        this.goalCompleted = Float.parseFloat(distString);

    }
    public long getDay() {
        return day;
    }
    public void setDay(long day) {
        this.day = day;
    }

    @Override
    public String toString(){
        return getDateText(day, formatter);
    }

    public String getMonth() {
        return getDateText(day, "MMMM");
    }

    public String getDayOfWeek() {
        return getDateText(day, "EEEEEE");
    }

    public String getNumDayString() {
        return getDateText(day, "d");
    }

    /*Comparator for sorting the list by day*/
    public static Comparator<DayStepHistory> DayDateComparator = new Comparator<DayStepHistory>() {

        public int compare(DayStepHistory m1, DayStepHistory m2) {
            int day1 = (int) m1.getDay();
            int day2 = (int) m2.getDay();

            return day1 - day2;

        }};

    /*Comparator for sorting the list by goalCompleted achieved*/
    public static Comparator<DayStepHistory> DayStepComparator = new Comparator<DayStepHistory>() {

        public int compare(DayStepHistory m1, DayStepHistory m2) {
            int day1 = (int) m1.getSteps();
            int day2 = (int) m2.getSteps();

            return day1 - day2;
        }};
}