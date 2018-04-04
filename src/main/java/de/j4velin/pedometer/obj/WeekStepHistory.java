package de.j4velin.pedometer.obj;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
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
    private double stdDev;
    private double median;
    private DateFormat formatter = new SimpleDateFormat("dd/MM/YYYY");

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
        return (avgSteps > 0) ? avgSteps : (int)(getSteps()/getNumDays());
    }

    public void setAvgSteps(int avgSteps) {
        this.avgSteps = avgSteps;
    }

    public void setAvgSteps() {
        this.avgSteps = super.getSteps()/getNumDays();
    }

    private int getNumDays() {
        int numDays = 0;
        numDays = (int) ((this.dtEnd - this.dtStart)/ TimeUnit.DAYS.toMillis(1)) + 1;
        return (numDays > 0) ? numDays : 7;
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

    public double getStdDev() {
        return stdDev;
    }
    public void setStdDev(double stdDev) {
        this.stdDev = stdDev;
    }
    public void calculateStdDev(int[] data){
        double variance = 0;
        for(int i :data)
            variance += (i-this.avgSteps)*(i-this.avgSteps);
        variance =  variance/(getNumDays()-1);

        this.stdDev =  Math.sqrt(variance);
    }

    public double getMedian() {
        return median;
    }
    public void setMedian(double median) {
        this.median = median;
    }
    public void calculateMedian( int[] data) {
        Arrays.sort(data);

        if (data.length % 2 == 0) {
            this.median = (data[(getNumDays() / 2) - 1] + data[getNumDays() / 2]) / 2.0;
        }
        this.median =  data[getNumDays() / 2];
    }

    /*Comparator for sorting the list by date*/
    public static Comparator<WeekStepHistory> WeekDateComparator = new Comparator<WeekStepHistory>() {

        public int compare(WeekStepHistory m1, WeekStepHistory m2) {
            int week1 = (int) m1.getDtStart();
            int week2 = (int) m2.getDtStart();

            return week1 - week2;

        }};


    /*Comparator for sorting the list by standard deviation*/
    public static Comparator<WeekStepHistory> WeekStdDevComparator = new Comparator<WeekStepHistory>() {

        public int compare(WeekStepHistory m1, WeekStepHistory m2) {
            int week1 = (int) m1.getStdDev();
            int week2 = (int) m2.getStdDev();

            return week1 - week2;

        }};

    /*Comparator for sorting the list by median*/
    public static Comparator<WeekStepHistory> WeekMedianComparator = new Comparator<WeekStepHistory>() {

        public int compare(WeekStepHistory m1, WeekStepHistory m2) {
            int week1 = (int) m1.getMedian();
            int week2 = (int) m2.getMedian();

            return week1 - week2;

        }};

    /*Comparator for sorting the list by best day*/
    public static Comparator<WeekStepHistory> WeekBestDayComparator = new Comparator<WeekStepHistory>() {

        public int compare(WeekStepHistory m1, WeekStepHistory m2) {
            int week1 = (int) m1.getBestDay();
            int week2 = (int) m2.getBestDay();

            return week1 - week2;

        }};

}
