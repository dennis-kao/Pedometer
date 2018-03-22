package de.j4velin.pedometer.obj;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 * Created by calimr on 2018-03-06.
 */

public class Week_Step_History {
    private int totalSteps = 0;
    private int avgSteps = 0;
    private int distance = 0;
    private int totalCalBurned = 0;
    private long dtEnd = 0;
    private long dtStart = 0;
    private long bestDay = 0;

    public long getBestDay() {
        return bestDay;
    }

    public void setBestDay(long bestDay) {
        this.bestDay = bestDay;
    }

    public int getTotalSteps() {
        return totalSteps;
    }

    public void setTotalSteps(int totalSteps) {
        this.totalSteps = totalSteps;
        this.setAvgSteps();
        this.setDistance();
    }

    public int getAvgSteps() {
        long numDays = 7;
        numDays = (this.dtEnd - this.dtStart)/ TimeUnit.DAYS.toMillis(1);
        if (numDays == 0) numDays = 1;
        return (avgSteps > 0) ? avgSteps : (int)(totalSteps/numDays);
    }

    public void setAvgSteps(int avgSteps) {
         this.avgSteps = avgSteps;
    }

    public void setAvgSteps() {
        this.avgSteps = this.totalSteps/7;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public void setDistance() {
        this.distance = this.totalSteps; // multiplied by stride length
    }

    public int getTotalCalBurned() {
        return totalCalBurned;
    }

    public void setTotalCalBurned(int totalCalBurned) {
        this.totalCalBurned = totalCalBurned;
    }

    public long getDtEnd() {
        return dtEnd;
    }

    public void setDtEnd(long dtEnd) {
        this.dtEnd = dtEnd;
    }

    public String getDtEndString(){
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(0);
        cal.setTimeInMillis(this.dtEnd);
        String rtn = "   " + cal.get(Calendar.DAY_OF_MONTH) + "/" + (cal.get(Calendar.MONTH)+1) + "/" + cal.get(Calendar.YEAR);
        return rtn;
    }

    public long getDtStart() {
        return dtStart;
    }

    public String getDtStartString(){
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(0);
        cal.setTimeInMillis(this.dtStart);
        String rtn = "   " + cal.get(Calendar.DAY_OF_MONTH) + "/" + (cal.get(Calendar.MONTH)+1) + "/" + cal.get(Calendar.YEAR);
        return rtn;
    }

    public void setDtStart(long dtStart) {
        this.dtStart = dtStart;
    }
}
