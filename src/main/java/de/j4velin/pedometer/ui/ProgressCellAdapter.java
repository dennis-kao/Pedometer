package de.j4velin.pedometer.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import de.j4velin.pedometer.R;

public class ProgressCellAdapter extends RecyclerView.Adapter<ProgressCellAdapter.ViewHolder> {

    private static final String TAG = "ProgressCellAdapter";
    private static final String[] categories = {"daily calories", "daily distance", "daily active minutes", "placeholder"};
    private static final int[] icons = {R.drawable.ic_footsteps};
    private int steps;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case

        private ImageView icon;
        private ProgressBar progressLine;
        private TextView numText, infoText;

        public ViewHolder(View v) {
            super(v);

            icon = v.findViewById(R.id.icon);
            progressLine = v.findViewById(R.id.progressLine);
            numText = v.findViewById(R.id.unitText);
            infoText = v.findViewById(R.id.infoText);
        }

        public void updateProgress(int numerator, int denominator) {

            progressLine.setProgress(numerator / denominator, true);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ProgressCellAdapter() {

    }

    // Create new views (invoked by the layout manager)
    @Override
    public ProgressCellAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {

        View v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.progress_cell, parent, false);
        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.infoText.setText(categories[position]);
        //holder.numText.setText();

        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        switch (position) {

            case 0:
                break;

            case 1:
                break;

            case 2:
                break;

            case 3:
                break;
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return categories.length;
    }
}
