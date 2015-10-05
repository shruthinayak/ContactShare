package com.contactshare.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

import com.contactshare.R;
import com.contactshare.utilities.Constants;
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
        if (!Utilities.getValueForKeyFromPref(SplashScreenActivity.this, Constants.KEY_LOGIN).equals("true")) {
            Thread timerThread = new Thread() {
                public void run() {
                    try {
                        Utilities.putValueIntoSharedPref(SplashScreenActivity.this, Constants.KEY_LOGIN, "true");
                        sleep(3000);
                    } catch (InterruptedException e) {

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

}
