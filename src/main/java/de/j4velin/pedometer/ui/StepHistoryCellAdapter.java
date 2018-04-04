package de.j4velin.pedometer.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import de.j4velin.pedometer.R;
import android.content.Context;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;

import de.j4velin.pedometer.obj.DayStepHistory;
import de.j4velin.pedometer.obj.MonthStepHistory;
import de.j4velin.pedometer.obj.StepHistory;
import de.j4velin.pedometer.obj.WeekStepHistory;

/**
 * Created by averyspeller on 2018-03-15.
 */

public class StepHistoryCellAdapter <T extends StepHistory> extends ArrayAdapter<T>{

    private int position;
    private View convertView;
    private ViewGroup parent;

    public StepHistoryCellAdapter(Context context, ArrayList<T> stepHistory) {
            super(context, 0, stepHistory);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            NumberFormat formatter = new DecimalFormat("#.##");
            this.position = position;
            this.convertView = convertView;
            this.parent = parent;

            // Get the data item for this position
            StepHistory stepHistory = getItem(position);

            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.step_history_cell, parent, false);
            }

            // Lookup view for data population
            TextView startDate = convertView.findViewById(R.id.startDate);
            TextView endDate = convertView.findViewById(R.id.endDate);
            TextView totalStep = convertView.findViewById(R.id.totalStep);
            TextView avgStep = convertView.findViewById(R.id.avgStep);
            TextView bestDay = convertView.findViewById(R.id.bestDay);
            TextView distance = convertView.findViewById(R.id.distance);
            TextView calories = convertView.findViewById(R.id.calories);

            // Populate the data into the template view using the data object
            if (stepHistory instanceof DayStepHistory) {

                DayStepHistory stepDays = (DayStepHistory) stepHistory;

                startDate.setText(stepDays.getDayString());
                endDate.setVisibility(TextView.INVISIBLE);
                totalStep.setText("   Total Steps: " + Integer.toString(stepDays.getSteps()));
                avgStep.setText("   Distance: " + Float.toString(stepDays.getDistance()) + " km");
                bestDay.setText("   Calories: " + Integer.toString(stepDays.getCalories()));
                distance.setText("   Goal Achieved: " + stepDays.getGoal() + "%");
                calories.setVisibility(TextView.INVISIBLE);
            }
            else if (stepHistory instanceof WeekStepHistory)
            {
                WeekStepHistory stepWeeks = (WeekStepHistory) stepHistory;

                Calendar cal = Calendar.getInstance();

                cal.setTimeInMillis(stepWeeks.getDtStart());
                cal.setTimeInMillis(stepWeeks.getDtEnd());

                startDate.setText("   Start: " + stepWeeks.getDtStartAsDateString());
                endDate.setText("   End: " + stepWeeks.getDtEndAsDateString());
                totalStep.setText("   Total Steps: "+Integer.toString(stepWeeks.getSteps()));
                avgStep.setText(String.format("   Average Steps: %d\t Std. Dev: %.3f\t Median:%.3f", stepWeeks.getAvgSteps(), stepWeeks.getStdDev(), stepWeeks.getMedian()));
                bestDay.setText("   Best Day: " + stepWeeks.getBestDayAsDateString() + " (" + Integer.toString(stepWeeks.getBestDaySteps()) + ")");
                distance.setText("   Distance: " + Float.toString(stepWeeks.getDistance()) + " km");
                calories.setText("   Calories: " + Integer.toString(stepWeeks.getCalories()));
            }
            else if (stepHistory instanceof MonthStepHistory)
            {

                MonthStepHistory stepMonth = (MonthStepHistory) stepHistory;

                startDate.setText(stepMonth.getMonth());
                endDate.setText(Integer.toString(stepMonth.getYear()));
                avgStep.setText(String.format("   Average Steps: %d\t Std. Dev: %.3f\t Median: %.3f", stepMonth.getAvgSteps(), stepMonth.getStdDev(), stepMonth.getMedian()));
                totalStep.setText("   Total Steps: " + Integer.toString(stepMonth.getSteps()));
                bestDay.setText("   Best Day: " + stepMonth.getBestDayString() + " (" + Integer.toString(stepMonth.getBestDaySteps()) + ")");
                distance.setText("   Distance: " + Float.toString(stepMonth.getDistance()) + " km");
                calories.setText("   Calories: " + Integer.toString(stepMonth.getCalories()));
            }

            // Return the completed view to render on screen
            return convertView;
        }

//    public void updateList(ArrayList<StepHistory> stepHistory) {
//        this.clear();
//        this.addAll(stepHistory);
//    }
}
