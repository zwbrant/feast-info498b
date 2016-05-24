package edu.uw.info498b.feast;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by Nick on 5/23/16.
 */
public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
