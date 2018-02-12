package de.j4velin.pedometer;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.support.annotation.NonNull;
import android.view.MenuItem;


/**
 * Created by dkao on 2/11/2018.
 */

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {

//                case android.R.id.home:
//                    getFragmentManager().popBackStackImmediate();
//                    break;
//                case R.id.action_settings:
//                    getFragmentManager().beginTransaction()
//                            .replace(android.R.id.content, new Fragment_Settings()).addToBackStack(null)
//                            .commit();
//                    break;
//                case R.id.action_leaderboard:
//                case R.id.action_achievements:
//                    if (mGoogleApiClient.isConnected()) {
//                        startActivityForResult(item.getItemId() == R.id.action_achievements ?
//                                        Games.Achievements.getAchievementsIntent(mGoogleApiClient) :
//                                        Games.Leaderboards.getAllLeaderboardsIntent(mGoogleApiClient),
//                                RC_LEADERBOARDS);
//                    } else {
//                        AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
//                        builder2.setTitle(R.string.sign_in_necessary);
//                        builder2.setMessage(R.string.please_sign_in_with_your_google_account);
//                        builder2.setPositiveButton(android.R.string.ok,
//                                new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        dialog.dismiss();
//                                        getFragmentManager().beginTransaction()
//                                                .replace(android.R.id.content, new Fragment_Settings())
//                                                .addToBackStack(null).commit();
//                                    }
//                                });
//                        builder2.setNegativeButton(android.R.string.cancel,
//                                new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        dialog.dismiss();
//                                    }
//                                });
//                        builder2.create().show();
//                    }
//                    break;


                case R.id.action_statistics:
                    break;
                case R.id.action_achievements:
                    break;
                case R.id.action_leaderboard:
                    break;
            }
            return true;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navbar_fragments);

//        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Disable the translation inside the CoordinatorLayout
        navigation.setBehaviorTranslationEnabled(false);

    }

}
