package de.j4velin.pedometer.obj;

/**
 * Created by calimr on 2018-03-06.
 */

public class StepHistoryWeek {
    private int totalSteps = 0;
    private int avgSteps = 0;
    private int distance = 0;
    private int totalCalBurned = 0;
    private long dtEnd = 0;
    private long dtStart = 0;

    public int getTotalSteps() {
        return totalSteps;
    }

    public void setTotalSteps(int totalSteps) {
        this.totalSteps = totalSteps;
        this.setAvgSteps();
        this.setDistance();
    }

    public int getAvgSteps() {
        return (avgSteps > 0) ? avgSteps : totalSteps/7;
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

    public long getDtStart() {
        return dtStart;
    }

    public void setDtStart(long dtStart) {
        this.dtStart = dtStart;
    }
}
