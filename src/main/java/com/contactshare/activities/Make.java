package com.contactshare.activities;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.contactshare.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class Make extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.make, container, false);
        String contactDetails = "Name:Shruthi Nayak;Ph:14693533951;em:rn.shruthi@gmail.com";
        ImageView imgQR = (ImageView) v.findViewById(R.id.imgQR);
        imgQR.setImageBitmap(encodeToQrCode(contactDetails, 100, 100));
        return v;
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
