package com.suheng.widget.demo;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.suheng.widget.SmoothUploadView;

import java.util.Random;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_aty);
    }

    public void onClickUpload(View view) {
        final SmoothUploadView smoothUploadView = findViewById(R.id.smooth_upload_view);
        int temp = new Random().nextInt(3);
        if (temp == 0) {
            smoothUploadView.setMaxProgress(260);
            smoothUploadView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.chat_picture));
            smoothUploadView.updateProgress(260);
        } else if (temp == 1) {
            smoothUploadView.setImageResource(R.drawable.share_pic);
            smoothUploadView.updateProgress(1.0);
        } else {
            smoothUploadView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.zsj15hgvj30sg15hkbw));
            smoothUploadView.updateProgress(0.7);
        }
    }
}
