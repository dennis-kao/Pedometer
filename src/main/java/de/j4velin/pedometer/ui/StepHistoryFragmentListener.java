package de.j4velin.pedometer.ui;

import android.support.design.widget.TabLayout;

import android.util.Log;

/**
 * Created by calimr on 2018-03-19.
 */

public class StepHistoryFragmentListener implements TabLayout.OnTabSelectedListener {
    private Fragment_StepHistory parent;

    public StepHistoryFragmentListener(Fragment_StepHistory parent) {
        super();
        this.parent = parent;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        switch(tab.getPosition()) {
            case 0:
                this.parent.showDayStepHistory();
                break;
            case 1:
                this.parent.showWeekStepHistory();
                break;
            case 2:
                this.parent.showMonthStepHistory();
                break;
            default:
                Log.e("STEP_HISTORY_TAB_LAYOUT", "Unexpected Tab selected: " + tab.getPosition());
                break;
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
