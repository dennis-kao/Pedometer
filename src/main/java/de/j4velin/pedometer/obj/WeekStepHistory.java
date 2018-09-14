package de.j4velin.pedometer.obj;

import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.TimeUnit;

/**
 * Created by calimr on 2018-03-06.
 */

public class WeekStepHistory extends StepHistory{

    private int avgSteps = 0;
    private int bestDaySteps = 0;
    private long dtEnd = 0;
    private long dtStart = 0;
    private long bestDay = 0;
    private double stdDev;
    private double median;
    public static String wFormat = "MMMM d";

    @Override
    public String toString() {
        return getDateText(dtStart, wFormat) + " - " + getDateText(dtEnd, wFormat);
    }

    public String getMonthText() {

        String startMonth = getDateText(dtStart, "MMMM");
        String endMonth = getDateText(dtEnd, "MMMM");

        if (startMonth.equals(endMonth)) return startMonth;
        else return startMonth + " & " + endMonth;
    }

    public String getNumText() {

        String firstNum = getDateText(dtStart, "d");
        String lastNum = getDateText(dtEnd, "d");

        if (firstNum.equals(lastNum)) return firstNum;
        else return firstNum + " - " + lastNum;
    }

    public String getYear() {
        String firstNum = getDateText(dtStart, "YYYY");
        return firstNum;
    }

    public int getBestDaySteps() {
        return bestDaySteps;
    }

    public void setBestDaySteps(int bestDaySteps) {
        this.bestDaySteps = bestDaySteps;
    }

    public long getBestDay() {
        return bestDay;
    }

    public void setBestDay(long bestDay) {
        this.bestDay = bestDay;
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

    public int getNumDays() {
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


    public long getDtStart() {
        return dtStart;
    }

    public void setDtStart(long dtStart) {
        this.dtStart = dtStart;
    }

    public double getDev() {
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
            long week1 =  m1.getDtStart();
            long week2 =  m2.getDtStart();

            return (int)(week1 - week2);

        }};


    /*Comparator for sorting the list by standard deviation*/
    public static Comparator<WeekStepHistory> WeekStdDevComparator = new Comparator<WeekStepHistory>() {

        public int compare(WeekStepHistory m1, WeekStepHistory m2) {
            int week1 = (int) m1.getDev();
            int week2 = (int) m2.getDev();

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
            int week1 = (int) m1.getBestDaySteps();
            int week2 = (int) m2.getBestDaySteps();

            return week1 - week2;

        }};

}
