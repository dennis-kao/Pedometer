package de.j4velin.pedometer.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import de.j4velin.pedometer.R;
import android.content.Context;
import android.widget.TextView;

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

            // Populate the data into the template view using the data object
            if (stepHistory instanceof DayStepHistory) {

                DayStepHistory stepDays = (DayStepHistory) stepHistory;

                startDate.setText(Long.toString(stepDays.getDay()));
                endDate.setText(stepDays.getDayString());
                totalStep.setText("   Total Steps: " + Integer.toString(stepDays.getSteps()));
                avgStep.setText("   Goal Achieved: " + stepDays.getGoal());
                bestDay.setText("   Distance: 2 km");
            }
            else if (stepHistory instanceof WeekStepHistory)
            {
                WeekStepHistory stepWeeks = (WeekStepHistory) stepHistory;

                Calendar cal = Calendar.getInstance();

                cal.setTimeInMillis(stepWeeks.getDtStart());
                cal.setTimeInMillis(stepWeeks.getDtEnd());

                startDate.setText( stepWeeks.getDtStartAsDateString());
                endDate.setText(stepWeeks.getDtEndAsDateString());
                totalStep.setText("   Total Steps: "+Integer.toString(stepWeeks.getSteps()));
                avgStep.setText("   Avg Steps: " + Integer.toString(stepWeeks.getAvgSteps()));
                bestDay.setText("   Best Day: everyday is your best day!!");

            }
            else if (stepHistory instanceof MonthStepHistory)
            {

                MonthStepHistory stepMonth = (MonthStepHistory) stepHistory;

                startDate.setText(stepMonth.getMonth());
                endDate.setText(Integer.toString(stepMonth.getYear()));
                avgStep.setText("   Average Steps: " + Long.toString(stepMonth.getAvgSteps()));
                totalStep.setText("   Total Steps: " + Integer.toString(stepMonth.getSteps()));
                bestDay.setText("   Distance: 34km");
            }

            // Return the completed view to render on screen
            return convertView;
        }

//    public void updateList(ArrayList<StepHistory> stepHistory) {
//        this.clear();
//        this.addAll(stepHistory);
//    }
}
