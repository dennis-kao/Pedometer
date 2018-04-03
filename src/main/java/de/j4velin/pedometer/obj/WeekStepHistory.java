package de.j4velin.pedometer.obj;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by calimr on 2018-03-06.
 */

public class WeekStepHistory extends StepHistory{
    private int avgSteps = 0;
    private long dtEnd = 0;
    private long dtStart = 0;
    private long bestDay = 0;
    private int numDays = 7;
    final private DateFormat formatter = new SimpleDateFormat("dd/MM/YYYY");

    public long getBestDay() {
        return bestDay;
    }

    public void setBestDay(long bestDay) {
        this.bestDay = bestDay;
    }

    public String getBestDayAsDateString() {
        String bestDay;
        Date date = new Date(this.bestDay);
        bestDay = this.formatter.format(date).toString();
        return bestDay;
    }

    public void setTotalSteps(int totalSteps) {
        super.setTotalSteps(totalSteps);
        this.setAvgSteps();
        super.setDistance(totalSteps);
    }

    public int getAvgSteps() {
        return (this.avgSteps > 0) ? this.avgSteps : (int)(getSteps()/this.numDays);
    }

    public void setAvgSteps(int avgSteps) {
         this.avgSteps = avgSteps;
    }

    public void setAvgSteps() {
        this.avgSteps = super.getSteps()/this.numDays;
    }

    public long getDtEnd() {
        return dtEnd;
    }

    public void setDtEnd(long dtEnd) {
        this.dtEnd = dtEnd;
        this.numDays = (int) ((this.dtEnd - this.dtStart) / TimeUnit.DAYS.toMillis(1));
        this.numDays = this.numDays > 0 ? this.numDays : 1;
    }

    public String getDtEndAsDateString() {
        String dtEnd;
        Date date = new Date(this.dtEnd);
        dtEnd = this.formatter.format(date).toString();
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
        dtStart = this.formatter.format(date).toString();
        return dtStart;
    }
}
