package com.example.tripreminder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.tripreminder.database.DataHolder;
import com.example.tripreminder.database.UsersDao;
import com.example.tripreminder.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    Button logUot;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        logUot=findViewById(R.id.logout_btn);
        mAuth=FirebaseAuth.getInstance();
        //rootRef= FirebaseDatabase.getInstance().getReference();
        currentUser=mAuth.getCurrentUser();
        logUot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                DataHolder.dataBaseUser=null;
                DataHolder.authUser=null;
                sendToLoginActivity();

            }
        });

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
        Intent intent= new Intent(MainActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();

    }
}