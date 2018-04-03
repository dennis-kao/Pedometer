package de.j4velin.pedometer.obj;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Comparator;

/**
 * Created by dkao on 3/22/2018.
 */

public abstract class StepHistory {
    private float distance;
    private int calories;
    private int totalSteps;

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public void setTotalSteps(int totalSteps) {
        this.totalSteps = totalSteps;
    }

    public void setDistance(float distance) {
        // formatting to only capture up to the 3rd decimal place
        String distString;
        NumberFormat formatter = new DecimalFormat("#.###");
        distString = formatter.format(distance);

        this.distance = Float.parseFloat(distString);
    }

    public int getSteps(){return totalSteps;}
    public float getDistance(){return distance;}
    public int getCalories(){return calories;}



    /*Comparator for sorting the list by step*/
    public static Comparator<StepHistory> StepComparator = new Comparator<StepHistory>() {

        public int compare(StepHistory m1, StepHistory m2) {
            int history1 = m1.getSteps();
            int history2 = m2.getSteps();

            return history1 - history2;

        }};
}
