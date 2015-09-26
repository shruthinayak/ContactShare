package com.contactshare.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.contactshare.R;
import com.contactshare.utilities.Constants;
import com.contactshare.utilities.Utilities;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";
    private Context ctx;
    private TextView txtConDetailHead;
    private EditText edtConName;
    private EditText edtConNumber;
    private EditText edtConEmail;
    private Button btnConSave;
    private ImageView imgConQrCode;
    private ImageView imgQrCode;
    private ImageButton btnConEdit;
    private ImageButton btnConScan;
    private View lytContactDetail;
    private View lytQrCode;


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
            String contact = "Name:" + sharedName + "No:" + sharedNumber + "Email:" + sharedEmail;
            imgQrCode.setImageBitmap(Utilities.encodeToQrCode(contact, 500, 500));
        }
    }

    private void setOnClickListeners() {
        btnConSave.setOnClickListener(this);
        btnConEdit.setOnClickListener(this);
        btnConScan.setOnClickListener(this);
    }

    private void initUI() {
        lytContactDetail = findViewById(R.id.lyt_contact_detail);
        lytQrCode = findViewById(R.id.lyt_qr_code);
        txtConDetailHead = (TextView) findViewById(R.id.txt_con_head);
        edtConName = (EditText) findViewById(R.id.edt_con_name);
        edtConNumber = (EditText) findViewById(R.id.edt_con_number);
        edtConEmail = (EditText) findViewById(R.id.edt_con_email);
        btnConSave = (Button) findViewById(R.id.btn_con_save);
        imgConQrCode = (ImageView) findViewById(R.id.img_con_qr_code);
        btnConEdit = (ImageButton) findViewById(R.id.btn_con_edit);
        btnConScan = (ImageButton) findViewById(R.id.btn_con_scan);
        //QR code on display mode
        imgQrCode = (ImageView) findViewById(R.id.img_qr_code);
        lytQrCode.setVisibility(View.GONE);

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //IntentResult res = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if (requestCode == 0) {
            //if (resultCode == RESULT_OK) {
            //get the extras that are returned from the intent
            String contents = intent.getStringExtra("SCAN_RESULT");
            String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
            Toast toast = Toast.makeText(this, "Content:" + contents + " Format:" + format, Toast.LENGTH_LONG);
            toast.show();
        }
        //}
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_con_save:
                String name = edtConName.getText().toString();
                String number = edtConNumber.getText().toString();
                String email = edtConEmail.getText().toString();
                String contact = "Name:" + name + "No:" + number + "Email:" + email;
                imgConQrCode.setImageBitmap(Utilities.encodeToQrCode(contact, 500, 500));
                Utilities.putValueIntoSharedPref(ctx, Constants.KEY_NAME, name);
                Utilities.putValueIntoSharedPref(ctx, Constants.KEY_EMAIL, email);
                Utilities.putValueIntoSharedPref(ctx, Constants.KEY_NUMBER, number);
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
                //addContact(MainActivity.this);

        }
    }
}
