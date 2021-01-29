package com.example.tripreminder;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class BaseHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_home);
    }
}