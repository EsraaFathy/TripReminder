package com.example.tripreminder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private Button register;
    private EditText userEmail, userPassword, userName;
    private TextView alredyUser;
    private ImageView google;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;
    private DatabaseReference RootRef;
    private GoogleSignInClient googleSignInClient;
    public static String TAG="";
    private int RC_SIGN_IN = 1;
    private GoogleSignInAccount account;
    private String gPersonName ,gPersonGivenName ,gPersonFamilyName,gPersonEmail,gPersonId;
    private Uri gPersonPhoto;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth= FirebaseAuth.getInstance();
        RootRef= FirebaseDatabase.getInstance().getReference();
        InitializeField();
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);

        alredyUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendToLoginActivity();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateNewAccount();
            }
        });
        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
    }


    private void CreateNewAccount() {
        final String username=userName.getText().toString();
        final String email= userEmail.getText().toString();
        String password= userPassword.getText().toString();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
        }
        else {
            loadingBar.setTitle("Creating new account");
            loadingBar.setMessage("Please wait, while we are creating an account for you");
            loadingBar.setCanceledOnTouchOutside(true);
            loadingBar.show();
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        String currentUserID = mAuth.getCurrentUser().getUid();
                        //final User user=new User();
                        user=new User();
                        user.setId(task.getResult().getUser().getUid());
                        Log.e("error",""+user.getId());
                        user.setName(username);
                        user.setEmail(email);
                        UsersDao.addUser(user, currentUserID, new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    DataHolder.dataBaseUser=user;
                                    DataHolder.authUser=mAuth.getCurrentUser();
                                    Toast.makeText(RegisterActivity.this, ""+R.string.account_created_successfully, Toast.LENGTH_SHORT).show();
                                    loadingBar.dismiss();
                                    saveDataInSharedPerefrence(getApplicationContext());
                                    sendToMainActivity();
                                }else {
//if we have any error
                                    String message = task.getException().getLocalizedMessage();
                                    Toast.makeText(RegisterActivity.this, ""+R.string.error + message, Toast.LENGTH_SHORT).show();
                                    loadingBar.dismiss();
                                }

                            }
                        });
                    }
                    else {
                        String message = task.getException().toString();
                        Toast.makeText(RegisterActivity.this, "Error: "+ message, Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }

                }
            });

        }
    }

    private void sendToLoginActivity() {
        Intent intent= new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);

    }

    private void sendToMainActivity() {
        Intent intent= new Intent(RegisterActivity.this, BaseHomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void InitializeField() {
        register=(Button) findViewById(R.id.register_btn);
        userEmail=(EditText) findViewById(R.id.email_edt);
        userPassword=(EditText) findViewById(R.id.password_edt);
        userName = (EditText) findViewById(R.id.name_edt);
        alredyUser=(TextView) findViewById(R.id.hasAccount);
        google=findViewById(R.id.google);
        loadingBar= new ProgressDialog(this);
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
            Toast.makeText(RegisterActivity.this,"In Successfully",Toast.LENGTH_SHORT).show();
            GoogleSignInAccount acc = completedTask.getResult(ApiException.class);
            Toast.makeText(RegisterActivity.this,"Signed In Successfully",Toast.LENGTH_SHORT).show();
            FirebaseGoogleAuth(acc);
        }
        catch (ApiException e){
            Toast.makeText(RegisterActivity.this,"Sign In Failed"+e.getMessage(),Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(RegisterActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                        GoogleAccuntIntoFirebase();
                        saveDataInSharedPerefrence(getApplicationContext());
                    } else {
                        Toast.makeText(RegisterActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }
                }
            });
        }
        else{
            Toast.makeText(RegisterActivity.this, "acc failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateUI(FirebaseUser fUser){
         account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if(account !=  null){
            gPersonName = account.getDisplayName();
            gPersonGivenName = account.getGivenName();
            gPersonFamilyName = account.getFamilyName();
            gPersonEmail = account.getEmail();
            gPersonId = account.getId();
            gPersonPhoto = account.getPhotoUrl();
            Toast.makeText(RegisterActivity.this,gPersonName + gPersonEmail+gPersonId ,Toast.LENGTH_SHORT).show();
        }

    }

    private void GoogleAccuntIntoFirebase() {
        final String username=gPersonName;
        final String email= gPersonEmail;

        loadingBar.setTitle("Creating new account");
        loadingBar.setMessage("Please wait, while we are creating an account for you");
        loadingBar.setCanceledOnTouchOutside(true);
        loadingBar.show();
        String currentUserID = mAuth.getCurrentUser().getUid();
        //final User user=new User();
        user=new User();
        user.setId(currentUserID);
        Log.e("error",""+user.getId());
        user.setName(username);
        user.setEmail(email);
        UsersDao.addUser(user, currentUserID, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    DataHolder.dataBaseUser=user;
                    DataHolder.authUser=mAuth.getCurrentUser();
                    Toast.makeText(RegisterActivity.this, ""+R.string.account_created_successfully, Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    saveDataInSharedPerefrence(getApplicationContext());
                    sendToMainActivity();
                }else {
//if we have any error
                    String message = task.getException().getLocalizedMessage();
                    Toast.makeText(RegisterActivity.this, "#$%"+R.string.error + message, Toast.LENGTH_LONG).show();
                    loadingBar.dismiss();
                }

            }
        });



    }

    public void saveDataInSharedPerefrence(Context context){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString("Name",user.getName());
        editor.putString("Email",user.getEmail());
        editor.commit();

    }



    public static void googleLogOut(){
        //googleSignInClient.signOut();
        Log.i("log","you logged out");
    }


}