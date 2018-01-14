package com.hatchers.ruralcaravane.scaner;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PointF;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;


import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.hatchers.ruralcaravane.R;

public class AdharScanner extends AppCompatActivity implements QRCodeReaderView.OnQRCodeReadListener {

    private QRCodeReaderView qrCodeReaderView;
    private int RETURN_ID =1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adhar_scanner);

        qrCodeReaderView = (QRCodeReaderView) findViewById(R.id.qrdecoderview);
        qrCodeReaderView.setOnQRCodeReadListener(this);

        // Use this function to enable/disable decoding
        qrCodeReaderView.setQRDecodingEnabled(true);

        // Use this function to change the autofocus interval (default is 5 secs)
        qrCodeReaderView.setAutofocusInterval(2000L);

        // Use this function to enable/disable Torch
        qrCodeReaderView.setTorchEnabled(true);

        // Use this function to set front camera preview
        qrCodeReaderView.setFrontCamera();

        // Use this function to set back camera preview
        qrCodeReaderView.setBackCamera();
    }

    // Called when a QR is decoded
    // "text" : the text encoded in QR
    // "points" : points where QR control points are placed in View
    @Override
    public void onQRCodeRead(String text, PointF[] points) {
       // resultTextView.setText(text);
       // Toast.makeText(this,text,Toast.LENGTH_SHORT).show();
        Log.d("Data",text);
        Intent intent=new Intent();
        intent.putExtra("MESSAGE",text);
        setResult(Activity.RESULT_OK,intent);
        finish();//finishing activity
    }

    @Override
    protected void onResume() {
        super.onResume();
        qrCodeReaderView.startCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        qrCodeReaderView.stopCamera();
    }

    @Override
    public void onBackPressed() {
               super.onBackPressed();
        Intent intent=new Intent();
        setResult(Activity.RESULT_OK,intent);
        finish();
    }
}
