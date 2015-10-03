package com.contactshare.activities;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
    private ProgressBar loadingIcon;


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
            loadingIcon.setVisibility(View.VISIBLE);
            String sharedName = Utilities.getValueForKeyFromPref(ctx, Constants.KEY_NAME);
            String sharedNumber = Utilities.getValueForKeyFromPref(ctx, Constants.KEY_NUMBER);
            String sharedEmail = Utilities.getValueForKeyFromPref(ctx, Constants.KEY_EMAIL);
            card = new VCard(sharedName, sharedNumber, sharedEmail);
            String contact = card.toString();
            new LoadQRAsyncTask().execute(contact);
        } else {
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

        ActionBar mActionBar = getActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);
        View mCustomView = mInflater.inflate(R.layout.activity_action_bar, null);
        mCustomView.findViewById(R.id.imgHelp).setOnClickListener(this);
        TextView txtAppName = (TextView) mCustomView.findViewById(R.id.txtAppName);
        txtAppName.setTypeface(mainTypeFace);
        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);

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
        loadingIcon = (ProgressBar) findViewById(R.id.loading_icon);
        loadingIcon.setVisibility(View.GONE);
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
            if (contents==null || !((contents.contains(Constants.KEY_NAME)) && (contents.contains(Constants.KEY_NUMBER))
                    && (contents.contains(Constants.KEY_EMAIL)))) {
                Toast.makeText(getApplicationContext(), "Please scan the right QR Code.",
                        Toast.LENGTH_LONG).show();
            } else {
                String fields[] = contents.split(";");
                String name = fields[0].split(":")[1];
                String number = fields[1].split(":")[1];
                String email = fields[2].split(":")[1];
                VCard scannedContact = new VCard(name, number, email);
                Utilities.addToContact(MainActivity.this, scannedContact);
            }
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
                if ((name.trim().length() < 2)) {
                    edtConName.setError("Enter name");
                } else if (number.trim().length() < 5) {
                    edtConNumber.setError("Enter number");
                } else if ((email.trim().length() < 2) || (!email.contains("@"))) {
                    edtConEmail.setError("Enter email");
                } else {
                    card = new VCard(name, number, email);
                    String contact = card.toString();
                    lytContactDetail.setVisibility(View.GONE);
                    lytQrCode.setVisibility(View.VISIBLE);
                    loadingIcon.setVisibility(View.VISIBLE);
                    imgQrCode.setVisibility(View.GONE);
                    new LoadQRAsyncTask().execute(contact);
                    Utilities.putValueIntoSharedPref(ctx, Constants.KEY_NAME, name);
                    Utilities.putValueIntoSharedPref(ctx, Constants.KEY_NUMBER, number);
                    Utilities.putValueIntoSharedPref(ctx, Constants.KEY_EMAIL, email);
                }
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
                break;
            case R.id.imgHelp:
                Intent helpIntent = new Intent(MainActivity.this, HelpActivity.class);
                startActivity(helpIntent);
                break;

        }
    }

    class LoadQRAsyncTask extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String[] params) {
            String contact = params[0];
            return Utilities.encodeToQrCode(MainActivity.this, contact);
        }

        @Override
        protected void onPostExecute(Bitmap image) {
            imgQrCode.setImageBitmap(image);
            loadingIcon.setVisibility(View.GONE);
            imgQrCode.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        if (!Utilities.isInitialSetupDone(ctx) && lytContactDetail.getVisibility() == View.VISIBLE) {
            finish();
        } else if (lytContactDetail.getVisibility() == View.VISIBLE) {
            lytContactDetail.setVisibility(View.GONE);
            lytQrCode.setVisibility(View.VISIBLE);
        } else finish();
    }
}
