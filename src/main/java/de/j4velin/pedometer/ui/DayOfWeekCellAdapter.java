package de.j4velin.pedometer.ui;

import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

import de.j4velin.pedometer.R;
import de.j4velin.pedometer.util.Logger;

public class DayOfWeekCellAdapter extends RecyclerView.Adapter<DayOfWeekCellAdapter.ViewHolder> {

    private static final String TAG = "DayOfWeekCellAdapter";
    private List<Pair<Long, Float>> weekProgress;
    final String[] strDays = new String[] { "Sunday", "Monday", "Tuesday", "Wednesday", "Thusday",
            "Friday", "Saturday" };

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case

        private TextView dowChar;
        private CircularProgressBar dowCircle;

        public ViewHolder(View v) {
            super(v);

            dowChar = v.findViewById(R.id.dowChar);

            dowCircle = v.findViewById(R.id.dowCircle);
            dowCircle.showProgressText(false);
            dowCircle.useRoundedCorners(false);
            dowCircle.setProgressWidth(5);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public DayOfWeekCellAdapter(List<Pair<Long, Float>> wp) {
        weekProgress = wp;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public DayOfWeekCellAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {

        View v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.dow_circle_cell, parent, false);
        DayOfWeekCellAdapter.ViewHolder vh = new DayOfWeekCellAdapter.ViewHolder(v);

        return vh;
    }

    public String calcEpochDayOfWeek(Long epochTime) {

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(epochTime);
        int dow = cal.get(Calendar.DAY_OF_WEEK) - 1;

        Logger.log("Day of week: " + strDays[dow]);

        return strDays[dow];
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(DayOfWeekCellAdapter.ViewHolder holder, int position) {

        Pair<Long, Float> dayRecord = weekProgress.get(position);

        //calc time, generate string of day, grab first character
        String getDayOfWeek = calcEpochDayOfWeek(dayRecord.first);

        //Logger.log("PROGRESS: " + Integer.toString(dayRecord.second));

        holder.dowChar.setText(Character.toString(getDayOfWeek.charAt(0)));
        holder.dowCircle.setProgress(dayRecord.second);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return weekProgress.size();
    }
}
