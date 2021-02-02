package com.example.tripreminder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.tripreminder.Fragments.HistoryFragment;
import com.example.tripreminder.Fragments.HomeFragment;
import com.example.tripreminder.RoomDataBase.TripTable;
import com.example.tripreminder.RoomDataBase.TripViewModel;
import com.example.tripreminder.database.DataHolder;
import com.example.tripreminder.database.UsersDao;
import com.example.tripreminder.databinding.ActivityBaseHomeBinding;
import com.example.tripreminder.model.User;
import com.example.tripreminder.serveses.FloatingViewService;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class BaseHomeActivity extends AppCompatActivity{
    private ActivityBaseHomeBinding activityBaseHomeBinding;
    private Fragment baseFragment;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private static final int SYSTEM_ALERT_WINDOW_PERMISSION = 2084;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_base_home);
        activityBaseHomeBinding = DataBindingUtil.setContentView(BaseHomeActivity.this, R.layout.activity_base_home);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        activityBaseHomeBinding.toolbar.setSubtitle("Upcoming");
        
//        activityBaseHomeBinding.toolbar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//                    startService(new Intent(BaseHomeActivity.this, FloatingViewService.class));
//                    finish();
//                } else if (Settings.canDrawOverlays(BaseHomeActivity.this)) {
//                    startService(new Intent(BaseHomeActivity.this, FloatingViewService.class));
//                    finish();
//                } else {
//                    askPermission();
//                    Toast.makeText(BaseHomeActivity.this, "You need System Alert Window Permission to do this", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//        });


//        activityBaseHomeBinding.addTrip.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // TODO: Adda the intent to go to add note activity
//                startActivity(new Intent(BaseHomeActivity.this,AddTripActivity.class));
//
//            }
//        });

        activityBaseHomeBinding.bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
    }


    private final BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {


            switch (menuItem.getItemId()) {
                case R.id.homeItem_menu:
                    baseFragment = new HomeFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentBasic, baseFragment).commit();
                    activityBaseHomeBinding.toolbar.setSubtitle("Upcoming");
                    break;

                case R.id.profileItem_menu:
                    baseFragment = new ProfileFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentBasic, baseFragment).commit();
                    activityBaseHomeBinding.toolbar.setSubtitle("Profile");
                    break;

                case R.id.historyItem_menu:
                    baseFragment = new HistoryFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentBasic, baseFragment).commit();
                    activityBaseHomeBinding.toolbar.setSubtitle("History");
                    break;

            }
            return true;
        }
    };

    @Override
    protected void onStart() {
        super.onStart();

        if (currentUser == null) {
            sendToLoginActivity();
        } else {
            UsersDao.getUser(mAuth.getUid(), new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User databaseUser = dataSnapshot.getValue(User.class);
                    DataHolder.dataBaseUser = databaseUser;
                    DataHolder.authUser = mAuth.getCurrentUser();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    }

    private void sendToLoginActivity() {
        Intent intent = new Intent(BaseHomeActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();

    }

    private void askPermission() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + getPackageName()));
        startActivityForResult(intent, SYSTEM_ALERT_WINDOW_PERMISSION);
    }

}