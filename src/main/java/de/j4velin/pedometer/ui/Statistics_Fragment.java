/*
 * Copyright 2014 Thomas Hoffmann
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.j4velin.pedometer.ui;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieEntry;

import org.eazegraph.lib.charts.BarChart;
import org.eazegraph.lib.models.BarModel;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.j4velin.pedometer.BuildConfig;
import de.j4velin.pedometer.Database;
import de.j4velin.pedometer.R;
import de.j4velin.pedometer.SensorListener;
import de.j4velin.pedometer.util.Logger;
import de.j4velin.pedometer.util.Util;

public class Statistics_Fragment extends Fragment implements SensorEventListener {

    private TextView stepsView, totalView, averageView;

    private int todayOffset, total_start, goal, since_boot, total_days;
    public final static NumberFormat formatter = NumberFormat.getInstance(Locale.getDefault());
    private boolean showSteps = true;

   PieChart chart;

    /**
     * Initial Creation of the overview fragment
     * @param savedInstanceState
     */
    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    /**
     * UI Creation of overview fragment, graphs, steps, litsener set-up
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_overview, null);
        stepsView = v.findViewById(R.id.steps);
        totalView = v.findViewById(R.id.total);
        averageView = v.findViewById(R.id.average);
        // chart = (PieChart) v.findViewById(R.id.progress_chart);

        //Settings_Fragment.DEFAULT_GOAL

        return v;
    }

    /**
     * Function which resumes counting of steps and sets the icon to resume
     */
    @Override
    public void onResume() {
        super.onResume();
//        getActivity().getActionBar().setDisplayHomeAsUpEnabled(false);

        Database db = Database.getInstance(getActivity());

        if (BuildConfig.DEBUG) db.logState();
        // read todays offset
        todayOffset = db.getSteps(Util.getToday());

        SharedPreferences prefs =
                getActivity().getSharedPreferences("de.dkao.de.dkao.pedometer", Context.MODE_PRIVATE);

        goal = prefs.getInt("goal", Settings_Fragment.DEFAULT_GOAL);
        since_boot = db.getCurrentSteps(); // do not use the value from the sharedPreferences
        int pauseDifference = since_boot - prefs.getInt("pauseCount", since_boot);

        // register a sensorlistener to live update the UI if a step is taken
//        if (!prefs.contains("pauseCount")) {
//            SensorManager sm =
//                    (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
//            Sensor sensor = sm.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
//            if (sensor == null) {
//                new AlertDialog.Builder(getActivity()).setTitle(R.string.no_sensor)
//                        .setMessage(R.string.no_sensor_explain)
//                        .setOnDismissListener(new DialogInterface.OnDismissListener() {
//                            @Override
//                            public void onDismiss(final DialogInterface dialogInterface) {
//                                getActivity().finish();
//                            }
//                        }).setNeutralButton(android.R.string.ok,
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(final DialogInterface dialogInterface, int i) {
//                                dialogInterface.dismiss();
//                            }
//                        }).create().show();
//            } else {
//                sm.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI, 0);
//            }
//        }

        since_boot -= pauseDifference;

        total_start = db.getTotalWithoutToday();
        total_days = db.getDays();

        db.close();

        stepsDistanceChanged();
    }

    /**
     * Call this method if the Fragment should update the "steps"/"km" text in
     * the pie graph as well as the pie and the bars graphs.
     */
    private void stepsDistanceChanged() {

        //  update the steps label
        if (showSteps) {
            ((TextView) getView().findViewById(R.id.unit)).setText(getString(R.string.steps));
        } else {
            String unit = getActivity().getSharedPreferences("de.dkao.de.dkao.pedometer", Context.MODE_PRIVATE)
                    .getString("stepsize_unit", Settings_Fragment.DEFAULT_STEP_UNIT);
            if (unit.equals("cm")) {
                unit = "km";
            } else {
                unit = "mi";
            }
            ((TextView) getView().findViewById(R.id.unit)).setText(unit);
        }

        //  now update the graphs
        updatePie();
        updateBars();
    }

    /**
     * Function which handles pausing the counting of steps and stops the phone from counting steps
     */
    @Override
    public void onPause() {
        super.onPause();
        try {
            SensorManager sm =
                    (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
            sm.unregisterListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Database db = Database.getInstance(getActivity());
        db.saveCurrentSteps(since_boot);
        db.close();
    }

    /**
     * Creates the expanded view of the option menu and sets the pause/resume icon
     * to the approriate icon
     * @param menu
     * @param inflater
     */
    @Override
    public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        MenuItem pause = menu.getItem(0);
        Drawable d;
        if (getActivity().getSharedPreferences("de.dkao.de.dkao.pedometer", Context.MODE_PRIVATE)
                .contains("pauseCount")) { // currently paused
            pause.setTitle(R.string.resume);
            d = getResources().getDrawable(R.drawable.ic_resume);
        } else {
            pause.setTitle(R.string.pause);
            d = getResources().getDrawable(R.drawable.ic_pause);
        }
        d.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        pause.setIcon(d);
    }

    /**
     * Function which handles an option item selection and displays the appropriate
     * dialog/drawable when an item gets selected
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_pause:
                SensorManager sm =
                        (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
                Drawable d;
                if (getActivity().getSharedPreferences("de.dkao.de.dkao.pedometer", Context.MODE_PRIVATE)
                        .contains("pauseCount")) { // currently paused -> now resumed
                    sm.registerListener(this, sm.getDefaultSensor(Sensor.TYPE_STEP_COUNTER),
                            SensorManager.SENSOR_DELAY_UI, 0);
                    item.setTitle(R.string.pause);
                    d = getResources().getDrawable(R.drawable.ic_pause);
                } else {
                    sm.unregisterListener(this);
                    item.setTitle(R.string.resume);
                    d = getResources().getDrawable(R.drawable.ic_resume);
                }
                d.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                item.setIcon(d);
                getActivity().startService(new Intent(getActivity(), SensorListener.class)
                        .putExtra("action", SensorListener.ACTION_PAUSE));
                return true;
            default:
                return ((Activity_Main) getActivity()).optionsItemSelected(item);
        }
    }

    /**
     * (Part of implementing sensor listener)
     * @param sensor
     * @param accuracy
     */
    @Override
    public void onAccuracyChanged(final Sensor sensor, int accuracy) {
        // won't happen
    }

    /**
     * Function which handles an event when the sensor has changed
     * (part of implementing sensor listener)
     * @param event
     */
    @Override
    public void onSensorChanged(final SensorEvent event) {
        if (BuildConfig.DEBUG)
            Logger.log("UI - sensorChanged | todayOffset: " + todayOffset + " since boot: " +
                    event.values[0]);
        if (event.values[0] > Integer.MAX_VALUE || event.values[0] == 0) {
            return;
        }
        if (todayOffset == Integer.MIN_VALUE) {
            // no values for today
            // we dont know when the reboot was, so set todays steps to 0 by
            // initializing them with -STEPS_SINCE_BOOT
            todayOffset = -(int) event.values[0];
            Database db = Database.getInstance(getActivity());
            db.insertNewDay(Util.getToday(), (int) event.values[0]);
            db.close();
        }
        since_boot = (int) event.values[0];
        updatePie();
    }

    /**
     * Updates the pie graph to show todays steps/distance as well as the
     * yesterday and total values. Should be called when switching from step
     * count to distance.
     */
    private void updatePie() {
        if (BuildConfig.DEBUG) Logger.log("UI - update steps: " + since_boot);
        // todayOffset might still be Integer.MIN_VALUE on first start

        List<PieEntry> entries = new ArrayList<>();

//        entries.add(new PieEntry(18.5f, "Green"));
//        entries.add(new PieEntry(26.7f, "Yellow"));
//        entries.add(new PieEntry(24.0f, "Red"));
//        entries.add(new PieEntry(30.8f, "Blue"));

        int steps_today = Math.max(todayOffset + since_boot, 0);

        if (steps_today < goal) {
                // can happen if the goal value was changed: old goal value was
                // reached but now there are some steps missing for the new goal
                float completed = (float) steps_today / (float) goal * 100;
                float remaining = 1 - completed;
                entries.add(new PieEntry(completed, "Completed"));
                entries.add(new PieEntry(remaining, "Remaining"));
        } else {
            // goal reached
            entries.add(new PieEntry(100, "Completed"));
        }

        if (showSteps) {
            stepsView.setText(formatter.format(steps_today));
            totalView.setText(formatter.format(total_start + steps_today));
            averageView.setText(formatter.format((total_start + steps_today) / total_days));
        } else {
            // update only every 10 steps when displaying distance
            SharedPreferences prefs =
                    getActivity().getSharedPreferences("de.dkao.de.dkao.pedometer", Context.MODE_PRIVATE);
            float stepsize = prefs.getFloat("stepsize_value", Settings_Fragment.DEFAULT_STEP_SIZE);
            float distance_today = steps_today * stepsize;
            float distance_total = (total_start + steps_today) * stepsize;
            if (prefs.getString("stepsize_unit", Settings_Fragment.DEFAULT_STEP_UNIT)
                    .equals("cm")) {
                distance_today /= 100000;
                distance_total /= 100000;
            } else {
                distance_today /= 5280;
                distance_total /= 5280;
            }
            stepsView.setText(formatter.format(distance_today));
            totalView.setText(formatter.format(distance_total));
            averageView.setText(formatter.format(distance_total / total_days));
        }
    }

    /**
     * Updates the bar graph to show the steps/distance of the last week. Should
     * be called when switching from step count to distance.
     */
    private void updateBars() {

        SimpleDateFormat df = new SimpleDateFormat("E", Locale.getDefault());
        BarChart barChart = getView().findViewById(R.id.bargraph);
        if (barChart.getData().size() > 0) barChart.clearChart();
        int steps;
        float distance, stepsize = Settings_Fragment.DEFAULT_STEP_SIZE;
        boolean stepsize_cm = true;
        if (!showSteps) {
            // load some more settings if distance is needed
            SharedPreferences prefs =
                    getActivity().getSharedPreferences("de.dkao.de.dkao.pedometer", Context.MODE_PRIVATE);
            stepsize = prefs.getFloat("stepsize_value", Settings_Fragment.DEFAULT_STEP_SIZE);
            stepsize_cm = prefs.getString("stepsize_unit", Settings_Fragment.DEFAULT_STEP_UNIT)
                    .equals("cm");
        }
        barChart.setShowDecimal(!showSteps); // show decimal in distance view only
        BarModel bm;
        Database db = Database.getInstance(getActivity());
        List<Pair<Long, Integer>> last = db.getLastEntries(8);
        db.close();
        for (int i = last.size() - 1; i > 0; i--) {
            Pair<Long, Integer> current = last.get(i);
            steps = current.second;
            if (steps > 0) {
                bm = new BarModel(df.format(new Date(current.first)), 0,
                        steps > goal ? Color.parseColor("#99CC00") : Color.parseColor("#0099cc"));
                if (showSteps) {
                    bm.setValue(steps);
                } else {
                    distance = steps * stepsize;
                    if (stepsize_cm) {
                        distance /= 100000;
                    } else {
                        distance /= 5280;
                    }
                    distance = Math.round(distance * 1000) / 1000f; // 3 decimals
                    bm.setValue(distance);
                }
                barChart.addBar(bm);
            }
        }
        if (barChart.getData().size() > 0) {
            barChart.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(final View v) {
                    Dialog_Statistics.getDialog(getActivity(), since_boot).show();
                }
            });
            barChart.startAnimation();
        } else {
            barChart.setVisibility(View.GONE);
        }
    }

}
