package com.example.tripreminder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.room.util.DBUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.tripreminder.database.DataHolder;
import com.example.tripreminder.database.UsersDao;
import com.example.tripreminder.databinding.ActivityBaseHomeBinding;
import com.example.tripreminder.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class BaseHomeActivity extends AppCompatActivity {
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
ActivityBaseHomeBinding activityBaseHomeBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth=FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser();
        activityBaseHomeBinding= DataBindingUtil.setContentView(this,R.layout.fragment_profile);
        /*activityBaseHomeBinding.addTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Adda the intent to go to add note activity
                startActivity(new Intent());
            }
        });*/
    }

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