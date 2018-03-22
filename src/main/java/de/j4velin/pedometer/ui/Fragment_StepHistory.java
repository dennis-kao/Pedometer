package de.j4velin.pedometer.ui;

import android.content.Context;
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

import de.j4velin.pedometer.Database;
import de.j4velin.pedometer.R;
import de.j4velin.pedometer.obj.Day_Step_History;
import de.j4velin.pedometer.obj.Week_Step_History;
import de.j4velin.pedometer.obj.Month_Step_History;

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

//    private StepHistoryFragmentListener shfListener;

    private ListView lsView;

    private ArrayList<Day_Step_History> dayHistoryRecords = null;
    private StepHistoryCellAdapter dayStepHistoryAdapter = null;

    private ArrayList<Week_Step_History> weekHistoryRecords = null;
    private WeekStepHistoryCellAdapter weekStepHistoryCellAdapter = null;

    private ArrayList<Month_Step_History> monthHistoryRecords = null;
    private MonthStepHistoryCellAdapter monthStepHistoryCellAdapter = null;

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
        /*Hard-Coded Values
        // Construct the data source
        ArrayList<Day_Step_History> arrayOfUsers = new ArrayList<Day_Step_History>();
        arrayOfUsers.add(new Day_Step_History());
        arrayOfUsers.add(new Day_Step_History());
        // Create the adapter to convert the array to views
        StepHistoryCellAdapter adapter = new StepHistoryCellAdapter(getContext(), arrayOfUsers);
        // Attach the adapter to a ListView
        this.lsView.setAdapter(adapter);*/
        Database db = Database.getInstance((getActivity()));

        this.dayHistoryRecords = db.stepHistoryByDay();
        this.dayStepHistoryAdapter = new StepHistoryCellAdapter(getContext(), this.dayHistoryRecords);
        if (this.dayHistoryRecords == null)
            Log.e("STEP_HISTORY", "MonthHistoryRecords null");
        else{
            this.lsView.setAdapter(this.dayStepHistoryAdapter);
        }
    }

    public void showWeekStepHistory() {
        // call update function for week step history list
        // change adapter
        Database db = Database.getInstance((getActivity()));

        this.weekHistoryRecords = db.getAllStepHistoryByWeek();
        if (this.weekStepHistoryCellAdapter == null) {
            this.weekStepHistoryCellAdapter = new WeekStepHistoryCellAdapter(getContext(), this.weekHistoryRecords);
        } else {
            this.weekStepHistoryCellAdapter.updateList(this.weekHistoryRecords);
        }

        if (this.weekHistoryRecords == null) {
            Log.e("STEP_HISTORY", "WeekHistoryRecords null");
        } else {
            this.lsView.setAdapter(this.weekStepHistoryCellAdapter);
        }
    }

    public void showMonthStepHistory() {
        /*
        //Hard-Coded Values

        // Construct the data source
        ArrayList<Month_Step_History> arrayOfUsers = new ArrayList<Month_Step_History>();
        arrayOfUsers.add(new Month_Step_History());
        arrayOfUsers.add(new Month_Step_History());
        arrayOfUsers.add(new Month_Step_History());
        // Create the adapter to convert the array to views
        MonthStepHistoryCellAdapter adapter = new MonthStepHistoryCellAdapter(getContext(), arrayOfUsers);
        // Attach the adapter to a ListView
        this.lsView.setAdapter(adapter);
        */
        Database db = Database.getInstance((getActivity()));

        this.monthHistoryRecords = db.stepHistoryByMonth();
        this.monthStepHistoryCellAdapter = new MonthStepHistoryCellAdapter(getContext(), this.monthHistoryRecords);
        if (this.monthHistoryRecords == null)
            Log.e("STEP_HISTORY", "MonthHistoryRecords null");
        else{
            this.lsView.setAdapter(this.monthStepHistoryCellAdapter);
        }
    }

   /* public void showMonthStepHistory() {
        Database db = Database.getInstance((getActivity()));
    }*/



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
