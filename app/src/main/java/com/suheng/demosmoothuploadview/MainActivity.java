package com.suheng.demosmoothuploadview;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.suheng.widget.SmoothUploadView;

import java.lang.ref.WeakReference;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private static final int TIME_UPDATE_PROGRESS = 100;
    private static final int MSG_UPDATE_PROGRESS = 1;
    private SmoothUploadView mSmoothUploadView;
    private WeakHandler mHandler = new WeakHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_view_aty);

        mSmoothUploadView = findViewById(R.id.upload_handler_view);
        mSmoothUploadView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.share_pic));
    }

    private void updateProgress(double percent) {
        if (percent >= 1.0) {
            percent = 1.0;
            mHandler.removeMessages(MSG_UPDATE_PROGRESS);
        } else {
            Message msg = new Message();
            msg.what = MSG_UPDATE_PROGRESS;
            msg.obj = (percent + 0.17);
            mHandler.sendMessageDelayed(msg, TIME_UPDATE_PROGRESS);
        }

        mSmoothUploadView.updateProgress(percent);
    }

    public void onClickUploadHandler(View view) {
        mSmoothUploadView.resetProgress();

        mHandler.removeMessages(MSG_UPDATE_PROGRESS);
        Message msg = new Message();
        msg.what = MSG_UPDATE_PROGRESS;
        msg.obj = 0.11;
        mHandler.sendMessageDelayed(msg, TIME_UPDATE_PROGRESS);
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

        //startActivity(new Intent(this, SecondActivity.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler.hasMessages(MSG_UPDATE_PROGRESS)) {
            mHandler.removeMessages(MSG_UPDATE_PROGRESS);
        }
    }

    private static class WeakHandler extends Handler {
        private WeakReference<MainActivity> mWeakReference;

        private WeakHandler(MainActivity instance) {
            mWeakReference = new WeakReference<>(instance);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            MainActivity instance = mWeakReference.get();
            if (instance == null) {
                return;
            }

            if (msg.what == MSG_UPDATE_PROGRESS) {
                instance.updateProgress((Double) msg.obj);
            }
        }
    }
}
