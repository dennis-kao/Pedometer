package de.j4velin.pedometer.ui;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import de.j4velin.pedometer.BuildConfig;
import de.j4velin.pedometer.Database;
import de.j4velin.pedometer.util.Logger;
import de.j4velin.pedometer.util.Util;
import de.j4velin.pedometer.R;

public class Statistics_Activity extends Fragment implements SensorEventListener {

    private int todayOffset, total_start, goal, since_boot, total_days;
    public final static NumberFormat formatter = NumberFormat.getInstance(Locale.getDefault());
    private PieChart dailyGoalChart, weeklyGoalChart;
//    private final Typeface robotoLight = getResources().getFont(R.font.roboto_light),
//            robotoBold = getResources().getFont(R.font.roboto_bold),
//            robotoThin = getResources().getFont(R.font.roboto_thin),
//            robotoMedium = getResources().getFont(R.font.roboto_medium);

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

    /**
     * Initial Creation of the overview fragment
     * @param savedInstanceState
     */
    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        createTestData();
    }

    private void setupPieChart(PieChart chart) {
//        dailyGoalChart.setEntryLabelTextSize(15);
//        dailyGoalChart.setEntryLabelTypeface(getResources().getFont(R.font.robotocondensed_regular));
        chart.setCenterTextSize(20);
        chart.setHoleRadius(70);
        chart.setTransparentCircleRadius(65);
        chart.setCenterTextTypeface(getResources().getFont(R.font.robotocondensed_regular));
        chart.setDrawHoleEnabled(true);
        chart.setHoleColor(Color.TRANSPARENT);
        chart.getDescription().setEnabled(false);
        chart.getLegend().setEnabled(false);
        chart.setDrawEntryLabels(false);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.statistics1, null);

        dailyGoalChart = (PieChart) v.findViewById(R.id.progress_chart);
        weeklyGoalChart = (PieChart) v.findViewById(R.id.weekly_goal);
        setupPieChart(dailyGoalChart);
        setupPieChart(weeklyGoalChart);
        return v;
    }

    /**
     * Function which resumes counting of steps and sets the icon to resume
     */
    @Override
    public void onResume() {
        super.onResume();

        Database db = Database.getInstance(getActivity());

        if (BuildConfig.DEBUG) db.logState();
        // read todays offset
        todayOffset = db.getSteps(Util.getToday());

        SharedPreferences prefs =
                getActivity().getSharedPreferences("pedometer", Context.MODE_PRIVATE);

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
        updateWeeklyProgress();
        //stepsDistanceChanged();
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

    public void updateDailyProgress() {

        List<PieEntry> entries = new ArrayList<>();
        int steps_today = Math.max(todayOffset + since_boot, 0);

        if (goal > steps_today) {

            float completed = (float) steps_today / (float) goal * 100;
            float remaining = 100 - completed;

            entries.add(new PieEntry(completed, "Completed"));
            entries.add(new PieEntry(remaining, "Remaining"));
        } else {
            entries.add(new PieEntry(100.00f, "Completed"));
        }

        dailyGoalChart.setCenterText(generateCenterSpannableText(goal, steps_today));

        PieDataSet set = new PieDataSet(entries, "");
        final int[] MY_COLORS = {Color.rgb(25,114,120), Color.rgb(239,45,86)};
        ArrayList<Integer> colors = new ArrayList<Integer>();

        for(int c: MY_COLORS) colors.add(c);

        set.setColors(colors);
        set.setValueFormatter(new PercentFormatter());
        set.setDrawValues(false);

        PieData data = new PieData(set);
//        data.setValueTextColor(Color.parseColor("#ffffff"));
//        data.setValueTextSize(15f);
//        data.setValueTypeface(getResources().getFont(R.font.roboto_bold));

        dailyGoalChart.setData(data);
        dailyGoalChart.invalidate(); // refresh
    }

    public void updateWeeklyProgress() {

        List<PieEntry> entries = new ArrayList<>();
        int steps_today = Math.max(todayOffset + since_boot, 0);

        if (goal > steps_today) {

            float completed = (float) steps_today / (float) goal * 100;
            float remaining = 100 - completed;

            entries.add(new PieEntry(completed, "Completed"));
            entries.add(new PieEntry(remaining, "Remaining"));
        } else {
            entries.add(new PieEntry(100.00f, "Completed"));
        }

        PieDataSet set = new PieDataSet(entries, "");
        final int[] MY_COLORS = {Color.rgb(25,114,120), Color.rgb(239,45,86)};
        ArrayList<Integer> colors = new ArrayList<Integer>();

        for(int c: MY_COLORS) colors.add(c);

        set.setColors(colors);
        set.setValueFormatter(new PercentFormatter());
        set.setDrawValues(false);

        PieData data = new PieData(set);
//        data.setValueTextColor(Color.parseColor("#ffffff"));
//        data.setValueTextSize(15f);
//        data.setValueTypeface(getResources().getFont(R.font.roboto_bold));

        weeklyGoalChart.setData(data);
        weeklyGoalChart.invalidate(); // refresh
    }

    private SpannableString generateCenterSpannableText(int goal, int steps_taken) {

        String goalText = NumberFormat.getNumberInstance(Locale.US).format(goal);
        String stepsTakenText = NumberFormat.getNumberInstance(Locale.US).format(steps_taken);
        String stepsLabel = String.format("%s\n/%s\nSTEPS", stepsTakenText, goalText);
        SpannableString s = new SpannableString(stepsLabel);

        //  formats the completed steps text
        s.setSpan(new RelativeSizeSpan(2.5f), 0, stepsTakenText.length(), 0);
        s.setSpan(new StyleSpan(Typeface.BOLD), 0, stepsTakenText.length(), 0);

        // formats the "goal" text
        s.setSpan(new RelativeSizeSpan(1.0f), stepsTakenText.length() + 1, goalText.length() + stepsTakenText.length() + 2,  0);
        s.setSpan(new StyleSpan(Typeface.BOLD), stepsTakenText.length() + 1,goalText.length() + stepsTakenText.length() + 2, 0);

        // formats the STEPS text
        s.setSpan(new RelativeSizeSpan(0.8f), goalText.length() + stepsTakenText.length() + 3, s.length(),  0);
        s.setSpan(new StyleSpan(Typeface.NORMAL), goalText.length() + stepsTakenText.length() + 3, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), goalText.length() + stepsTakenText.length() + 3, s.length(), 0);

        return s;
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
        //updatePie();
    }
}
