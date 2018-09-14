package de.j4velin.pedometer.obj;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.ArrayList;
import java.util.Calendar;
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

    public static String[] monthStrings = {"January",
            "February",
            "March",
            "April",
            "May",
            "June",
            "July",
            "August",
            "September",
            "October",
            "November",
            "December"};

    public static String[] shortMonthStrings = {"Jan",
            "Feb",
            "March",
            "April",
            "May",
            "June",
            "July",
            "Aug",
            "Sept",
            "Oct",
            "Nov",
            "Dec"};

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

    public String getMonth() {
        return shortMonthStrings[month];
    }

    public String getYearString() {
        return Integer.toString(year);
    }

    @Override
    public String toString(){
        return monthStrings[month] + " " + Integer.toString(year);
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

    public int getDaysInMonth() {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        int numDays = calendar.getActualMaximum(Calendar.DATE);

        return numDays;
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