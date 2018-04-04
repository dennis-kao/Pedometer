package de.j4velin.pedometer.ui;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import de.j4velin.pedometer.Database;
import de.j4velin.pedometer.R;
import de.j4velin.pedometer.util.Util;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Fragment_SplitCount.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Fragment_SplitCount#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_SplitCount extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private long split_date;
    private int totalSteps = -1, split_steps, todayOffset, total_start, since_boot;
    private float stepsize, distance;
    private static boolean split_active;

    private TextView stepsText, distanceText;

    public Fragment_SplitCount() {
        // Required empty public constructor
        totalSteps = 0;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_SplitCount.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_SplitCount newInstance(String param1, String param2) {
        Fragment_SplitCount fragment = new Fragment_SplitCount();
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

    public void updateStepsAndDistanceData()
    {
        Database db = Database.getInstance(getContext());
        todayOffset = db.getSteps(Util.getToday());
        total_start = db.getTotalWithoutToday();
        since_boot = db.getCurrentSteps();

        totalSteps = total_start + Math.max(todayOffset + since_boot, 0);
        db.close();
    }

    public void updateStepsAndDistanceText(int newDistance, int newSteps)
    {
        distance = newDistance;

        distanceText
                .setText(Fragment_Overview.formatter.format(newDistance));

        stepsText
                .setText(Fragment_Overview.formatter.format(newSteps));
    }

    @Override
    public void onResume()
    {
        super.onResume();
//        updateStepsAndDistance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View d = inflater.inflate(R.layout.dialog_split, container, false);

        final Context c = getContext();

        final SharedPreferences prefs =
                c.getSharedPreferences("pedometer", Context.MODE_MULTI_PROCESS);

        updateStepsAndDistanceData();

        split_date = prefs.getLong("split_date", -1);
        split_steps = prefs.getInt("split_steps", totalSteps);
        stepsText = d.findViewById(R.id.steps);
        stepsText.setText(Fragment_Overview.formatter.format(totalSteps - split_steps));
        stepsize = prefs.getFloat("stepsize_value", Fragment_Settings.DEFAULT_STEP_SIZE);
        distance = (totalSteps - split_steps) * stepsize;

        //shows steps taken in kilometer
        if (prefs.getString("stepsize_unit", Fragment_Settings.DEFAULT_STEP_UNIT).equals("cm")) {
            distance /= 100000;
            ((TextView) d.findViewById(R.id.distanceunit)).setText("km");
        } else {
            //shows steps taken in miles
            distance /= 5280;
            ((TextView) d.findViewById(R.id.distanceunit)).setText("mi");
        }

        distanceText =  d.findViewById(R.id.distance);
        distanceText.setText(Fragment_Overview.formatter.format(distance));

        //  split date is used to determine if a split count is active
        split_active = split_date > 0;
        //getting date and time when steps were taken
        if (split_date == -1) split_date = System.currentTimeMillis();  //  if getting the split date failed, set it to the current day
        ((TextView) d.findViewById(R.id.date)).setText(c.getString(R.string.since,
                java.text.DateFormat.getDateTimeInstance().format(split_date)));

        //once stop has been hit, user's step and distance gets stored in the history
        final Button startstop = d.findViewById(R.id.start);
        startstop.setText(split_active ? R.string.stop : R.string.start);
        startstop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (!split_active) {
                    prefs.edit().putLong("split_date", System.currentTimeMillis())
                            .putInt("split_steps", totalSteps).apply();
                    split_active = true;
                } else {
                    prefs.edit().remove("split_date").remove("split_steps").apply();
                    split_active = false;
                    updateStepsAndDistanceText(0, 0);
                }
                startstop.setText(split_active ? R.string.stop : R.string.start);
            }
        });

        return d;
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

//    @Override
//    public boolean onOptionsItemSelected(final MenuItem item)
//    {
//
//        return ((Fragment_Overview) getContext()).onOptionsItemSelected(item);
//    }
}
