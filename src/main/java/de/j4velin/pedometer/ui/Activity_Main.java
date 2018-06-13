package de.j4velin.pedometer.ui;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesActivityResultCodes;

import de.j4velin.pedometer.BuildConfig;
import de.j4velin.pedometer.R;
import de.j4velin.pedometer.util.GoogleFit;
import de.j4velin.pedometer.util.Logger;

/**
 * Created by dkao on 2/11/2018.
 */

public class Activity_Main extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        Count_Fragment.OnFragmentInteractionListener,
        History_Fragment.OnFragmentInteractionListener {

    private GoogleApiClient mGoogleApiClient;
    private final static int RC_RESOLVE = 1;
    private final static int RC_LEADERBOARDS = 2;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            //  Achievements and leaderboards will crash the app since PlayServices has yet to be
            //  imported properly
            return optionsItemSelected(item);
        }

    };

//    /**
//     * Creates the expanded view of the option menu and sets the pause/resume icon
//     * to the approriate icon
//     * @param menu
//     * @param inflater
//     */
//    @Override
//    public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
//        inflater.inflate(R.menu.main, menu);
//        MenuItem pause = menu.getItem(0);
//        Drawable d;
//        if (this.getSharedPreferences("de.dkao.de.dkao.pedometer", Context.MODE_PRIVATE)
//                .contains("pauseCount")) { // currently paused
//            pause.setTitle(R.string.resume);
//            d = getResources().getDrawable(R.drawable.ic_resume);
//        } else {
//            pause.setTitle(R.string.pause);
//            d = getResources().getDrawable(R.drawable.ic_pause);
//        }
//        d.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
//        pause.setIcon(d);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(R.style.AppTheme); //  remove splash screen, default is AppTheme.Launcher
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navbar_fragments);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_list_black_24dp);

        if (savedInstanceState == null) {

            Fragment newFragment = new Statistics_Activity();
            android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            transaction.replace(R.id.container, newFragment);
            transaction.commit();
        }

        GoogleApiClient.Builder builder = new GoogleApiClient.Builder(this, this, this);
        builder.addApi(Games.API, Games.GamesOptions.builder().build());
        builder.addScope(Games.SCOPE_GAMES);
        builder.addApi(Fitness.HISTORY_API);
        builder.addApi(Fitness.RECORDING_API);
        builder.addScope(new Scope(Scopes.FITNESS_ACTIVITY_READ_WRITE));
        mGoogleApiClient = builder.build();

        if (BuildConfig.DEBUG && Build.VERSION.SDK_INT >= 23 && PermissionChecker
                .checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PermissionChecker.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (BuildConfig.DEBUG) Logger.log("Main::onStart");
        if (getSharedPreferences("pedometer_playservices", Context.MODE_PRIVATE)
                .getBoolean("autosignin", false) && !mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (BuildConfig.DEBUG) Logger.log("Main::onStop");
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    public GoogleApiClient getGC() {
        return mGoogleApiClient;
    }

    public void beginSignIn() {
        if (!mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }
    }

    public void signOut() {
        if (mGoogleApiClient.isConnected()) {
            Games.signOut(mGoogleApiClient);
            mGoogleApiClient.disconnect();
        }
        getSharedPreferences("pedometer_playservices", Context.MODE_PRIVATE).edit()
                .putBoolean("autosignin", false).apply();
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStackImmediate();
        } else {
            finish();
        }
    }

    public boolean optionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_statistics:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, new Statistics_Activity()).addToBackStack(null)
                        .commit();
                break;
            case R.id.action_settings:
                //getSupportFragmentManager().beginTransaction().replace(R.id.container, new Settings_Fragment()).addToBackStack(null).commit();
                break;
            case R.id.action_leaderboard:
            case R.id.action_achievements:
                if (mGoogleApiClient.isConnected()) {
                    startActivityForResult(item.getItemId() == R.id.action_achievements ?
                                    Games.Achievements.getAchievementsIntent(mGoogleApiClient) :
                                    Games.Leaderboards.getAllLeaderboardsIntent(mGoogleApiClient),
                            RC_LEADERBOARDS);
                } else {
                    AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
                    builder2.setTitle(R.string.sign_in_necessary);
                    builder2.setMessage(R.string.please_sign_in_with_your_google_account);
                    builder2.setPositiveButton(android.R.string.ok,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    getFragmentManager().beginTransaction()
                                            .replace(R.id.container, new Settings_Fragment())
                                            .addToBackStack(null).commit();
                                }
                            });
                    builder2.setNegativeButton(android.R.string.cancel,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    builder2.create().show();
                }
                break;
//            case R.id.action_faq:
//                startActivity(new Intent(Intent.ACTION_VIEW,
//                        Uri.parse("http://j4velin.de/faq/index.php?app=pm"))
//                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
//                break;
            case R.id.action_about:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.about);
                TextView tv = new TextView(this);
                tv.setPadding(10, 10, 10, 10);
                tv.setText(R.string.about_text_links);
                try {
                    tv.append(getString(R.string.about_app_version,
                            getPackageManager().getPackageInfo(getPackageName(), 0).versionName));
                } catch (PackageManager.NameNotFoundException e1) {
                    // should not happen as the app is definitely installed when
                    // seeing the dialog
                    e1.printStackTrace();
                }
                tv.setMovementMethod(LinkMovementMethod.getInstance());
                builder.setView(tv);
                builder.setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                builder.create().show();
                break;
            case R.id.action_split_count:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, new Count_Fragment()).addToBackStack(null)
                        .commit();
                break;
            case R.id.action_step_history:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, new History_Fragment()).addToBackStack(null)
                        .commit();
                break;
        }
        return true;
    }

    @Override
    public void onConnected(final Bundle bundle) {
        // TO DO: Import the PlayServicesClass
//        PlayServices.achievementsAndLeaderboard(mGoogleApiClient, this);
        new GoogleFit.Sync(mGoogleApiClient, this).execute();
        Fitness.RecordingApi.subscribe(mGoogleApiClient, DataType.TYPE_STEP_COUNT_DELTA);
        getSharedPreferences("pedometer_playservices", Context.MODE_PRIVATE).edit()
                .putBoolean("autosignin", true).apply();
    }

    @Override
    public void onConnectionSuspended(int cause) {
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnectionFailed(final ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            // This problem can be fixed. So let's try to fix it.
            try {
                // launch appropriate UI flow (which might, for example, be the
                // sign-in flow)
                connectionResult.startResolutionForResult(this, RC_RESOLVE);
            } catch (IntentSender.SendIntentException e) {
                // Try connecting again
                mGoogleApiClient.connect();
            }
        } else {
            if (!isFinishing() && !isDestroyed()) {
                GooglePlayServicesUtil.getErrorDialog(connectionResult.getErrorCode(), this, 0)
                        .show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (requestCode == RC_RESOLVE) {
            // We're coming back from an activity that was launched to resolve a
            // connection problem. For example, the sign-in UI.
            if (resultCode == Activity.RESULT_OK && !mGoogleApiClient.isConnected() &&
                    !mGoogleApiClient.isConnecting()) {
                // Ready to try to connect again.
                mGoogleApiClient.connect();
            } else if (resultCode == GamesActivityResultCodes.RESULT_RECONNECT_REQUIRED &&
                    !mGoogleApiClient.isConnected() && !mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.connect();
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // User cancelled.
                mGoogleApiClient.disconnect();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri)
    {

    }
}
