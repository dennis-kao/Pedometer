package de.j4velin.pedometer.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.support.design.widget.TabLayout;
import android.widget.Spinner;

import java.util.ArrayList;

import de.j4velin.pedometer.Database;
import de.j4velin.pedometer.R;
import de.j4velin.pedometer.obj.DayStepHistory;
import de.j4velin.pedometer.obj.MonthStepHistory;
import de.j4velin.pedometer.obj.WeekStepHistory;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link History_Fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link History_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class History_Fragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private ListView lsView;

    private Spinner dateSpinner;
    private Spinner sortSpinner;

    private ArrayList<DayStepHistory> dayHistoryRecords = null;
    private ArrayList<WeekStepHistory> weekHistoryRecords = null;
    private ArrayList<MonthStepHistory> monthHistoryRecords = null;
    private HistoryCellAdapter stepHistoryCellAdapter = null;

    private SharedPreferences prefs;

    private float stepsize;
    private float weight;

    private Database db;

    public History_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment History_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static History_Fragment newInstance(String param1, String param2) {
        History_Fragment fragment = new History_Fragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //GRAB USER DATA
        prefs = getActivity().getSharedPreferences("de.dkao.de.dkao.pedometer", Context.MODE_PRIVATE);
        stepsize = prefs.getFloat("stepsize_value", Settings_Fragment.DEFAULT_STEP_SIZE);
        weight = prefs.getFloat("weight_value", Settings_Fragment.DEFAULT_WEIGHT);
        db = Database.getInstance((getActivity()));

        this.dayHistoryRecords = db.stepHistoryByDay(stepsize, weight);
        this.weekHistoryRecords = db.getAllStepHistoryByWeek(stepsize, weight);
        this.monthHistoryRecords = db.stepHistoryByMonth(stepsize, weight);
    }

    public void sortListener(int option) {

        int currentTimeFrame = dateSpinner.getSelectedItemPosition();

        //  sort the appropriate ArrayList
        switch (option) {
            case 0:
                if (currentTimeFrame == 0) dayHistoryRecords.sort(DayStepHistory.StepComparator);
                else if (currentTimeFrame == 1) weekHistoryRecords.sort(WeekStepHistory.StepComparator);
                else monthHistoryRecords.sort(MonthStepHistory.StepComparator);
                break;
            case 1:
                if (currentTimeFrame == 0) dayHistoryRecords.sort(DayStepHistory.DayDateComparator);
                else if (currentTimeFrame == 1) weekHistoryRecords.sort(WeekStepHistory.WeekDateComparator);
                else monthHistoryRecords.sort(MonthStepHistory.MonthDateComparator);
                break;
            case 2:
                if (currentTimeFrame == 0) dayHistoryRecords.sort(DayStepHistory.DayGoalComparator);
                break;
            case 3:
                if (currentTimeFrame == 1) weekHistoryRecords.sort(WeekStepHistory.WeekMedianComparator);
                else monthHistoryRecords.sort(MonthStepHistory.MonthMedianComparator);
                break;
            case 4:
                if (currentTimeFrame == 1) weekHistoryRecords.sort(WeekStepHistory.WeekBestDayComparator);
                else monthHistoryRecords.sort(MonthStepHistory.MonthBestDayComparator);
                break;
            case 5:
                if (currentTimeFrame == 1) weekHistoryRecords.sort(WeekStepHistory.WeekStdDevComparator);
                else monthHistoryRecords.sort(MonthStepHistory.MonthStdDevComparator);
                break;
            default:
                break;
        }

        //  feed to adapter and change the view
        if (currentTimeFrame == 0)  {
            this.stepHistoryCellAdapter = new HistoryCellAdapter(getContext(), this.dayHistoryRecords);
        }
        else if (currentTimeFrame == 1) {
            this.stepHistoryCellAdapter = new HistoryCellAdapter(getContext(), this.weekHistoryRecords);
        }
        else {
            this.stepHistoryCellAdapter = new HistoryCellAdapter(getContext(), this.monthHistoryRecords);
        }

        this.lsView.setAdapter(this.stepHistoryCellAdapter);
    }

    public Spinner setupSpinner(View view, int id, int arrayID, AdapterView.OnItemSelectedListener listener) {
        Spinner s = view.findViewById(id);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                arrayID, R.layout.spinner_item);
        // Specify the layout to use when the list of choices appears
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        s.setAdapter(adapter);
        s.setOnItemSelectedListener(listener);
        s.setSelection(0);

        return s;
    }

    public Spinner setupSpinner(View view, int id, ArrayAdapter<String> adapter, AdapterView.OnItemSelectedListener listener) {
        Spinner s = view.findViewById(id);
        // Specify the layout to use when the list of choices appears
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        s.setAdapter(adapter);
        s.setOnItemSelectedListener(listener);
        s.setSelection(0);

        return s;
    }

    public void setupSortSpinner(View view, AdapterView.OnItemSelectedListener listener) {

        final String[] sortArray = {"Steps", "Date", "Median", "Dev.", "Best day"};

        ArrayAdapter<String> sortAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item, sortArray) {
            @Override
            public int getCount() {
                if (dateSpinner.getSelectedItemPosition() == 0) return sortArray.length - 3; // Steps, Date
                else return sortArray.length;
            }
        };

        sortSpinner = setupSpinner(view, R.id.sort_spinner, sortAdapter, listener);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_step_history , container, false);

        dateSpinner = setupSpinner(view, R.id.date_spinner, R.array.date_array, new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                if (sortSpinner != null) sortSpinner.setSelection(0);

                switch(position) {
                    case 0:
                        showDayStepHistory();
                        break;
                    case 1:
                        showWeekStepHistory();
                        break;
                    case 2:
                        showMonthStepHistory();
                        break;
                    default:
                        Log.e("STEP_HISTORY_TAB_LAYOUT", "Unexpected Tab selected: " + position);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        setupSortSpinner(view, new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
            int position, long id) {

                sortListener(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        this.lsView = view.findViewById(R.id.step_history_list);

        return view;
    }

    public void showDayStepHistory() {

        this.stepHistoryCellAdapter = new HistoryCellAdapter(getContext(), this.dayHistoryRecords);
        if (this.dayHistoryRecords == null)
            Log.e("STEP_HISTORY", "DayHistoryRecords null");
        else{
            this.lsView.setAdapter(this.stepHistoryCellAdapter);
        }
    }

    public void showWeekStepHistory() {

        this.stepHistoryCellAdapter = new HistoryCellAdapter(getContext(), this.weekHistoryRecords);

        if (this.weekHistoryRecords == null) {
            Log.e("STEP_HISTORY", "WeekHistoryRecords null");
        } else {
            this.lsView.setAdapter(this.stepHistoryCellAdapter);
        }
    }

    public void showMonthStepHistory() {

        this.stepHistoryCellAdapter = new HistoryCellAdapter(getContext(), this.monthHistoryRecords);

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
