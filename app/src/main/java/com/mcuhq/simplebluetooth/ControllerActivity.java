package com.mcuhq.simplebluetooth;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

public class ControllerActivity extends AppCompatActivity {
    private ImageView dialImage;
    private View touchView;
    private PointF center = new PointF();
    private double prevDegree = 0.0;
    private SwitchCompat lightSwitch;
    private TextView deviceIdText;
    private String bluetoothName;
    private ImageButton prevButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controller);

        dialImage = (ImageView) findViewById(R.id.dial_image);
        touchView =  findViewById(R.id.view_touch);
        lightSwitch = (SwitchCompat) findViewById(R.id.light_switch);
        bluetoothName = getIntent().getStringExtra("BLUETOOTH_NAME");
        prevButton = (ImageButton) findViewById(R.id.prev_button);
        deviceIdText = (TextView) findViewById(R.id.device_id_text);
        if(bluetoothName != null) deviceIdText.setText(bluetoothName);

        touchView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                center.x = touchView.getX() + touchView.getWidth() / 2;
                center.y = touchView.getY() + touchView.getHeight() / 2;

                dialImage.setX(center.x - dialImage.getWidth() / 2);
                dialImage.setY(center.y - dialImage.getHeight() / 2);

                touchView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        touchView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                double dX = event.getX() - center.x;
                double dY = event.getY() - center.y;

                double degree = Math.toDegrees(Math.atan2(dY, dX));

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        prevDegree = degree;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float currentRotation = dialImage.getRotation();
                        float newRotation = currentRotation + (float) (degree - prevDegree);
                        if (newRotation >= 360.0f) {    // 360 이상이면 0으로 설정
                            newRotation = 0.0f;
                        } else if (newRotation < 0.0f) {    // 0 미만이면 360으로 설정
                            newRotation = 360.0f + newRotation;
                        }
                        dialImage.setRotation(newRotation);
                        prevDegree = degree;
                        break;
                    case MotionEvent.ACTION_UP:
                        if (MainActivity.mConnectedThread != null) {
                            String rotationValue = String.valueOf(dialImage.getRotation());
                            MainActivity.mConnectedThread.write(rotationValue);
                            break;

                        }

                }
                return true;
            }
        });

        lightSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            LinearLayout controllerLayout = (LinearLayout) findViewById(R.id.controller_layout);
            if (isChecked) {
                controllerLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
                if (MainActivity.mConnectedThread != null) // First check to make sure thread created
                    MainActivity.mConnectedThread.write("ON");
            }else{
                controllerLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.gray));
                if (MainActivity.mConnectedThread != null) // First check to make sure thread created
                    MainActivity.mConnectedThread.write("OFF");
                }
        });

        prevButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });

    }
}