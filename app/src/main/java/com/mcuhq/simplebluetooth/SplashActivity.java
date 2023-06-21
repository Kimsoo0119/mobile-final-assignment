package com.mcuhq.simplebluetooth;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {
    private static final int BACKGROUND_COLOR_DELAY = 1000;
    private static final int FINISH_DELAY = 3000;
    private ImageView lightImageView;
    private TextView studentCodeTextView;

    private TextView nameTextView;
    private TextView titleTextView;
    private LinearLayout splash;
    private Integer lightOnImage;
    private ConnectedThread mConnectedThread; // bluetooth background worker thread to send and receive data


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);
        lightImageView = (ImageView) findViewById(R.id.light);
        studentCodeTextView = (TextView) findViewById(R.id.studentCode);
        nameTextView = (TextView) findViewById(R.id.nameText);
        titleTextView = (TextView) findViewById(R.id.title);
        lightOnImage = R.drawable.light;

        studentCodeTextView.setVisibility(View.GONE);
        nameTextView.setVisibility(View.GONE);
        titleTextView.setVisibility(View.GONE);

        Handler handler = new Handler();
        handler.postDelayed(() -> changeBackgroundColor(), BACKGROUND_COLOR_DELAY);
        handler.postDelayed(() -> startMainActivity(), FINISH_DELAY);

    }

    // 액티비티 종료
    private void changeBackgroundColor() {
        splash = (LinearLayout) findViewById(R.id.splash);
        splash.setBackgroundColor(ContextCompat.getColor(this, android.R.color.white));
        lightImageView.setImageResource(lightOnImage);
        studentCodeTextView.setVisibility(View.VISIBLE);
        nameTextView.setVisibility(View.VISIBLE);
        titleTextView.setVisibility(View.VISIBLE);
    }

    // MainActivity 실행
    private void startMainActivity() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish(); // SplashActivity 종료
    }
}
