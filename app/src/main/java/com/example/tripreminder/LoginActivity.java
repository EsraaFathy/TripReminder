package com.example.tripreminder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tripreminder.RoomDataBase.TripTable;
import com.example.tripreminder.RoomDataBase.TripViewModel;
import com.example.tripreminder.database.DataHolder;
import com.example.tripreminder.database.UsersDao;
import com.example.tripreminder.model.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private Button logen;
    private EditText userEmail,userPassword;
    private TextView newUser;
    private FirebaseAuth mAuth;
    private ImageView google;
    private ProgressDialog loadingBar;
    private static GoogleSignInClient googleSignInClient;
    public static String TAG="";
    private int RC_SIGN_IN = 1;
    private User databaseUser=new User();
    private TripViewModel tripViewModel;
    private List<TripTable> trips = new ArrayList<>();
    List<TripTable> tribsList;
    Sync sync;
    String Name,Email;
    Handler handler;
    Thread th;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // TODO: this to move to test my code
        //startActivity(new Intent(LoginActivity.this,BaseHomeActivity.class));
        mAuth = FirebaseAuth.getInstance();
        tripViewModel = new ViewModelProvider(LoginActivity.this, ViewModelProvider.AndroidViewModelFactory.getInstance(LoginActivity.this.getApplication())).get(TripViewModel.class);
        sync=new Sync();
        InitiatizeFields();
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);

        handler=new Handler(){

            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                LoginActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
            }
        };

        newUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendToRegisterActivity();
            }
        });

        logen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AllowUserToLogen();
            }
        });

        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

    }


    private void AllowUserToLogen() {
        String email= userEmail.getText().toString();
        String password= userPassword.getText().toString();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
        }
        else {
            loadingBar.setTitle("Signing in");
            loadingBar.setMessage("Please wait...");
            //loadingBar.setCanceledOnTouchOutside(true);
            loadingBar.show();
            LoginActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful())
                    {
                        String currentUserId = mAuth.getCurrentUser().getUid();
                        UsersDao.getUser(currentUserId, new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                //final User databaseUser=(User) dataSnapshot.getValue(User.class);
                                databaseUser=(User) dataSnapshot.getValue(User.class);
                                Email=databaseUser.getEmail();
                                Name=databaseUser.getName();
                                Log.i("log","after snapShot: "+Name+Email);
                                DataHolder.dataBaseUser=databaseUser;
                                DataHolder.authUser=mAuth.getCurrentUser();
                                Toast.makeText(LoginActivity.this,""+ R.string.logged_is_successful, Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                                saveDataInSharedPerefrence(getApplicationContext());
                                LoginActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                                th=new Thread(sync);
                                th.start();
                                sendToMainActivity();

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                String message = databaseError.toException().getLocalizedMessage();
                                Toast.makeText(LoginActivity.this, ""+R.string.error + message, Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                                LoginActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                            }
                        });

                    }
                    else {
                        String message = task.getException().toString();
                        Toast.makeText(LoginActivity.this, "Error: "+ message, Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                        LoginActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                    }
                }
            });
        }
    }

    private void sendToRegisterActivity() {
        Intent intent= new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    private void InitiatizeFields() {
        logen=(Button) findViewById(R.id.logen_btn);
        userEmail=(EditText) findViewById(R.id.email_edt);
        userPassword=(EditText) findViewById(R.id.password_edt);
        newUser=(TextView) findViewById(R.id.newUser_tve);
        google=findViewById(R.id.google);
        loadingBar= new ProgressDialog(this);

    }

    private void sendToMainActivity() {
        Intent intent= new Intent(LoginActivity.this, BaseHomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void signIn(){
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask){
        try{
            Toast.makeText(LoginActivity.this,"In Successfully",Toast.LENGTH_SHORT).show();
            GoogleSignInAccount acc = completedTask.getResult(ApiException.class);
            Toast.makeText(LoginActivity.this,"Signed In Successfully",Toast.LENGTH_SHORT).show();
            FirebaseGoogleAuth(acc);
        }
        catch (ApiException e){
            Toast.makeText(LoginActivity.this,"Sign In Failed"+e.getMessage(),Toast.LENGTH_SHORT).show();
            FirebaseGoogleAuth(null);
        }
    }

    private void FirebaseGoogleAuth(GoogleSignInAccount acct) {
        //check if the account is null
        if (acct != null) {
            AuthCredential authCredential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
            mAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                        AllowGoogleAccountToLogen();
                    } else {
                        Toast.makeText(LoginActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }
                }
            });
        }
        else{
            Toast.makeText(LoginActivity.this, "acc failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateUI(FirebaseUser fUser){
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if(account !=  null){
            String personName = account.getDisplayName();
            String personGivenName = account.getGivenName();
            String personFamilyName = account.getFamilyName();
            String personEmail = account.getEmail();
            String personId = account.getId();
            Uri personPhoto = account.getPhotoUrl();
            Toast.makeText(LoginActivity.this,personName + personEmail+personId ,Toast.LENGTH_SHORT).show();
        }

    }

    private void AllowGoogleAccountToLogen() {

        loadingBar.setTitle("Signing in");
        loadingBar.setMessage("Please wait...");
        //loadingBar.setCanceledOnTouchOutside(true);
        loadingBar.show();
        LoginActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        String currentUserId = mAuth.getCurrentUser().getUid();
        UsersDao.getUser(currentUserId, new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Toast.makeText(LoginActivity.this,"In onDataChange ", Toast.LENGTH_SHORT).show();
                final User databaseUser=(User) dataSnapshot.getValue(User.class);
                Toast.makeText(LoginActivity.this, ""+databaseUser, Toast.LENGTH_SHORT).show();
                if (databaseUser==null){
                    Toast.makeText(LoginActivity.this, "You are not registered please register", Toast.LENGTH_SHORT).show();
                }else {
                    Email = databaseUser.getEmail();
                    Name = databaseUser.getName();
                    //databaseUser=(User) dataSnapshot.getValue(User.class);
                    Log.i("log", "after snapShot: " + Name + Email);
                    DataHolder.dataBaseUser = databaseUser;
                    DataHolder.authUser = mAuth.getCurrentUser();
                    Toast.makeText(LoginActivity.this, "" + databaseUser.toString(), Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    saveDataInSharedPerefrence(getApplicationContext());
                    th=new Thread(sync);
                    th.start();
                    LoginActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                    sendToMainActivity();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                String message = databaseError.toException().getLocalizedMessage();
                Toast.makeText(LoginActivity.this, ""+R.string.error + message, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
                LoginActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
            }
        });

    }

    public void saveDataInSharedPerefrence(Context context){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor=preferences.edit();
        Log.i("log",Name+Email);
        editor.putString("Name",Name);
        editor.putString("Email",Email);
        editor.commit();

    }



    public void sync(){
        String currentUserId=mAuth.getCurrentUser().getUid();
        UsersDao.getUserTrips(currentUserId, new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tribsList=new ArrayList<>();
                for(DataSnapshot dataSnapshot1: snapshot.getChildren()){
                    String title= dataSnapshot1.child("title").getValue().toString();
                    String time= dataSnapshot1.child("time").getValue().toString();
                    String date= dataSnapshot1.child("date").getValue().toString();
                    String status= dataSnapshot1.child("status").getValue().toString();
                    String repetition=dataSnapshot1.child("repetition").getValue().toString();
                    String ways=dataSnapshot1.child("ways").getValue().toString();
                    String from=dataSnapshot1.child("from").getValue().toString();
                    String to=dataSnapshot1.child("to").getValue().toString();
                    String note=dataSnapshot1.child("notes").getValue().toString();
                    //Todo    we add her
                    String d=dataSnapshot1.child("distance").getValue().toString();
                    double distance=Double.valueOf(d);
                    String latStart=dataSnapshot1.child("latStart").getValue().toString();
                    double latStart1=Double.valueOf(latStart);
                    String longStart=dataSnapshot1.child("longStart").getValue().toString();
                    double longStart1=Double.valueOf(longStart);
                    String latEnd=dataSnapshot1.child("latEnd").getValue().toString();
                    double latEnd1=Double.valueOf(latEnd);
                    String longEnd=dataSnapshot1.child("longEnd").getValue().toString();
                    double longEnd1=Double.valueOf(longEnd);

                    boolean b=true;
                    if(ways.equals("true"))
                        b=true;
                    else
                        b=false;
                    TripTable trip=new TripTable(title,time,date,status,repetition,b,from,to,note,distance,latStart1,longStart1
                    ,latEnd1,longEnd1);
                    tribsList.add(trip);
                }
                saveFromFirebaseToRoom(tribsList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(LoginActivity.this, "Failed to get your data", Toast.LENGTH_SHORT).show();

            }
        });

    }

    class Sync implements Runnable{

        @Override
        public void run() {
            LoginActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
            sync();
            handler.sendEmptyMessage(0);
        }

    }

    private void saveFromFirebaseToRoom(List<TripTable> trips) {
        tripViewModel.deleteAllTrips();
        for (TripTable table : trips) {
          new Thread(new Runnable() {
              @Override
              public void run() {
                  tripViewModel.insert(table);
              }
          }).start();
        }
    }

}