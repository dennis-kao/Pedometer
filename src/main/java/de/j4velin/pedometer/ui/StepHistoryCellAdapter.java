package de.j4velin.pedometer.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import de.j4velin.pedometer.R;
import android.content.Context;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by averyspeller on 2018-03-15.
 */

public class StepHistoryCellAdapter  extends ArrayAdapter<Day_Step_History>{


    private int position;
    private View convertView;
    private ViewGroup parent;

    public StepHistoryCellAdapter(Context context, ArrayList<Day_Step_History> stepDays) {
            super(context, 0, stepDays);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            this.position = position;
            this.convertView = convertView;
            this.parent = parent;
            // Get the data item for this position
            Day_Step_History stepDays = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.step_history_cell, parent, false);
            }
            // Lookup view for data population

            TextView startDate = (TextView) convertView.findViewById(R.id.startDate);
            TextView endDate = (TextView) convertView.findViewById(R.id.endDate);
            TextView totalStep = (TextView) convertView.findViewById(R.id.totalStep);
            TextView avgStep = (TextView) convertView.findViewById(R.id.avgStep);
            TextView bestDay = (TextView) convertView.findViewById(R.id.bestDay);




            // Populate the data into the template view using the data object



            startDate.setText(stepDays.getStartDate());
            endDate.setText(stepDays.getEndDate());
            totalStep.setText(stepDays.getTotalStep());
            avgStep.setText(stepDays.getAvgStep());
            bestDay.setText(stepDays.getBestDay());




            // Return the completed view to render on screen
            return convertView;
        }







}
