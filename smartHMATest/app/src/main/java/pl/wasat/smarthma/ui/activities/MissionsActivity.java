package pl.wasat.smarthma.ui.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import pl.wasat.smarthma.R;
import pl.wasat.smarthma.helper.Const;
import pl.wasat.smarthma.model.mission.MissionItemData;
import pl.wasat.smarthma.parser.Parser.Parser;
import pl.wasat.smarthma.parser.database.ParserDb;
import pl.wasat.smarthma.ui.activities.base.BaseSmartHMActivity;
import pl.wasat.smarthma.ui.frags.missions.MissionsDetailsFragment;
import pl.wasat.smarthma.ui.frags.missions.MissionsDetailsFragment.OnMissionsDetailNewFragmentListener;
import pl.wasat.smarthma.ui.frags.missions.MissionsExtListFragment;
import pl.wasat.smarthma.ui.frags.missions.MissionsExtListFragment.OnExtendedListFragmentListener;

public class MissionsActivity extends BaseSmartHMActivity implements
        OnExtendedListFragmentListener, OnMissionsDetailNewFragmentListener {


    private MissionsExtListFragment extendedListFragment;
    public boolean detail, start;
    String objective;
    MissionItemData missionObjective;
    MissionsDetailsFragment missionsDetailNewFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        start = false;
        detail = false;

        RelativeLayout btnBack = (RelativeLayout) findViewById(R.id.btnBackTwoPanel);
        btnBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(MissionsActivity.this, StartActivity.class);
                startActivity(startIntent);
            }
        });

        TextView text = (TextView) findViewById(R.id.action_bar_title);
        text.setText("ESA Missions");

        FrameLayout listLayout = (FrameLayout) findViewById(R.id.activity_base_list_container);
        listLayout.setBackgroundColor(Color.parseColor("#D9D9D9"));
        ParserDb parserDb = new ParserDb(this);
        parserDb.open();

        //jesli jest w bazie 60 misji, to ladujemy fragment
        if (parserDb.getMissionCount() == 60) {
            extendedListFragment = MissionsExtListFragment
                    .newInstance(one_Panel);

            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.activity_base_list_container, extendedListFragment,
                            "ExtendedListFragment").commit();

            objective = getString(R.string.mission_activity_objective);

            missionObjective = new MissionItemData(0,
                    "ESA Earth Observation Missions",
                    "https://earth.esa.int/web/guest/missions", "", objective);

            missionsDetailNewFragment = MissionsDetailsFragment
                    .newInstance(missionObjective);

            if (!one_Panel) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.activity_base_details_container, missionsDetailNewFragment,
                                "ExtendedListFragment").commit();
                extendedListFragment.detail = false;
            }
        }

        //w przeciwnym wypadku odpalamy async task
        else {
            //AsyncTask task =
            new ProgressTask(this).execute();

        }

    }


    @Override
    public void onBackPressed() {
        if (one_Panel) {
            if (missionsDetailNewFragment != null) {
                if (extendedListFragment.detail) {

                    if (getSupportFragmentManager().findFragmentByTag("ExtendedListFragment") == null) {
                        getSupportFragmentManager()
                                .beginTransaction()
                                .add(R.id.activity_base_list_container, extendedListFragment,
                                        "ExtendedListFragment").commit();
                        extendedListFragment.detail = false;
                    } else {
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.activity_base_list_container, extendedListFragment,
                                        "ExtendedListFragment").commit();
                        extendedListFragment.detail = false;
                    }

                } else {
                    super.onBackPressed();
                }
            } else {
                super.onBackPressed();
            }
        }
        else
        {super.onBackPressed();}
    }

    @Override
    public void onMissionsDetailNewFragmentSearchData(String missionName) {
        Intent showProductsIntent = new Intent(this,
                SearchCollectionResultsActivity.class);
        showProductsIntent.setAction(Const.KEY_ACTION_SEARCH_MISSION_DATA);
        showProductsIntent.putExtra(Const.KEY_INTENT_QUERY, missionName);
        startActivityForResult(showProductsIntent, REQUEST_NEW_SEARCH);

    }

    public MissionsExtListFragment getExtendedListFragment() {
        return extendedListFragment;
    }

    public class ProgressTask extends AsyncTask<String, String, Boolean> {

        public ProgressTask(MissionsActivity activity) {
            this.activity = activity;
            dialog = new ProgressDialog(activity);
            parserDb = new ParserDb(activity);
            parserDb.open();
        }

        /**
         * progress dialog to show user that the backup is processing.
         */
        private final ProgressDialog dialog;
        /**
         * application context.
         */
        private final MissionsActivity activity;
        private final ParserDb parserDb;
        private MissionsExtListFragment extendedListFragment;

        protected void onPreExecute() {
            dialog.setMessage("Loading ESA EO MISSIONS, please wait...");
            dialog.setMax(60);
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.setCancelable(false);
            dialog.show();
        }


        @Override
        protected void onPostExecute(final Boolean success) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            extendedListFragment = MissionsExtListFragment
                    .newInstance(one_Panel);

            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.activity_base_list_container, extendedListFragment,
                            "ExtendedListFragment").commit();

            String objective = getString(R.string.mission_activity_objective);
            MissionItemData missionObjective = new MissionItemData(0,
                    "ESA Earth Observation Missions",
                    "https://earth.esa.int/web/guest/missions", "", objective);
            MissionsDetailsFragment missionsDetailNewFragment = MissionsDetailsFragment
                    .newInstance(missionObjective);
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.activity_base_details_container,
                            missionsDetailNewFragment, "MissionsDetailNewFragment")
                    .commit();

            parserDb.close();

        }


        protected Boolean doInBackground(final String... args) {
            try {

                Parser parser = new Parser(activity);
                publishProgress("Loading ESA EO MISSIONS, please wait...", "2");
                parser.cat0();
                publishProgress("Loading ESA FUTURE MISSIONS, please wait...", "9");

                parser.cat1();
                publishProgress("Loading THIRD PARTY MISSIONS, please wait...", "16");

                parser.cat2();
                publishProgress("Loading HISTORICAL MISSIONS, please wait...", "41");

                parser.cat3();
                publishProgress("Loading POTENTIAL MISSIONS, please wait...", "50");

                parser.cat4();
                publishProgress("Loading OTHER MISSIONS, please wait...", "56");

                parser.cat5();
                return true;
            } catch (Exception e) {
                Log.e("tag", "error", e);
            }
            return false;
        }

        @Override
        protected void onProgressUpdate(String... message) {
            super.onProgressUpdate();
            dialog.setMessage(message[0]);
            dialog.setProgress(Integer.parseInt(message[1]));
        }

    }


}
