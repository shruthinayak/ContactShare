package com.contactshare.activities;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.contactshare.R;
import com.contactshare.models.VCard;
import com.contactshare.utilities.Constants;
import com.contactshare.utilities.Utilities;

public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";
    private Context ctx;
    private EditText edtConName;
    private EditText edtConNumber;
    private EditText edtConEmail;
    private Button btnConGetQR;
    private ImageView imgQrCode;
    private ImageButton btnConEdit;
    private ImageButton btnConScan;
    private View lytContactDetail;
    private View lytQrCode;
    private VCard card;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ctx = getApplicationContext();
        initUI();
        setOnClickListeners();

        //Check if there is stored data
        if (Utilities.isInitialSetupDone(ctx)) {
            lytContactDetail.setVisibility(View.GONE);
            lytQrCode.setVisibility(View.VISIBLE);
            String sharedName = Utilities.getValueForKeyFromPref(ctx, Constants.KEY_NAME);
            String sharedNumber = Utilities.getValueForKeyFromPref(ctx, Constants.KEY_NUMBER);
            String sharedEmail = Utilities.getValueForKeyFromPref(ctx, Constants.KEY_EMAIL);
            card = new VCard(sharedName,sharedNumber,sharedEmail);
            String contact = card.toString();
            imgQrCode.setImageBitmap(Utilities.encodeToQrCode(MainActivity.this, contact));
        } else{
            lytContactDetail.setVisibility(View.VISIBLE);
            lytQrCode.setVisibility(View.GONE);
        }
    }

    private void setOnClickListeners() {
        btnConGetQR.setOnClickListener(this);
        btnConEdit.setOnClickListener(this);
        btnConScan.setOnClickListener(this);
    }

    private void initUI() {
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/GandhiSans-Regular.otf");
        Typeface mainTypeFace = Typeface.createFromAsset(getAssets(), "fonts/Sabrinahandfont.ttf");
        Window window = MainActivity.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(MainActivity.this.getResources().getColor(R.color.ColorLockGreenTranslucent));

        ActionBar actionBar = getActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.ColorLockGreen)));

        int titleId = getResources().getIdentifier("action_bar_title", "id",
                "android");
        TextView yourTextView = (TextView) findViewById(titleId);
        yourTextView.setTextSize(30);
        yourTextView.setTypeface(mainTypeFace);
        yourTextView.setText("Contact Share");

        lytContactDetail = findViewById(R.id.lyt_contact_detail);
        lytQrCode = findViewById(R.id.lyt_qr_code);
        edtConName = (EditText) findViewById(R.id.edt_con_name);
        edtConNumber = (EditText) findViewById(R.id.edt_con_number);
        edtConEmail = (EditText) findViewById(R.id.edt_con_email);
        btnConGetQR = (Button) findViewById(R.id.btn_con_get_qr);
        btnConEdit = (ImageButton) findViewById(R.id.btn_con_edit);
        btnConScan = (ImageButton) findViewById(R.id.btn_con_scan);
        //QR code on display mode
        imgQrCode = (ImageView) findViewById(R.id.img_qr_code);
        lytQrCode.setVisibility(View.GONE);
        btnConGetQR.setTypeface(typeface);
        edtConName.setTypeface(typeface);
        edtConNumber.setTypeface(typeface);
        edtConEmail.setTypeface(typeface);


    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            String contents = intent.getStringExtra("SCAN_RESULT");
            Utilities.addToContact(MainActivity.this,contents);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_con_get_qr:
                String name = edtConName.getText().toString();
                String number = edtConNumber.getText().toString();
                String email = edtConEmail.getText().toString();
                card = new VCard(name,number,email);
                String contact = card.toString();
                lytContactDetail.setVisibility(View.GONE);
                lytQrCode.setVisibility(View.VISIBLE);
                imgQrCode.setImageBitmap(Utilities.encodeToQrCode(MainActivity.this, contact));
                Utilities.putValueIntoSharedPref(ctx, Constants.KEY_NAME, name);
                Utilities.putValueIntoSharedPref(ctx, Constants.KEY_NUMBER, number);
                Utilities.putValueIntoSharedPref(ctx, Constants.KEY_EMAIL, email);
                break;
            case R.id.btn_con_edit:
                lytQrCode.setVisibility(View.GONE);
                lytContactDetail.setVisibility(View.VISIBLE);
                edtConName.setText(Utilities.getValueForKeyFromPref(ctx, Constants.KEY_NAME));
                edtConNumber.setText(Utilities.getValueForKeyFromPref(ctx, Constants.KEY_NUMBER));
                edtConEmail.setText(Utilities.getValueForKeyFromPref(ctx, Constants.KEY_EMAIL));
                break;
            case R.id.btn_con_scan:
                Intent intent = new Intent(ACTION_SCAN);
                intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
                startActivityForResult(intent, 0);

        }
    }
}
