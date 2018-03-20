package de.j4velin.pedometer.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.Calendar;

import de.j4velin.pedometer.obj.Week_Step_History;
import de.j4velin.pedometer.R;

/**
 * Created by calimr on 2018-03-19.
 */

public class WeekStepHistoryCellAdapter extends ArrayAdapter<Week_Step_History> {

    private int position;
    private View convertView;
    private ViewGroup parent;

    public WeekStepHistoryCellAdapter(@NonNull Context context, @NonNull ArrayList<Week_Step_History> stepWeeks) {
        super(context, 0, stepWeeks);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        this.position = position;
        this.convertView = convertView;
        this.parent = parent;

        Week_Step_History stepWeeks = getItem(position);
        Calendar cal = Calendar.getInstance();

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.step_history_cell, parent, false);
        }

        TextView startDate = (TextView) convertView.findViewById(R.id.startDate);
        TextView endDate = (TextView) convertView.findViewById(R.id.endDate);
        TextView totalStep = (TextView) convertView.findViewById(R.id.totalStep);
        TextView avgStep = (TextView) convertView.findViewById(R.id.avgStep);
        TextView bestDay = (TextView) convertView.findViewById(R.id.bestDay);

        cal.setTimeInMillis(stepWeeks.getDtStart());
        startDate.setText("Start Date:" + Long.toString(stepWeeks.getDtStart()));

        cal.setTimeInMillis(stepWeeks.getDtEnd());
        endDate.setText(Long.toString(stepWeeks.getDtEnd()));

        totalStep.setText(Integer.toString(stepWeeks.getTotalSteps()));

        avgStep.setText(Integer.toString(stepWeeks.getAvgSteps()));

//        cal.setTimeInMillis(stepWeeks.getBestDay());
//        bestDay.setText(cal.get(Calendar.DATE));

        return convertView;
    }

    public void updateList(ArrayList<Week_Step_History> stepWeeks) {
        this.clear();
        this.addAll(stepWeeks);
    }
}
