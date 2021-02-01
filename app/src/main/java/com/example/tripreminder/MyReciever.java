package com.example.tripreminder;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Random;

public class MyReciever extends BroadcastReceiver {

    public static Ringtone rigntone;
    public static NotificationManagerCompat notificationManager;
    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("OnRecieve");

        Intent intent1 = new Intent(context,MainActivity.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent1, 0);

        final int random = new Random().nextInt(10000);

//        Intent ok = new Intent(context,OK.class);
//        ok.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        ok.putExtra("notificationID", random);
//        PendingIntent pendingIntentok = PendingIntent.getActivity(context, 0, ok, PendingIntent.FLAG_ONE_SHOT);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "notification")
                .setSmallIcon(R.drawable.ic_baseline_add_alert_24)
                .setContentTitle("Remind me a trip")
                .setContentText("Hey buddy, it's time for a new trip")
                .setPriority(Notification.PRIORITY_HIGH)
                .setContentIntent(pendingIntent) // what works on tap notification
//                .addAction(R.drawable.ic_baseline_task_alt_24,"Start",pendingIntentok)
//                .addAction(R.drawable.ic_baseline_snooze_24, "Snooze", pendingIntentok)
//                .addAction(R.drawable.ic_baseline_cancel_24, "Cancel", pendingIntentok)
                .setCategory(NotificationCompat.CATEGORY_SYSTEM)
                .setAutoCancel(true);

        notificationManager = NotificationManagerCompat.from(context);
        Notification n = builder.build();
        notificationManager.notify(random, n);

        n.vibrate = new long[]{150, 300, 150, 400};
        n.flags = Notification.FLAG_AUTO_CANCEL;
        //create a vibration
        try{

            Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            rigntone = RingtoneManager.getRingtone(context, uri);
            rigntone.play();
        }
        catch(Exception e){}
    }
}
