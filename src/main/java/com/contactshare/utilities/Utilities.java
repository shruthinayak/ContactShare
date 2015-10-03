package com.contactshare.utilities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.widget.Toast;

import com.contactshare.R;
import com.contactshare.models.VCard;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import static android.provider.ContactsContract.Contacts;
import static android.provider.ContactsContract.Intents;

public class Utilities {

    private static SharedPreferences sharedPref;

    public static Bitmap encodeToQrCode(Context ctx, String text) {
        int width = Constants.QR_CODE_SIZE;
        int height = Constants.QR_CODE_SIZE;
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix matrix = null;
        try {
            matrix = writer.encode(text, BarcodeFormat.QR_CODE, width, height);
        } catch (WriterException ex) {
            ex.printStackTrace();
        }
        int colorId = ctx.getResources().getColor(R.color.ColorLaceWhite);
        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                bmp.setPixel(x, y, matrix.get(x, y) ? Color.BLACK : colorId);
            }
        }
        return bmp;
    }

    public static boolean isInitialSetupDone(Context ctx) {
        boolean isInitialSetup = false;
        sharedPref = ctx.getSharedPreferences(Constants.SHARED_PREF_NAME, 0);
        return !Constants.NULL_STRING.equals(sharedPref.getString(Constants.KEY_NAME, Constants.NULL_STRING));
    }

    public static String getValueForKeyFromPref(Context ctx, String key) {
        sharedPref = ctx.getSharedPreferences(Constants.SHARED_PREF_NAME, 0);
        return sharedPref.getString(key, Constants.NULL_STRING);
    }

    public static void putValueIntoSharedPref(Context ctx, String key, String value) {
        sharedPref = ctx.getSharedPreferences(Constants.SHARED_PREF_NAME, 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void addToContact(Context ctx, VCard scannedContact) {
        if(!doesContactAlreadyExist(ctx, scannedContact)){
            Intent intent1 = new Intent(Intent.ACTION_INSERT,
                    Contacts.CONTENT_URI);
            intent1.putExtra(Intents.Insert.NAME, scannedContact.getName());
            intent1.putExtra(Intents.Insert.PHONE, scannedContact.getNumber());
            intent1.putExtra(Intents.Insert.EMAIL, scannedContact.getEmail());
            ctx.startActivity(intent1);
        } else{
            Toast.makeText(ctx, "Contact already exists in your phone!", Toast.LENGTH_LONG).show();
        }
    }

    public static boolean doesContactAlreadyExist(Context ctx, VCard information) {
        return false;
    }
}
