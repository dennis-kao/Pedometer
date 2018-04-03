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

    private ArrayList<DayStepHistory> dayHistoryRecords = null;
    private ArrayList<WeekStepHistory> weekHistoryRecords = null;
    private ArrayList<MonthStepHistory> monthHistoryRecords = null;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment__step_history , container, false);
        TabLayout recordTypeTab = view.findViewById(R.id.tab_layout);
        this.lsView = view.findViewById(R.id.step_history_list);

        recordTypeTab.addOnTabSelectedListener(new StepHistoryFragmentListener(this));
        this.showDayStepHistory();
        return view;
    }

    public void showDayStepHistory() {

        Database db = Database.getInstance((getActivity()));
        SharedPreferences prefs =
                getActivity().getSharedPreferences("pedometer", Context.MODE_PRIVATE);
        float stepsize = prefs.getFloat("stepsize_value", Fragment_Settings.DEFAULT_STEP_SIZE);
        this.dayHistoryRecords = db.stepHistoryByDay(stepsize);
        this.stepHistoryCellAdapter = new StepHistoryCellAdapter(getContext(), this.dayHistoryRecords);
        if (this.dayHistoryRecords == null)
            Log.e("STEP_HISTORY", "DayHistoryRecords null");
        else{
            this.lsView.setAdapter(this.stepHistoryCellAdapter);
        }
    }

    public void showWeekStepHistory() {
        // call update function for week step history list
        // change adapter
        Database db = Database.getInstance((getActivity()));

        SharedPreferences prefs =
                getActivity().getSharedPreferences("pedometer", Context.MODE_PRIVATE);
        float stepsize = prefs.getFloat("stepsize_value", Fragment_Settings.DEFAULT_STEP_SIZE);
        this.weekHistoryRecords = db.getAllStepHistoryByWeek(stepsize);
        this.stepHistoryCellAdapter = new StepHistoryCellAdapter(getContext(), this.weekHistoryRecords);

        if (this.weekHistoryRecords == null) {
            Log.e("STEP_HISTORY", "WeekHistoryRecords null");
        } else {
            this.lsView.setAdapter(this.stepHistoryCellAdapter);
        }
    }

    public void showMonthStepHistory() {

        Database db = Database.getInstance((getActivity()));

        SharedPreferences prefs =
                getActivity().getSharedPreferences("pedometer", Context.MODE_PRIVATE);
        float stepsize = prefs.getFloat("stepsize_value", Fragment_Settings.DEFAULT_STEP_SIZE);
        this.monthHistoryRecords = db.stepHistoryByMonth(stepsize);
        this.stepHistoryCellAdapter = new StepHistoryCellAdapter(getContext(), this.monthHistoryRecords);

        if (this.monthHistoryRecords == null)
            Log.e("STEP_HISTORY", "MonthHistoryRecords null");
        else{
            this.lsView.setAdapter(this.stepHistoryCellAdapter);
        }
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
