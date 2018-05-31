package de.j4velin.pedometer.ui;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.xiaochen.progressroundbutton.AnimButtonLayout;
import com.xiaochen.progressroundbutton.AnimDownloadProgressButton;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import de.j4velin.pedometer.BuildConfig;
import de.j4velin.pedometer.Database;
import de.j4velin.pedometer.util.Logger;
import de.j4velin.pedometer.util.Util;
import de.j4velin.pedometer.R;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.daimajia.numberprogressbar.OnProgressBarListener;


public class Statistics_Activity extends Fragment implements SensorEventListener {

    private int todayOffset, total_start, goal, since_boot, total_days;
    public final static NumberFormat formatter = NumberFormat.getInstance(Locale.getDefault());
    private TextView date, calories, distance;

    final int[] dailyColors = {Color.rgb(25,114,120), Color.rgb(239,45,86)};
    ArrayList<Integer> colors = new ArrayList<Integer>();
    NumberProgressBar dailyProgresBar;

    public void createTestData() {
        Database db = Database.getInstance(getActivity());
        Random ran = new Random();

        int ranSteps = ran.nextInt(1000) + 100;

        for (int i = 0; i < 60; i++)
        {
            Log.d("SETTINGS", "CREATING TEST DATA");
            db.insertNewDay(Util.getToday() - (i * TimeUnit.DAYS.toMillis(1)), -ranSteps);
            ranSteps = ran.nextInt(1000) + 100;
        }
        db.close();
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        createTestData();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.statistics1, null);

        calories = v.findViewById(R.id.calories);
        date = v.findViewById(R.id.date);
        distance = v.findViewById(R.id.distance);

        return v;
    }

    /**
     * Function which resumes counting of steps and sets the icon to resume
     */
    @Override
    public void onResume() {
        super.onResume();

        //  update the day text if needed
        String dateText = new SimpleDateFormat("EEEE MMMM dd").format(new Date());
        Database db = Database.getInstance(getActivity());
        SharedPreferences prefs =
                getActivity().getSharedPreferences("pedometer", Context.MODE_PRIVATE);

        date.setText(dateText);

        if (BuildConfig.DEBUG) db.logState();
        // read todays offset
        todayOffset = db.getSteps(Util.getToday());

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

        updateDailyProgress();
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
     * (Part of implementing sensor listener)
     * @param sensor
     * @param accuracy
     */
    @Override
    public void onAccuracyChanged(final Sensor sensor, int accuracy) {
        // won't happen
    }

    private float getCurrentProgress() {
        int steps_today = Math.max(todayOffset + since_boot, 0);
        return (float) steps_today / (float) goal;
    }

    public void updateDailyProgress() {

    }

    public void updateWeeklyProgress(BarChart week) {

        BarDataSet set;
        ArrayList<BarEntry> yVals = new ArrayList<BarEntry>();

        Database db = Database.getInstance(getActivity());
        List<Pair<Long, Integer>> last = db.getLastEntries(7);
        db.close();

        for (int i = 0; i < last.size(); i++) {
            Pair<Long, Integer> day = last.get(i);
            yVals.add(new BarEntry(i, day.second));
        }

        if (week.getData() != null && week.getData().getDataSetCount() > 0) {

            set = (BarDataSet) week.getData().getDataSetByIndex(0);
            set.setValues(yVals);

            week.getData().notifyDataChanged();
            week.notifyDataSetChanged();
        }
        else {
            set = new BarDataSet(yVals, "Steps");
            set.setColors(ColorTemplate.MATERIAL_COLORS);

            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set);

            BarData data = new BarData(dataSets);

            week.setData(data);
        }
    }

//    private SpannableString generateCenterSpannableText(int goal, int steps_taken) {
//
//        String goalText = NumberFormat.getNumberInstance(Locale.US).format(goal);
//        String stepsTakenText = NumberFormat.getNumberInstance(Locale.US).format(steps_taken);
//        String stepsLabel = String.format("%s\n\n/%s\nSTEPS", stepsTakenText, goalText);
//        SpannableString s = new SpannableString(stepsLabel);
//
//        //  formats the completed steps text
//        s.setSpan(new RelativeSizeSpan(3.0f), 0, stepsTakenText.length(), 0);
//        s.setSpan(new StyleSpan(Typeface.BOLD), 0, stepsTakenText.length(), 0);
//
//        // formats the "goal" text
//        s.setSpan(new RelativeSizeSpan(1.0f), stepsTakenText.length() + 2, goalText.length() + stepsTakenText.length() + 3,  0);
//        s.setSpan(new StyleSpan(Typeface.BOLD), stepsTakenText.length() + 2, goalText.length() + stepsTakenText.length() + 3, 0);
//
//        // formats the STEPS text
//        s.setSpan(new RelativeSizeSpan(0.8f), s.length() - 5, s.length(),  0);
//        s.setSpan(new StyleSpan(Typeface.NORMAL), s.length() - 5, s.length(), 0);
//        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - 5, s.length(), 0);
//
//        return s;
//    }


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
        updateDailyProgress();
    }
}
