package com.example.tripreminder;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.tripreminder.database.DataHolder;
import com.google.firebase.auth.FirebaseAuth;

import static android.content.Context.MODE_PRIVATE;

public class ProfileFragment extends Fragment {


    ImageView profile_image;
    TextView fullNameTxt;
    TextView emailTxt;
    TextView logoutBtn;
    Button syncBtn;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO: get profile date from shared preference
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

       super.onViewCreated(view, savedInstanceState);
        fullNameTxt = view.findViewById(R.id.name);
        emailTxt = view.findViewById(R.id.email);
        logoutBtn =  view.findViewById(R.id.logout_tve);
        syncBtn = view.findViewById(R.id.sync_btn);
        loadDataInSharedPerefrence();


    }

    public void loadDataInSharedPerefrence(){

        SharedPreferences set=getActivity().getSharedPreferences("Phone",MODE_PRIVATE);
        fullNameTxt.setText(set.getString("Name","null"));
        emailTxt.setText(set.getString("Email","null"));

    }

    public void logOut(){
        FirebaseAuth.getInstance().signOut();
        DataHolder.dataBaseUser=null;
        DataHolder.authUser=null;
        sendToLoginActivity();
    }

    private void sendToLoginActivity() {
        Intent intent= new Intent(getContext(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        getActivity().finish();

    }

    public void sync(){

    }


}