package com.example.tripreminder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.tripreminder.Fragments.HistoryFragment;
import com.example.tripreminder.Fragments.HomeFragment;
import com.example.tripreminder.Fragments.ProfileFragment;
import com.example.tripreminder.database.DataHolder;
import com.example.tripreminder.database.UsersDao;
import com.example.tripreminder.databinding.ActivityBaseHomeBinding;
import com.example.tripreminder.model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class BaseHomeActivity extends AppCompatActivity {
    ActivityBaseHomeBinding activityBaseHomeBinding;
    Fragment baseFragment;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBaseHomeBinding = DataBindingUtil.setContentView(this, R.layout.activity_base_home);
        baseFragment = new HomeFragment();
        mAuth=FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser();
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

    @Override
    protected void onStart() {
        super.onStart();

        if(currentUser==null){
            sendToLoginActivity();
        }

        else{
            UsersDao.getUser(mAuth.getUid(), new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User databaseUser=dataSnapshot.getValue(User.class);
                    DataHolder.dataBaseUser=databaseUser;
                    DataHolder.authUser=mAuth.getCurrentUser();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    }

    private void sendToLoginActivity() {
        Intent intent= new Intent(BaseHomeActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();

    }

}