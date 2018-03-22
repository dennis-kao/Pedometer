package de.j4velin.pedometer.obj;

/**
 * Created by Rajbir on 20/03/18.
 */

public class Month_Step_History {
    private int month;
    private int year;
    private int totalSteps = 0;
    private long avgSteps = 0;
    private int distance = 0;

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
    public  void setMonth(int month){
        this.month = month;
    }

    public int getYear(){
        return year;
    }
    public  void setYear(int year){
        this.year = year;
    }

    public int getTotalSteps() {
        return totalSteps;
    }
    public void setTotalSteps(int totalSteps) {
        this.totalSteps = totalSteps;
    }

    public long getAvgSteps() {
        return avgSteps;
    }
    public void setAvgSteps(long avgSteps) {
        this.avgSteps = avgSteps;
    }

    public int getDistance() {
        return distance;
    }
    public void setDistance(int distance) {
        this.distance = distance;
    }
}

