package com.example.tripreminder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tripreminder.databinding.ActivityAlertDialogBinding;
import com.example.tripreminder.serveses.FloatingViewService;

import java.util.Locale;
import java.util.Random;

public class AlertDialog extends AppCompatActivity {


    public static Ringtone rigntone;
    public static final String START_SERVICE = "com.example.tripreminder.StartService";
    public static final String SNOOZE_SERVICE = "com.example.tripreminder.SnoozeService";
    public static final String CANCEL_SERVICE = "com.example.tripreminder.CancelService";

    private String SOURCE_URL= "http://maps.google.com/maps?saddr=";
    private String DEST_URL= "http://maps.google.com/maps?daddr=";

    Intent myIntent;

    ActivityAlertDialogBinding binding;

    PendingIntent startPendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_dialog);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_alert_dialog);
        myIntent = getIntent();

        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
        r.play();

        Log.i("TAG", "onCreate alert dialog: "+getIntent().getIntExtra("ID", 0));

        binding.startBtn.setOnClickListener(v -> {
            finish();
            startTrip();

        });

        binding.laterBtn.setOnClickListener(v -> {

            createNotification(getApplicationContext());
            r.stop();
            finish();

        });

        binding.cancelBtn.setOnClickListener(v -> {
            finish();
            r.stop();
            //todo:: cancel trip in database
        });

    } // end on create

    private void createNotification(Context context){
        Intent tapNotification = new Intent(getApplicationContext(),AlertDialog.class).setAction(START_SERVICE);
        setTripDate(tapNotification);
        int id = myIntent.getIntExtra("ID",0);
        startPendingIntent = PendingIntent.getActivity(getApplicationContext(), id, tapNotification, PendingIntent.FLAG_ONE_SHOT);


        NotificationUtils notificationUtils = new NotificationUtils(context);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationCompat.Builder nb = notificationUtils.getAndroidChannelNotification(myIntent.getStringExtra("tripName"),startPendingIntent);
            notificationUtils.getManager().notify(id, nb.build());

        }

//        try{
//
//            Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
//            rigntone = RingtoneManager.getRingtone(context, uri);
//            if(!rigntone.isPlaying())
//                rigntone.play();
//        }
//        catch(Exception e){}


    }


    private void setTripDate(Intent pass){


        if((myIntent.hasExtra("sourceName"))){ // source exists
            Log.i("log", "not null myrec");
            pass.putExtra("sourceLat",myIntent.getDoubleExtra("sourceLat",0));
            pass.putExtra("sourceLon",myIntent.getDoubleExtra("sourceLon",0));
            pass.putExtra("sourceName",myIntent.getExtras().getString("sourceName","null"));
        }

        pass.putExtra("destinationLat", myIntent.getDoubleExtra("destinationLat",0));
        pass.putExtra("destinationLon",myIntent.getDoubleExtra("destinationLon",0));
        pass.putExtra("destinationName",myIntent.getExtras().getString("destinationName","null"));
        pass.putExtra("ID", myIntent.getIntExtra("ID", 0));
        pass.putExtra("tripName", myIntent.getStringExtra("tripName"));
        pass.putExtra("ways", myIntent.getBooleanExtra("ways",false));
    }

    private void startTrip(){

        Toast.makeText(getApplicationContext(), "start", Toast.LENGTH_SHORT).show();
        double sourceLat,sourceLon,destinationLat,destinationLon;
        String sourceName,destinationName;

        destinationLat = myIntent.getDoubleExtra("destinationLat", 0);
        destinationLon = myIntent.getDoubleExtra("destinationLon", 0);
        destinationName = myIntent.getStringExtra("destinationName");
        Intent mapIntent;
        String my_data;

        if(!myIntent.getExtras().getString("sourceName", "null").equals("null")){ // source exists
            Log.i("log", "not null notif");
            sourceLat = myIntent.getDoubleExtra("sourceLat",0);
            sourceLon = myIntent.getDoubleExtra("sourceLon",0);
            sourceName = myIntent.getStringExtra("sourceName");


            my_data= String.format(Locale.ENGLISH, SOURCE_URL+sourceLat+","+sourceLon+"("+sourceName+")&daddr="+destinationLat+","+destinationLon+"("+destinationName+")");
            mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(my_data));
            mapIntent.setPackage("com.google.android.apps.maps");
            mapIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }else{
            my_data= String.format(Locale.ENGLISH, DEST_URL+destinationLat+","+destinationLon+"("+destinationName+")");
            mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(my_data));
            mapIntent.setPackage("com.google.android.apps.maps");
            mapIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            System.out.println("null source");
        }

        startFloatingIcon();
        startActivity(mapIntent);

        if(myIntent.hasExtra("notificationID")){
            Log.i("log", "cancel notification ::::"+myIntent.getIntExtra("notificationID", 0)+"");
//            notificationManager.cancel(myIntent.getIntExtra("notificationID", 0));
//            MyReciever.rigntone.stop();
        }else{

        }

        //Todo: delete this trip
    }

    private void startFloatingIcon(){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            startService(new Intent(getApplicationContext(), FloatingViewService.class));
        }else if (Settings.canDrawOverlays(this)) {
            startService(new Intent(getApplicationContext(), FloatingViewService.class));
        }else {
            Toast.makeText(this, "You need System Alert Window Permission to do this", Toast.LENGTH_SHORT).show();
        }
    }

}