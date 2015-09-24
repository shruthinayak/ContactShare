package com.contactshare.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.contactshare.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

/**
 * Created by SG0222540 on 9/12/2015.
 */
public class Scan extends Fragment{

    static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";
    private TextView conDetailHead;
    private EditText conName;
    private EditText conNumber;
    private EditText conEmail;
    private Button conSave;
    private ImageView conQR;
    private ImageView qrCode;
    private ImageButton conEdit;
    private ImageButton conScan;
    private SharedPreferences sharedPref;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.scan, container, false);

        conDetailHead = (TextView)v.findViewById(R.id.con_head);
        conName = (EditText) v.findViewById(R.id.con_name);
        conNumber = (EditText) v.findViewById(R.id.con_number);
        conEmail = (EditText) v.findViewById(R.id.con_email);
        conSave = (Button) v.findViewById(R.id.con_save);
        conQR = (ImageView) v.findViewById(R.id.con_qr_code);
        conEdit = (ImageButton) v.findViewById(R.id.btn_edit);
        conScan = (ImageButton) v.findViewById(R.id.btn_scan);
        //QR code on display mode
        qrCode = (ImageView) v.findViewById(R.id.qr_code);
        final View conDataLayout = v.findViewById(R.id.contact_layout);
        final View qrLayout = v.findViewById(R.id.qr_layout);
        qrLayout.setVisibility(View.GONE);
        sharedPref = getActivity().getSharedPreferences("pref",0);
        String sharedName = sharedPref.getString("Name", "Null");

        //Check if there is stored data
        if(sharedName!="Null"){
            conDataLayout.setVisibility(View.GONE);
            qrLayout.setVisibility(View.VISIBLE);
            String sharedNumber = sharedPref.getString("Number","Null");
            String sharedEmail = sharedPref.getString("Email","Null");
            String contact = "Name"+sharedName+"No:"+sharedNumber+"Email:"+sharedEmail;
            qrCode.setImageBitmap(encodeToQrCode(contact, 500, 500));
        }

        //Saving contact info
        conSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = conName.getText().toString();
                String number = conNumber.getText().toString();
                String email = conEmail.getText().toString();
                String contact = "Name"+name+"No:"+number+"Email:"+email;
                //Log.i("inf`o",contact);
                conQR.setImageBitmap(encodeToQrCode(contact, 500, 500));
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("Name",name);
                editor.putString("Number",number);
                editor.putString("Email",email);
                editor.commit();


            }
        });

        conEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qrLayout.setVisibility(View.GONE);
                conDataLayout.setVisibility(View.VISIBLE);
                conName.setText(sharedPref.getString("Name","Null"));
                conNumber.setText(sharedPref.getString("Number","Null"));
                conEmail.setText(sharedPref.getString("Email","Null"));
            }
        });
        /*Intent intent = new Intent(ACTION_SCAN);

        intent.putExtra("SCAN_MODE", "QR_CODE_MODE");

        startActivityForResult(intent, 0);*/

/*        IntentIntegrator integrator = new IntentIntegrator(getActivity());
        integrator.initiateScan();*/
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //IntentResult res = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if (requestCode == 0) {

            //if (resultCode == RESULT_OK) {

                //get the extras that are returned from the intent

                String contents = intent.getStringExtra("SCAN_RESULT");

                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");

                Toast toast = Toast.makeText(getActivity(), "Content:" + contents + " Format:" + format, Toast.LENGTH_LONG);

                toast.show();

            }

        //}

    }

    public static Bitmap encodeToQrCode(String text, int width, int height) {
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix matrix = null;
        try {
            matrix = writer.encode(text, BarcodeFormat.QR_CODE, 500, 500);
        } catch (WriterException ex) {
            ex.printStackTrace();
        }
        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                bmp.setPixel(x, y, matrix.get(x, y) ? Color.BLACK : Color.WHITE);
            }
        }
        return bmp;
    }
}
