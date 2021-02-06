package com.example.tripreminder;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
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
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.room.Database;

import com.example.tripreminder.RoomDataBase.TripTable;
import com.example.tripreminder.RoomDataBase.TripViewModel;
import com.example.tripreminder.database.DataBase;
import com.example.tripreminder.database.DataHolder;
import com.example.tripreminder.database.UsersDao;
import com.example.tripreminder.model.Trip;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import static android.content.Context.MODE_PRIVATE;

public class ProfileFragment extends Fragment {


    ImageView profile_image;
    TextView fullNameTxt;
    TextView emailTxt;
    TextView logoutBtn;
    Button syncBtn;
    List<TripTable> tables;
    CardView cardViewReport;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private TripTable tripTable;
    private TripViewModel tripViewModel;
    Handler handler;
    Thread th;
    Sync sync;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO: get profile date from shared preference
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_profile, container, false);
        fullNameTxt = view.findViewById(R.id.name);
        emailTxt = view.findViewById(R.id.email);
        logoutBtn =  view.findViewById(R.id.logout_tve);
        syncBtn = view.findViewById(R.id.sync_btn);
        cardViewReport=view.findViewById(R.id.Report);
        mAuth = FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser();
        tables=new ArrayList<>();
        tripViewModel= new ViewModelProvider(getActivity(),
                ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication())).get(TripViewModel.class);

        return view;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

       super.onViewCreated(view, savedInstanceState);
                tables=saveFromRoomToFirebase();
        loadDataInSharedPerefrence(getContext());
        sync=new Sync();
        handler=new Handler(){

            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
            }
        };

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logOut();
            }
        });

        syncBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                th=new Thread(sync);
                th.start();
            }
        });

        cardViewReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(getActivity(),DistanceReport.class);
                startActivity(intent);

            }
        });


    }

    public void loadDataInSharedPerefrence(Context context){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        fullNameTxt.setText(preferences.getString("Name","null"));
        emailTxt.setText(preferences.getString("Email","null"));

    }

    public void logOut(){
        tripViewModel.deleteAllTrips();
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

    public void sync(List<TripTable> trips){

        //List<TripTable> trips= tripViewModel.getAllTrips().getValue();

        Log.i("sync",""+trips);
        String currentUserId=currentUser.getUid();
        //DataBase.getUsers().child(currentUserId).child(DataBase.USER_Trip_REF).setValue("");
        UsersDao.addUserTrips(trips, currentUserId, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(getActivity(), R.string.Yourdataisbackedup, Toast.LENGTH_LONG).show();


                }else {
                    String message = task.getException().getLocalizedMessage();
                    Toast.makeText(getActivity(), ""+R.string.error + message, Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    class Sync implements Runnable{

        @Override
        public void run() {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
            sync(tables);
            handler.sendEmptyMessage(0);
        }

    }


    private List<TripTable> saveFromRoomToFirebase() {

        tripViewModel.getGetAllAsync().observe(getActivity(), new Observer<List<TripTable>>() {
            @Override
            public void onChanged(List<TripTable> tripTables) {
                tables = tripTables;
            }
        });
        return tables;
    }

}