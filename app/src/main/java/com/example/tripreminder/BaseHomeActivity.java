package com.example.tripreminder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.room.util.DBUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.tripreminder.databinding.ActivityBaseHomeBinding;

public class BaseHomeActivity extends AppCompatActivity {
ActivityBaseHomeBinding activityBaseHomeBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBaseHomeBinding= DataBindingUtil.setContentView(this,R.layout.activity_base_home);
        activityBaseHomeBinding.addTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Adda the intent to go to add note activity
                startActivity(new Intent());
            }
        });
    }
}