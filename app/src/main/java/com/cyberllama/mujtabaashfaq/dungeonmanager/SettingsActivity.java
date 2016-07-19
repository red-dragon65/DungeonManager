package com.cyberllama.mujtabaashfaq.dungeonmanager;


import android.support.v7.app.AppCompatActivity;
import android.preference.PreferenceFragment;
import android.os.Bundle;


//Class that creates the preferences layout activitiy.
public class SettingsActivity extends AppCompatActivity {

    /**
     * Gen
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new PrefFragment())
                .commit();
    }


    /**
     * Fragment to pe displayed.
     */
    public static class PrefFragment extends PreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.preferences);

        }
    }
}