package de.j4velin.pedometer.obj;

/**
 * Created by dkao on 3/22/2018.
 */

public abstract class StepHistory {
    private int distance;
    private int calories;
    private int totalSteps;

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public void setTotalSteps(int totalSteps) {
        this.totalSteps = totalSteps;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getSteps(){return totalSteps;}
    public int getDistance(){return distance;}
    public int getCalories(){return calories;}
}
