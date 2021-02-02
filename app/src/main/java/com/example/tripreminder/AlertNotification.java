package com.example.tripreminder;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class AlertNotification extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_notification);
        System.out.println("oncreate");

        findViewById(R.id.button).setOnClickListener(v -> {
            MyReciever.rigntone.stop();
        });
    }
}