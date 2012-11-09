package com.artigile.android.placesapi;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import com.google.inject.Inject;
import roboguice.activity.RoboActivity;

/**
 * @author IoaN, 7/3/11 11:45 AM
 */
public class ApplicationConfiguration extends RoboActivity {

    @Inject
    private SharedPreferences sharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.configuration);
        /*    try {
            HttpRequest request = new NetHttpTransport().createRequestFactory().buildPostRequest(new GenericUrl(), new EmptyContent());
            request.getContent();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void saveSettings(View view) {

        saveConfiguration();
        finish();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        saveConfiguration();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    private void saveConfiguration() {
        SharedPreferences.Editor editPref = sharedPrefs.edit();
//        editPref.putInt(SCARY_LEVEL_PROPERTY_NAME, mazeDotState.getScaryLevel());
        editPref.commit();
    }
}
