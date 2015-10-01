package com.contactshare.activities;

import android.app.ActionBar;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.contactshare.R;

public class HelpActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_activity_one);
        Typeface mainTypeFace = Typeface.createFromAsset(getAssets(), "fonts/Sabrinahandfont.ttf");

        ActionBar mActionBar = getActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);
        View mCustomView = mInflater.inflate(R.layout.activity_action_bar, null);
        mCustomView.findViewById(R.id.imgHelp).setVisibility(View.GONE);
        TextView txtAppName = (TextView) mCustomView.findViewById(R.id.txtAppName);
        txtAppName.setTypeface(mainTypeFace);
        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);
    }
}
