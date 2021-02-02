package com.example.tripreminder;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.tripreminder.serveses.FloatingViewService;

import java.util.Locale;
import java.util.Random;

import static androidx.core.app.ActivityCompat.startActivityForResult;

public class NotificationActionService extends Service {


    private Intent intent;

    private String SOURCE_URL= "http://maps.google.com/maps?saddr=";
    private String DEST_URL= "http://maps.google.com/maps?daddr=";
    public NotificationActionService(){
//        super("NotificationActionService");

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        MyReciever.rigntone.stop();
        switch (intent.getAction()){
            case MyReciever.START_SERVICE:
                    Toast.makeText(getApplicationContext(), "start", Toast.LENGTH_SHORT).show();
                    double sourceLat,sourceLon,destinationLat,destinationLon;
                    String sourceName,destinationName;

                    destinationLat = intent.getDoubleExtra("destinationLat", 0);
                    destinationLon = intent.getDoubleExtra("destinationLon", 0);
                    destinationName = intent.getStringExtra("destinationName");
                    Intent mapIntent;
                    String my_data;

                    if(!intent.getExtras().getString("sourceName", "null").equals("null")){ // source exists
                        Log.i("log", "not null notif");
                        sourceLat = intent.getDoubleExtra("sourceLat",0);
                        sourceLon = intent.getDoubleExtra("sourceLon",0);
                        sourceName = intent.getStringExtra("sourceName");


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

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                    startService(new Intent(getApplicationContext(), FloatingViewService.class));
//                    finish();
                    }else if (Settings.canDrawOverlays(this)) {
                    startService(new Intent(getApplicationContext(), FloatingViewService.class));
//                    finish();
                }else {
//                    askPermission();
                    Toast.makeText(this, "You need System Alert Window Permission to do this", Toast.LENGTH_SHORT).show();
                }
                startActivity(mapIntent);

                MyReciever.notificationManager.cancelAll();
                MyReciever.rigntone.stop();
                    //Todo: delete this trip
                break;
            case MyReciever.SNOOZE_SERVICE:
                Toast.makeText(getApplicationContext(), "Snooze", Toast.LENGTH_SHORT).show();

                break;
            case MyReciever.CANCEL_SERVICE:
                Toast.makeText(getApplicationContext(), "Cancel", Toast.LENGTH_SHORT).show();
                MyReciever.notificationManager.cancelAll();
                MyReciever.rigntone.stop();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("log", "onDestroy");
        stopSelf();
    }

    public NotificationActionService(String name) {
//        super(name);
        System.out.println("onHandleWork");
    }

//
//    @Override
//    protected void onHandleIntent(@Nullable Intent intent) {
////        createNotification(getApplicationContext());
//
//    }

}
