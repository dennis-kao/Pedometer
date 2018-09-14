package de.j4velin.pedometer.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import de.j4velin.pedometer.R;
import android.content.Context;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;

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

public class HistoryCellAdapter<T extends StepHistory> extends ArrayAdapter<T>{

    private int position, goal;
    private View convertView;
    private ViewGroup parent;

    public HistoryCellAdapter(Context context, ArrayList<T> stepHistory, int goal) {
            super(context, 0, stepHistory);
            this.goal = goal;
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
            TextView dateText = convertView.findViewById(R.id.dateText);
            TextView numText = convertView.findViewById(R.id.numText);
            RoundCornerProgressBar progressBar = convertView.findViewById(R.id.stepProgress);

            // Populate the data into the template view using the data object
            if (stepHistory instanceof DayStepHistory) {

                DayStepHistory stepDays = (DayStepHistory) stepHistory;

                dateText.setText(stepDays.toString());
                numText.setText(Integer.toString(stepDays.getSteps()));
                progressBar.setProgress(stepDays.getGoalCompleted());
            }
            else if (stepHistory instanceof WeekStepHistory)
            {
                WeekStepHistory stepWeeks = (WeekStepHistory) stepHistory;

                Calendar cal = Calendar.getInstance();

                cal.setTimeInMillis(stepWeeks.getDtStart());
                cal.setTimeInMillis(stepWeeks.getDtEnd());

                dateText.setText(stepWeeks.toString());
                numText.setText(Integer.toString(stepWeeks.getSteps()));
                progressBar.setProgress(stepWeeks.getSteps() / (float) (stepWeeks.getNumDays() * goal) * 100);
            }
            else if (stepHistory instanceof MonthStepHistory)
            {

                MonthStepHistory stepMonth = (MonthStepHistory) stepHistory;

                dateText.setText(stepMonth.toString());
                numText.setText(Integer.toString(stepMonth.getSteps()));
                progressBar.setProgress( (float) stepMonth.getSteps() / (float) (stepMonth.getDaysInMonth() * goal) * 100);
            }

            // Return the completed view to render on screen
            return convertView;
        }
}
