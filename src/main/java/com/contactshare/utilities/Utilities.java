package com.contactshare.utilities;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
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
        int colorId = ctx.getResources().getColor(R.color.ColorGhostWhite);
        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                bmp.setPixel(x, y, matrix.get(x, y) ? Color.BLACK : colorId);
            }
        }
        return bmp;
    }

    public static boolean isInitialSetupDone(Context ctx) {
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
        if (!doesContactAlreadyExist(ctx, scannedContact)) {
            Intent intent1 = new Intent(Intent.ACTION_INSERT,
                    Contacts.CONTENT_URI);
            intent1.putExtra(Intents.Insert.NAME, scannedContact.getName());
            intent1.putExtra(Intents.Insert.PHONE, scannedContact.getNumber());
            intent1.putExtra(Intents.Insert.EMAIL, scannedContact.getEmail());
            ctx.startActivity(intent1);
        } else {
            Toast.makeText(ctx, "Contact already exists in your phone!", Toast.LENGTH_LONG).show();
        }
    }

    public static VCard getUserContact(Context ctx) {
        final AccountManager manager = AccountManager.get(ctx);
        final Account[] accounts = manager.getAccountsByType("com.google");
        String name = "";
        String phone, email = "";
        if (accounts[0].name != null) {
            String accountName = accounts[0].name;

            ContentResolver cr = ctx.getContentResolver();
            Cursor emailCur = cr.query(
                    ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Email.DATA + " = ?",
                    new String[]{accountName}, null);
            while (emailCur.moveToNext()) {

                email = emailCur
                        .getString(emailCur
                                .getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                String newName = emailCur
                        .getString(emailCur
                                .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                if (name == null || newName.length() > name.length())
                    name = newName;

            }
            TelephonyManager tMgr = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
            phone = tMgr.getLine1Number();

            return new VCard(toTitleCase(name), phone, email);


        }
        return null;
    }

    public static String toTitleCase(String s) {
        final String ACTIONABLE_DELIMITERS = " '-/"; // these cause the character following
        // to be capitalized
        StringBuilder sb = new StringBuilder();
        boolean capNext = true;

        for (char c : s.toCharArray()) {
            c = (capNext)
                    ? Character.toUpperCase(c)
                    : Character.toLowerCase(c);
            sb.append(c);
            capNext = (ACTIONABLE_DELIMITERS.indexOf((int) c) >= 0); // explicit cast not needed
        }
        return sb.toString();
    }

    public static boolean doesContactAlreadyExist(Context ctx, VCard information) {
        return false;
    }
}
