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

    Intent intent;
    public static NotificationManagerCompat notificationManager;
    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("OnRecieve");
        this.intent = intent;
        System.out.println(intent.getExtras().get("sourceName"));
        Log.i("log", intent.getExtras().get("sourceName").toString());
        createNotification(context);

        try{

            Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            rigntone = RingtoneManager.getRingtone(context, uri);
            rigntone.play();
        }
        catch(Exception e){}
    }

    private void createNotification(Context context){

        Intent startInten = new Intent(context,NotificationActionService.class).setAction(START_SERVICE);
        PendingIntent startPendingIntent = PendingIntent.getService(context, 0, startInten, PendingIntent.FLAG_ONE_SHOT);
        setTripDate(startInten);

        Intent snoozeIntent = new Intent(context,NotificationActionService.class).setAction(SNOOZE_SERVICE);
        PendingIntent snoozePendingIntent = PendingIntent.getService(context, 0, snoozeIntent, PendingIntent.FLAG_ONE_SHOT);
        setTripDate(snoozeIntent);

        Intent cancelIntent = new Intent(context,NotificationActionService.class).setAction(CANCEL_SERVICE);
        PendingIntent cancelPendingIntent = PendingIntent.getService(context, 0, cancelIntent, PendingIntent.FLAG_ONE_SHOT);
        setTripDate(cancelIntent);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "notification")
                .setSmallIcon(R.drawable.ic_baseline_add_alert_24)
                .setContentTitle("Remind me a trip")
                .setContentText(intent.getExtras().getString("sourceName"))
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
        Log.i("log", "startTripData :"+intent.getDoubleExtra("sourceLat", 0));
        pass.putExtra("sourceLat",intent.getDoubleExtra("sourceLat",0));
        pass.putExtra("sourceLon",intent.getDoubleExtra("sourceLon",0));
        pass.putExtra("sourceName",intent.getStringExtra("sourceName"));
        pass.putExtra("destinationLan", intent.getDoubleExtra("destinationLat",0));
        pass.putExtra("destinationLon",intent.getDoubleExtra("destinationLon",0));
        pass.putExtra("destinationName",intent.getStringExtra("destinationName"));
    }
}
