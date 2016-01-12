package pl.wasat.smarthma.ui.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.Window;

import pl.wasat.smarthma.ui.frags.common.GlobalSettingsFragment;

import static android.app.PendingIntent.getActivity;

public class GlobalSettingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction().replace(android.R.id.content,
                new GlobalSettingsFragment()).commit();

}

}

