package com.artigile.android.placesapi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import com.artigile.android.placesapi.R;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import roboguice.activity.RoboActivity;

@Singleton
public class GooglePlaces extends RoboActivity {

    @Inject
    private SharedPreferences sharedPrefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        restoreAppProperties();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    public void surfaceCreated(SurfaceHolder surfaceHolder) {

    }

    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_menu, menu);
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.adjustSensor:
                Intent intent = new Intent(this, ApplicationConfiguration.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade, R.anim.hold);
                return true;
            /*     case R.id.about:
           System.out.println("About!!!");
           return true;*/
            default:
                /*  // Don't toast text when a submenu is clicked
                if (!item.hasSubMenu()) {
                    Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT).show();
                    return true;
                }*/
                break;
        }
        return false;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    private void restoreAppProperties() {

    }

}

