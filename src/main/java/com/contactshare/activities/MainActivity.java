package com.contactshare.activities;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
    private static final int SELECT_IMAGE = 100;
    private static final int RESULT_CROP = 101;
    int count = 0;
    static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";
    private Context ctx;
    private EditText edtConName;
    private EditText edtConNumber;
    private EditText edtConEmail;
    private Button btnConSave;
    private ImageView imgQrCode;
    private ImageButton btnConEdit;
    private ImageButton btnConScan;
    private View lytContactDetail;
    private View lytQrCode;
    private ImageButton btnProfile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ctx = getApplicationContext();
        initUI();
        setOnClickListeners();

        if (Utilities.isInitialSetupDone(ctx)) {
            lytContactDetail.setVisibility(View.GONE);
            lytQrCode.setVisibility(View.VISIBLE);
            String sharedName = Utilities.getValueForKeyFromPref(ctx, Constants.KEY_NAME);
            String sharedNumber = Utilities.getValueForKeyFromPref(ctx, Constants.KEY_NUMBER);
            String sharedEmail = Utilities.getValueForKeyFromPref(ctx, Constants.KEY_EMAIL);
            String contact = "Name:" + sharedName + "No:" + sharedNumber + "Email:" + sharedEmail;
            imgQrCode.setImageBitmap(Utilities.encodeToQrCode(contact));
        } else {
            lytContactDetail.setVisibility(View.VISIBLE);
            lytQrCode.setVisibility(View.GONE);
        }
    }

    private void setOnClickListeners() {
        btnConSave.setOnClickListener(this);
        btnConEdit.setOnClickListener(this);
        btnConScan.setOnClickListener(this);
    }

    private void initUI() {
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Sabrinahandfont.ttf");
        Window window = MainActivity.this.getWindow();
        //window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(MainActivity.this.getResources().getColor(R.color.ColorLockGreen));

        ActionBar actionBar = getActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.ColorLockGreen)));

        int titleId = getResources().getIdentifier("action_bar_title", "id",
                "android");
        TextView yourTextView = (TextView) findViewById(titleId);
        yourTextView.setTextSize(30);
        yourTextView.setTypeface(typeface);
        yourTextView.setText("Contact Share");

        lytContactDetail = findViewById(R.id.lyt_contact_detail);
        lytQrCode = findViewById(R.id.lyt_qr_code);
        edtConName = (EditText) findViewById(R.id.edt_con_name);
        edtConNumber = (EditText) findViewById(R.id.edt_con_number);
        edtConEmail = (EditText) findViewById(R.id.edt_con_email);
        btnConSave = (Button) findViewById(R.id.btn_con_save);
        btnConEdit = (ImageButton) findViewById(R.id.btn_con_edit);
        btnConScan = (ImageButton) findViewById(R.id.btn_con_scan);
        btnProfile = (ImageButton) findViewById(R.id.btn_profile);
        //QR code on display mode
        imgQrCode = (ImageView) findViewById(R.id.img_qr_code);
        lytQrCode.setVisibility(View.GONE);

        btnConSave.setTypeface(typeface);
        edtConName.setTypeface(typeface);
        edtConNumber.setTypeface(typeface);
        edtConEmail.setTypeface(typeface);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 0:
                String contents = data.getStringExtra("SCAN_RESULT");
                String format = data.getStringExtra("SCAN_RESULT_FORMAT");
                Toast toast = Toast.makeText(this, "Content:" + contents + " Format:" + format, Toast.LENGTH_LONG);
                toast.show();
                Intent intent1 = new Intent(Intent.ACTION_INSERT,
                        ContactsContract.Contacts.CONTENT_URI);
                intent1.putExtra(ContactsContract.Intents.Insert.NAME, "NAMEEEE");
                intent1.putExtra(ContactsContract.Intents.Insert.PHONE, "7387387348");
                startActivity(intent1);
                break;
            case SELECT_IMAGE:
                try {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    Intent returnFromGalleryIntent = new Intent();
                    setResult(RESULT_CANCELED, returnFromGalleryIntent);
                    finish();
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (lytContactDetail.getVisibility() == View.VISIBLE && !Utilities.isInitialSetupDone(MainActivity.this)) {
            lytQrCode.setVisibility(View.GONE);
            finish();
        }
        if (lytContactDetail.getVisibility() == View.VISIBLE) {
            lytQrCode.setVisibility(View.VISIBLE);
            lytContactDetail.setVisibility(View.GONE);
        } else if (lytQrCode.getVisibility() == View.VISIBLE) {
            if (count == 1) {
                finish();
            } else {
                count++;
                Toast.makeText(MainActivity.this, "Pressing back again will exit the application.", Toast.LENGTH_LONG).show();
            }
        }
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
                lytContactDetail.setVisibility(View.GONE);
                lytQrCode.setVisibility(View.VISIBLE);
                imgQrCode.setImageBitmap(Utilities.encodeToQrCode(contact));
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
                break;

        }
    }
}
