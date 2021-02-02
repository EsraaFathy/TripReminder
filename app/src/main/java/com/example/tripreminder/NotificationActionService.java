package com.example.tripreminder;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Locale;
import java.util.Random;

public class NotificationActionService extends Service {


    private Intent intent;
    private static String URL= "http://maps.google.com/maps?daddr=";
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
        this.intent = intent;
        MyReciever.rigntone.stop();
        switch (intent.getAction()){
            case MyReciever.START_SERVICE:
                Toast.makeText(getApplicationContext(), "start", Toast.LENGTH_SHORT).show();

                    String my_data= String.format(Locale.ENGLISH, "http://maps.google.com/maps?daddr=30.6178118,32.2761602(الممر)");
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(my_data));
                    mapIntent.setPackage("com.google.android.apps.maps");
                    startActivity(intent);

                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                    Log.i("log", "onPackageManager");
                }

                MyReciever.notificationManager.cancelAll();
                break;
            case MyReciever.SNOOZE_SERVICE:
                Toast.makeText(getApplicationContext(), "Snooze", Toast.LENGTH_SHORT).show();

                break;
            case MyReciever.CANCEL_SERVICE:
                Toast.makeText(getApplicationContext(), "Cancel", Toast.LENGTH_SHORT).show();
                MyReciever.notificationManager.cancelAll();
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
