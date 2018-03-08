package de.j4velin.pedometer.obj;

/**
 * Created by calimr on 2018-03-06.
 */

public class StepHistoryWeek {
    private int totalSteps;
    private int avgSteps;
    private int distance;
    private int totalCalBurned;
    private long dtEnd;
    private long dtStart;

    public int getTotalSteps() {
        return totalSteps;
    }

    public void setTotalSteps(int totalSteps) {
        this.totalSteps = totalSteps;
    }

    public int getAvgSteps() {
        return avgSteps;
    }

    public void setAvgSteps(int avgSteps) {
        this.avgSteps = avgSteps;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
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

    public long getDtStart() {
        return dtStart;
    }

    public void setDtStart(long dtStart) {
        this.dtStart = dtStart;
    }
}
