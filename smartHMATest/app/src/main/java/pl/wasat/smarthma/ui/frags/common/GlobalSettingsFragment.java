package pl.wasat.smarthma.ui.frags.common;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.MultiSelectListPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.RingtonePreference;
import android.view.ViewGroup;

import java.util.Set;

import pl.wasat.smarthma.R;

public class GlobalSettingsFragment extends PreferenceFragment implements
        SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    public void onResume() {
        super.onResume();

        // Set up a listener whenever a key changes
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);

        initSummary();
    }





    @Override
    public void onPause() {
        super.onPause();

        // Unregister the listener whenever a key changes
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                          String key) {
        // Update summary
        updatePrefsSummary(sharedPreferences, findPreference(key));
    }

    /*
 * Init summary
 */
    private void initSummary() {
        int pcsCount = getPreferenceScreen().getPreferenceCount();
        for (int i = 0; i < pcsCount; i++) {
            initPrefsSummary(getPreferenceManager().getSharedPreferences(),
                    getPreferenceScreen().getPreference(i));
        }
    }

    private void initPrefsSummary(SharedPreferences sharedPreferences,
                                  Preference p) {
        if (p instanceof PreferenceCategory) {
            PreferenceCategory pCat = (PreferenceCategory) p;
            int pcCatCount = pCat.getPreferenceCount();
            for (int i = 0; i < pcCatCount; i++) {
                initPrefsSummary(sharedPreferences, pCat.getPreference(i));
            }
        } else {
            updatePrefsSummary(sharedPreferences, p);
        }
    }

    /**
     * Update summary
     *
     * @param sharedPreferences SharedPreferences
     * @param pref              Preference
     */
    private void updatePrefsSummary(SharedPreferences sharedPreferences, Preference pref) {

        if (pref == null)
            return;

        if (pref instanceof ListPreference) {
            // List Preference
            ListPreference listPref = (ListPreference) pref;
            listPref.setSummary(listPref.getEntry());

        } else if (pref instanceof EditTextPreference) {
            // EditPreference
            EditTextPreference editTextPref = (EditTextPreference) pref;
            editTextPref.setSummary(editTextPref.getText());

        } else if (pref instanceof MultiSelectListPreference) {
            // MultiSelectList Preference
            MultiSelectListPreference mListPref = (MultiSelectListPreference) pref;
            String summaryMListPref = "";
            String and = "";

            // Retrieve values
            Set<String> values = mListPref.getValues();
            for (String value : values) {
                // For each value retrieve index
                int index = mListPref.findIndexOfValue(value);
                // Retrieve entry from index
                CharSequence mEntry = index >= 0
                        && mListPref.getEntries() != null ? mListPref
                        .getEntries()[index] : null;
                if (mEntry != null) {
                    // add summary
                    summaryMListPref = summaryMListPref + and + mEntry;
                    and = ";";
                }
            }
            // set summary
            mListPref.setSummary(summaryMListPref);

        } else if (pref instanceof RingtonePreference) {
            // RingtonePreference
            RingtonePreference rtPref = (RingtonePreference) pref;
            String uri;
            uri = sharedPreferences.getString(rtPref.getKey(), null);
            if (uri != null) {
                Ringtone ringtone = RingtoneManager.getRingtone(
                        getActivity(), Uri.parse(uri));

                pref.setSummary(ringtone.getTitle(getActivity()));
            }

        }
    }


}
