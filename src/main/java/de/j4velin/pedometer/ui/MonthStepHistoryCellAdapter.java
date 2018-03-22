package de.j4velin.pedometer.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater; //
import android.view.View; //
import android.view.ViewGroup; //
import android.widget.TextView;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import de.j4velin.pedometer.R;
import de.j4velin.pedometer.obj.Month_Step_History;

import java.util.ArrayList;

/**
 * Created by kiranbir on 2018-03-21.
 */

public class MonthStepHistoryCellAdapter extends ArrayAdapter<Month_Step_History> {

    private int position;
    private View convertView;
    private ViewGroup parent;

    public MonthStepHistoryCellAdapter(Context context, ArrayList<Month_Step_History> stepMonths) {
        super(context, 0, stepMonths);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        this.position = position;
        this.convertView = convertView;
        this.parent = parent;
        // Get the data item for this position
        Month_Step_History stepDays = getItem(position);

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


        startDate.setText(stepDays.getMonth());
        endDate.setText(Integer.toString(stepDays.getYear()));
        avgStep.setText("   Average Steps: " + Long.toString(stepDays.getAvgSteps()));
        totalStep.setText("   Total Steps: " + Integer.toString(stepDays.getTotalSteps()));
        bestDay.setText("   Distance: 34km");


        // Return the completed view to render on screen
        return convertView;
    }

}
