package com.contactshare.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.contactshare.R;
import com.contactshare.utilities.Utilities;

public class SplashScreenActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getActionBar().hide();

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Sabrinahandfont.ttf");
        TextView txtLauncherTitle = (TextView) findViewById(R.id.txtLauncherTitle);
        txtLauncherTitle.setTypeface(typeface);
        if(!Utilities.getValueForKeyFromPref(SplashScreenActivity.this, "login").equals("true")){
            Thread timerThread = new Thread() {
                public void run() {
                    try {
                        Utilities.putValueIntoSharedPref(SplashScreenActivity.this, "login", "true");
                        sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            };
            timerThread.start();
        } else {
            Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
