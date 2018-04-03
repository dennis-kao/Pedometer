package de.j4velin.pedometer.obj;
import java.util.Comparator;

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



    /*Comparator for sorting the list by step*/
    public static Comparator<StepHistory> StepComparator = new Comparator<StepHistory>() {

        public int compare(StepHistory m1, StepHistory m2) {
            int history1 = m1.getSteps();
            int history2 = m2.getSteps();

            return history1 - history2;

        }};
}
