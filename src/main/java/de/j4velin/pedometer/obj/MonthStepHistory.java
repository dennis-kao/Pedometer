package de.j4velin.pedometer.obj;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by Rajbir on 20/03/18.
 */

public class MonthStepHistory extends StepHistory {
    private int month;
    private int year;
    private int bestDaySteps;
    private long avgSteps;
    private long bestDay;
    private double stdDev;
    private double median;

    public void setup(int month, int year, int totalSteps, long avgSteps, long bestDay, float distance, ArrayList<Integer> stepsForTheMonth){
        this.month = month;
        this.year = year;
        this.setTotalSteps(totalSteps);
        this.avgSteps = avgSteps;
        this.bestDay = bestDay;
        this.setDistance(distance);
        this.calculateMedian(stepsForTheMonth);
        this.calculateStdDev(stepsForTheMonth);
    }

    public int getBestDaySteps() {
        return bestDaySteps;
    }

    public void setBestDaySteps(int bestDaySteps) {
        this.bestDaySteps = bestDaySteps;
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
        String rtn;
        Date date = new Date(this.bestDay);
        DateFormat formatter = new SimpleDateFormat("dd/MM/YYYY");
        rtn = formatter.format(date).toString();
        return rtn;
    }


    public double getStdDev() {
        return stdDev;
    }
    public void setStdDev(double stdDev) {
        this.stdDev = stdDev;
    }
    public void calculateStdDev(ArrayList<Integer> data){
        double variance = 0;
        for(int i :data)
            variance += (i-this.avgSteps)*(i-this.avgSteps);
        variance =  variance/(data.size()-1);

        this.stdDev =  Math.sqrt(variance);
    }

    public double getMedian() {
        return median;
    }
    public void setMedian(double median) {
        this.median = median;
    }
    public void calculateMedian( ArrayList<Integer> data) {
        Collections.sort(data);

        if (data.size() % 2 == 0) {
            this.median = (data.get((data.size() / 2) - 1) + data.get(data.size() / 2)) / 2.0;
        }
        this.median =  data.get(data.size() / 2);
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
            int month1 = (int) m1.getBestDaySteps();
            int month2 = (int) m2.getBestDaySteps();

            return month1 - month2;

        }};
}