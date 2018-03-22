package de.j4velin.pedometer.obj;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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
        int numDays;
        numDays = (int) ((this.dtEnd - this.dtStart)/ TimeUnit.DAYS.toMillis(1));
        if (numDays == 0) numDays = 1;
        this.avgSteps = this.totalSteps/numDays;
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

    public String getDtEndAsDateString() {
        String dtEnd;
        Date date = new Date(this.dtEnd);
        DateFormat formatter = new SimpleDateFormat("dd/MM/YYYY");
        dtEnd = formatter.format(date).toString();
        return dtEnd;
    }

    public long getDtStart() {
        return dtStart;
    }

    public void setDtStart(long dtStart) {
        this.dtStart = dtStart;
    }

    public String getDtStartAsDateString() {
        String dtStart;
        Date date = new Date(this.dtStart);
        DateFormat formatter = new SimpleDateFormat("dd/MM/YYYY");
        dtStart = formatter.format(date).toString();
        return dtStart;
    }
}
