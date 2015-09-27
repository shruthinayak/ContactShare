package com.contactshare.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.HashMap;

/**
 * Created by SG0222540 on 9/24/2015.
 */
public class Utilities {

    private static SharedPreferences sharedPref;
    public static Bitmap encodeToQrCode(String text) {
        int width = Constants.QR_CODE_SIZE;
        int height = Constants.QR_CODE_SIZE;
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix matrix = null;
        try {
            matrix = writer.encode(text, BarcodeFormat.QR_CODE, width, height);
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

    public static boolean isInitialSetupDone(Context ctx){
        boolean isInitialSetup = false;
        sharedPref = ctx.getSharedPreferences(Constants.SHARED_PREF_NAME, 0);
        return !Constants.NULL_STRING.equals(sharedPref.getString(Constants.KEY_NAME, Constants.NULL_STRING));
    }

    public static String getValueForKeyFromPref(Context ctx, String key) {
        sharedPref = ctx.getSharedPreferences(Constants.SHARED_PREF_NAME, 0);
        return sharedPref.getString(key, Constants.NULL_STRING);
    }

    public static void putValueIntoSharedPref(Context ctx, String key, String value){
        sharedPref = ctx.getSharedPreferences(Constants.SHARED_PREF_NAME, 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.commit();
    }
    public static String prepareStringForQRCode(HashMap<String, String> fields){
        StringBuilder sb = new StringBuilder();
        for(String key: fields.keySet()){
            sb.append(key);
            sb.append(":");
            sb.append(fields.get(key));
        }
        return sb.toString();
    }
}
