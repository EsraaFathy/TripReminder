package com.example.tripreminder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
    String Name,Email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        InitiatizeFields();
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);

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
                //saveDataInSharedPerefrence();
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
            loadingBar.setCanceledOnTouchOutside(true);
            loadingBar.show();
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
                                DataHolder.dataBaseUser=databaseUser;
                                DataHolder.authUser=mAuth.getCurrentUser();
                                Toast.makeText(LoginActivity.this,""+ R.string.logged_is_successful, Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                                sendToMainActivity();

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                String message = databaseError.toException().getLocalizedMessage();
                                Toast.makeText(LoginActivity.this, ""+R.string.error + message, Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }
                        });

                    }
                    else {
                        String message = task.getException().toString();
                        Toast.makeText(LoginActivity.this, "Error: "+ message, Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
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
        Intent intent= new Intent(LoginActivity.this, MainActivity.class);
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
        loadingBar.setCanceledOnTouchOutside(true);
        loadingBar.show();
        String currentUserId = mAuth.getCurrentUser().getUid();
        UsersDao.getUser(currentUserId, new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Toast.makeText(LoginActivity.this,"In onDataChange ", Toast.LENGTH_SHORT).show();
                final User databaseUser=(User) dataSnapshot.getValue(User.class);
                Email=databaseUser.getEmail();
                Name=databaseUser.getName();
                //databaseUser=(User) dataSnapshot.getValue(User.class);
                DataHolder.dataBaseUser=databaseUser;
                DataHolder.authUser=mAuth.getCurrentUser();
                Toast.makeText(LoginActivity.this,""+databaseUser.toString(), Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
                saveDataInSharedPerefrence();
                sendToMainActivity();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                String message = databaseError.toException().getLocalizedMessage();
                Toast.makeText(LoginActivity.this, ""+R.string.error + message, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        });

    }

    public void saveDataInSharedPerefrence(){

        SharedPreferences set=getSharedPreferences("PersonalInfo",MODE_PRIVATE);
        SharedPreferences.Editor editor=set.edit();
        Log.i("log",Name+Email);
        editor.putString("Name",Name);
        editor.putString("Email",Email);
        editor.commit();

    }

    public static void googleLogOut(){
        googleSignInClient.signOut();
        Log.i("log","you logged out");
    }
}