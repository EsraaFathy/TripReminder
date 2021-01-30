package com.example.tripreminder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.room.util.DBUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.tripreminder.Fragments.HistoryFragment;
import com.example.tripreminder.Fragments.HomeFragment;
import com.example.tripreminder.Fragments.ProfileFragment;
import com.example.tripreminder.databinding.ActivityBaseHomeBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BaseHomeActivity extends AppCompatActivity {
    ActivityBaseHomeBinding activityBaseHomeBinding;
    Fragment baseFragment;
//nsjbj
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBaseHomeBinding = DataBindingUtil.setContentView(this, R.layout.activity_base_home);
        baseFragment = new HomeFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentBasic, baseFragment).commit();

        activityBaseHomeBinding.addTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Adda the intent to go to add note activity
                startActivity(new Intent());
            }
        });

        activityBaseHomeBinding.bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
    }


    private final BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {


            switch (menuItem.getItemId()) {
                case R.id.homeItem_menu:
                    baseFragment = new HomeFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentBasic, baseFragment).commit();
                    break;

                case R.id.profileItem_menu:
                    baseFragment = new ProfileFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentBasic, baseFragment).commit();
                    break;

                case R.id.historyItem_menu:
                    baseFragment = new HistoryFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentBasic, baseFragment).commit();
                    break;

            }
            return true;
        }
    };
}