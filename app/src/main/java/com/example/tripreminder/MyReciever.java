package com.example.tripreminder;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Random;

public class MyReciever extends BroadcastReceiver {

    public static Ringtone rigntone;
    public static final String START_SERVICE = "com.example.tripreminder.StartService";
    public static final String SNOOZE_SERVICE = "com.example.tripreminder.SnoozeService";
    public static final String CANCEL_SERVICE = "com.example.tripreminder.CancelService";

    Intent myIntent;
    public static NotificationManagerCompat notificationManager;
    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("OnRecieve");
        this.myIntent = intent;

        createNotification(context);

        try{

            Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            rigntone = RingtoneManager.getRingtone(context, uri);
            if(!rigntone.isPlaying())
                rigntone.play();
        }
        catch(Exception e){}
    }

    private void createNotification(Context context){

        Intent startInten = new Intent(context,NotificationActionService.class).setAction(START_SERVICE);
        setTripDate(startInten);
        PendingIntent startPendingIntent = PendingIntent.getService(context, 0, startInten, PendingIntent.FLAG_ONE_SHOT);


        Intent snoozeIntent = new Intent(context,NotificationActionService.class).setAction(SNOOZE_SERVICE);
        setTripDate(snoozeIntent);
        PendingIntent snoozePendingIntent = PendingIntent.getService(context, 0, snoozeIntent, PendingIntent.FLAG_ONE_SHOT);


        Intent cancelIntent = new Intent(context,NotificationActionService.class).setAction(CANCEL_SERVICE);
        setTripDate(cancelIntent);
        PendingIntent cancelPendingIntent = PendingIntent.getService(context, 0, cancelIntent, PendingIntent.FLAG_ONE_SHOT);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "notification")
                .setSmallIcon(R.drawable.ic_baseline_add_alert_24)
                .setContentTitle("Remind me a trip")
                .setContentText(myIntent.getExtras().getString("sourceName"))
                .setPriority(Notification.PRIORITY_HIGH)
//                .setContentIntent(pendingIntent) // what works on tap notification

                .addAction(R.drawable.ic_baseline_task_alt_24,"Start",startPendingIntent)
                .addAction(R.drawable.ic_baseline_snooze_24, "Snooze", snoozePendingIntent)
                .addAction(R.drawable.ic_baseline_cancel_24, "Cancel", cancelPendingIntent)


                .setCategory(NotificationCompat.CATEGORY_SYSTEM)
                .setAutoCancel(true);

        notificationManager = NotificationManagerCompat.from(context);
        Notification n = builder.build();
        final int random = new Random().nextInt(10000);
        notificationManager.notify(random, n);

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
    }
}
