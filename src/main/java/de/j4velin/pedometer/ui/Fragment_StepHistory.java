package de.j4velin.pedometer.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.support.design.widget.TabLayout;

import com.github.clans.fab.FloatingActionMenu;
import com.github.clans.fab.FloatingActionButton;


import java.util.ArrayList;
import java.util.Collections;

import de.j4velin.pedometer.Database;
import de.j4velin.pedometer.R;
import de.j4velin.pedometer.obj.DayStepHistory;
import de.j4velin.pedometer.obj.MonthStepHistory;
import de.j4velin.pedometer.obj.WeekStepHistory;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Fragment_StepHistory.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Fragment_StepHistory#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_StepHistory extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private ListView lsView;
    private TabLayout recordTypeTab;

    private ArrayList<DayStepHistory> dayHistoryRecords = null;
    private ArrayList<WeekStepHistory> weekHistoryRecords = null;
    private ArrayList<MonthStepHistory> monthHistoryRecords = null;

    private FloatingActionMenu fabMenu;
    private FloatingActionButton stepButton;
    private FloatingActionButton dateButton;
    private FloatingActionButton goalButton;
    private FloatingActionButton stdDevButton;
    private FloatingActionButton medianButton;
    private FloatingActionButton bestDayButton;

    private StepHistoryCellAdapter stepHistoryCellAdapter = null;

    public Fragment_StepHistory() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_StepHistory.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_StepHistory newInstance(String param1, String param2) {
        Fragment_StepHistory fragment = new Fragment_StepHistory();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        setHasOptionsMenu(true);
    }

    public void sortListener(String option) {

        int currentTimeFrame = recordTypeTab.getSelectedTabPosition();

        //  sort the appropriate ArrayList
        switch (option) {
            case "Step":
                if (currentTimeFrame == 0) dayHistoryRecords.sort(DayStepHistory.StepComparator);
                else if (currentTimeFrame == 1) weekHistoryRecords.sort(WeekStepHistory.StepComparator);
                else monthHistoryRecords.sort(MonthStepHistory.StepComparator);
                break;
            case "Date":
                if (currentTimeFrame == 0) dayHistoryRecords.sort(DayStepHistory.DayDateComparator);
                else if (currentTimeFrame == 1) weekHistoryRecords.sort(WeekStepHistory.WeekDateComparator);
                else monthHistoryRecords.sort(MonthStepHistory.MonthDateComparator);
                break;
            case "Goal":
                if (currentTimeFrame == 0) dayHistoryRecords.sort(DayStepHistory.DayGoalComparator);
                break;
            case "Median":
                if (currentTimeFrame == 1) weekHistoryRecords.sort(WeekStepHistory.WeekMedianComparator);
                else monthHistoryRecords.sort(MonthStepHistory.MonthMedianComparator);
                break;
            case "Best Day":
                if (currentTimeFrame == 1) weekHistoryRecords.sort(WeekStepHistory.WeekBestDayComparator);
                else monthHistoryRecords.sort(MonthStepHistory.MonthBestDayComparator);
                break;
            case "Std. Dev.":
                if (currentTimeFrame == 1) weekHistoryRecords.sort(WeekStepHistory.WeekStdDevComparator);
                else monthHistoryRecords.sort(MonthStepHistory.MonthStdDevComparator);
                break;
            default:
                break;
        }

        //  feed to adapter and change the view
        if (currentTimeFrame == 0)  {
            this.stepHistoryCellAdapter = new StepHistoryCellAdapter(getContext(), this.dayHistoryRecords);
        }
        else if (currentTimeFrame == 1) {
            this.stepHistoryCellAdapter = new StepHistoryCellAdapter(getContext(), this.weekHistoryRecords);
        }
        else {
            this.stepHistoryCellAdapter = new StepHistoryCellAdapter(getContext(), this.monthHistoryRecords);
        }

        this.lsView.setAdapter(this.stepHistoryCellAdapter);
    }

    public FloatingActionButton setupButton(String buttonText) {
        final FloatingActionButton newButton = new FloatingActionButton((getContext()));
        newButton.setLabelText(buttonText);
        newButton.setButtonSize(FloatingActionButton.SIZE_MINI);
        newButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortListener(newButton.getLabelText());
            }
        });

        return newButton;
    }

    public void setOptions(String timeFrameSelected) {

        fabMenu.removeAllMenuButtons();

        //  cells are always sortable by Date and Steps
        fabMenu.addMenuButton(stepButton);
        fabMenu.addMenuButton(dateButton);

        if (timeFrameSelected.equals("day")) {
            fabMenu.addMenuButton(goalButton);
        }
        else if ((timeFrameSelected.equals("week")) || (timeFrameSelected.equals("month"))) {
            fabMenu.addMenuButton(stdDevButton);
            fabMenu.addMenuButton(medianButton);
            fabMenu.addMenuButton(bestDayButton);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment__step_history , container, false);
        recordTypeTab = view.findViewById(R.id.tab_layout);
        this.lsView = view.findViewById(R.id.step_history_list);

        recordTypeTab.addOnTabSelectedListener(new StepHistoryFragmentListener(this));
        this.showDayStepHistory();

        fabMenu = (FloatingActionMenu) view.findViewById(R.id.fab);
        stepButton = setupButton("Step");
        dateButton = setupButton("Date");
        goalButton = setupButton("Goal");
        stdDevButton = setupButton("Std. Dev.");
        medianButton = setupButton("Median");
        bestDayButton = setupButton("Best Day");

        setOptions("day");
        return view;
    }

    public void showDayStepHistory() {

        Database db = Database.getInstance((getActivity()));
        SharedPreferences prefs =
                getActivity().getSharedPreferences("pedometer", Context.MODE_PRIVATE);
        float stepsize = prefs.getFloat("stepsize_value", Fragment_Settings.DEFAULT_STEP_SIZE);
        float weight = prefs.getFloat("weight_value", Fragment_Settings.DEFAULT_WEIGHT);
        this.dayHistoryRecords = db.stepHistoryByDay(stepsize, weight);
        this.stepHistoryCellAdapter = new StepHistoryCellAdapter(getContext(), this.dayHistoryRecords);
        if (this.dayHistoryRecords == null)
            Log.e("STEP_HISTORY", "DayHistoryRecords null");
        else{
            this.lsView.setAdapter(this.stepHistoryCellAdapter);
        }

        if (fabMenu != null) setOptions("day");
    }

    public void showWeekStepHistory() {
        // call update function for week step history list
        // change adapter
        Database db = Database.getInstance((getActivity()));

        SharedPreferences prefs =
                getActivity().getSharedPreferences("pedometer", Context.MODE_PRIVATE);
        float stepsize = prefs.getFloat("stepsize_value", Fragment_Settings.DEFAULT_STEP_SIZE);
        float weight = prefs.getFloat("weight_value", Fragment_Settings.DEFAULT_WEIGHT);
        this.weekHistoryRecords = db.getAllStepHistoryByWeek(stepsize, weight);
        this.stepHistoryCellAdapter = new StepHistoryCellAdapter(getContext(), this.weekHistoryRecords);

        if (this.weekHistoryRecords == null) {
            Log.e("STEP_HISTORY", "WeekHistoryRecords null");
        } else {
            this.lsView.setAdapter(this.stepHistoryCellAdapter);
        }

        if (fabMenu != null) setOptions("week");
    }

    public void showMonthStepHistory() {

        Database db = Database.getInstance((getActivity()));

        SharedPreferences prefs =
                getActivity().getSharedPreferences("pedometer", Context.MODE_PRIVATE);
        float stepsize = prefs.getFloat("stepsize_value", Fragment_Settings.DEFAULT_STEP_SIZE);
        float weight = prefs.getFloat("weight_value", Fragment_Settings.DEFAULT_WEIGHT);
        this.monthHistoryRecords = db.stepHistoryByMonth(stepsize, weight);
        this.stepHistoryCellAdapter = new StepHistoryCellAdapter(getContext(), this.monthHistoryRecords);

        if (this.monthHistoryRecords == null)
            Log.e("STEP_HISTORY", "MonthHistoryRecords null");
        else{
            this.lsView.setAdapter(this.stepHistoryCellAdapter);
        }

        if (fabMenu != null) setOptions("month");
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
