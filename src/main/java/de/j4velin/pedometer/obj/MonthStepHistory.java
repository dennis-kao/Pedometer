package de.j4velin.pedometer.obj;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;

/**
 * Created by Rajbir on 20/03/18.
 */

public class MonthStepHistory extends StepHistory {
    private int month;
    private int year;
    private long avgSteps;
    private long bestDay;
    private double stdDev;
    private double median;

    public void setup(int month, int year, int totalSteps, long avgSteps, long bestDay, float distance, int[] stepsForTheMonth){
        this.month = month;
        this.year = year;
        this.setTotalSteps(totalSteps);
        this.avgSteps = avgSteps;
        this.bestDay = bestDay;
        this.setDistance(distance);
        this.calculateMedian(stepsForTheMonth);
        this.calculateStdDev(stepsForTheMonth);
    }

    public String getMonth(){
        switch(month) {
            case 1:
                return "January";
            case 2:
                return "February";
            case 3:
                return "March";
            case 4:
                return "April";
            case 5:
                return "May";
            case 6:
                return "June";
            case 7:
                return "July";
            case 8:
                return "August";
            case 9:
                return "September";
            case 10:
                return "October";
            case 11:
                return "November";
            case 12:
                return "December";
            default:
                return "null";
        }
    }
    public int getMonthInt(){
        return month;
    }
    public  void setMonth(int month){
        this.month = month;
    }

    public int getYear(){
        return year;
    }
    public  void setYear(int year){
        this.year = year;
    }

    public long getAvgSteps() {
        return avgSteps;
    }
    public void setAvgSteps(long avgSteps) { this.avgSteps = avgSteps; }

    public long getBestDay() { return bestDay; }
    public void setBestDay(long bestDay) { this.bestDay = bestDay; }
    public String getBestDayString(){
        if(this.bestDay <= 0){
            return "n/a";
        }
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(0);
        cal.setTimeInMillis(this.bestDay);
        return "   " + cal.get(Calendar.DAY_OF_MONTH) + "/" + (cal.get(Calendar.MONTH)+1) + "/" + cal.get(Calendar.YEAR);
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
        variance =  variance/(data.length-1);

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
            this.median = (data[(data.length / 2) - 1] + data[data.length / 2]) / 2.0;
        }
        this.median =  data[data.length / 2];
    }

    /*Comparator for sorting the list by date*/
    public static Comparator<MonthStepHistory> MonthDateComparator = new Comparator<MonthStepHistory>() {

        public int compare(MonthStepHistory m1, MonthStepHistory m2) {
            int month1 = m1.getMonthInt() + m1.getYear();
            int month2 = m2.getMonthInt() + m2.getYear();

            return month1 - month2;

        }};

    /*Comparator for sorting the list by standard deviation*/
    public static Comparator<MonthStepHistory> MonthStdDevComparator = new Comparator<MonthStepHistory>() {

        public int compare(MonthStepHistory m1, MonthStepHistory m2) {
            int month1 = (int) m1.getStdDev();
            int month2 = (int) m2.getStdDev();

            return month1 - month2;

        }};

    /*Comparator for sorting the list by median*/
    public static Comparator<MonthStepHistory> MonthMedianComparator = new Comparator<MonthStepHistory>() {

        public int compare(MonthStepHistory m1, MonthStepHistory m2) {
            int month1 = (int) m1.getMedian();
            int month2 = (int) m2.getMedian();

            return month1 - month2;

        }};

    /*Comparator for sorting the list by best day*/
    public static Comparator<MonthStepHistory> MonthBestDayComparator = new Comparator<MonthStepHistory>() {

        public int compare(MonthStepHistory m1, MonthStepHistory m2) {
            int month1 = (int) m1.getBestDay();
            int month2 = (int) m2.getBestDay();

            return month1 - month2;

        }};
}

