package com.metrafonic.nativeminecraftquery;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by mathias on 6/10/13.
 */
public class AppSettingsHelper extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }

}
