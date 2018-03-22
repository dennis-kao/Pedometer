package de.j4velin.pedometer.obj;

import java.util.Calendar;

/**
 * Created by Rajbir on 22/03/18.
 */

public class Day_Step_History {
    private float goal;
    private int calories;
    private int distance;
    private int steps;
    private long day;

    public String getGoal(){
        String rtn = goal + "%";
        return rtn;
    }
    public  void setGoal(){
        this.goal = (this.steps/10000) * 100;
    }
    public int getCalories() {
        return calories;
    }
    public void setCalories(int calories) {
        this.calories = calories;
    }
    public int getDistance() {
        return distance;
    }
    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getSteps() {
        return steps;
    }
    public void setSteps(int Steps) {
        this.steps = Steps;
    }

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
