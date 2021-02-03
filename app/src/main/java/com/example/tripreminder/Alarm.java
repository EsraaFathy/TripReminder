package com.example.tripreminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.libraries.places.api.model.Place;

import java.util.Calendar;
import java.util.Random;

public class Alarm {

    Calendar calender;
    Context context;
    Place startPlace,endPlace;
    String name;
    boolean ways;
    public Alarm(Context context, Calendar calendar, Place start, Place end , boolean way, String name){
        this.context = context;
        this.calender = calendar;
        startPlace = start;
        endPlace = end;
        this.ways = way;
        this.name = name;
    }

    public void prepareAlarm(){
        Intent intent = new Intent(context,AlertDialog.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);

        if(startPlace !=null){
            Log.i("log", "not null alarm");
            intent.putExtra("sourceLat", startPlace.getLatLng().latitude);
            intent.putExtra("sourceLon", startPlace.getLatLng().longitude);
            intent.putExtra("sourceName", startPlace.getName());
        }
        intent.putExtra("destinationLat", endPlace.getLatLng().latitude);
        intent.putExtra("destinationLon", endPlace.getLatLng().longitude);
        intent.putExtra("destinationName", endPlace.getName());
        intent.putExtra("tripName", name);
        intent.putExtra("ways", ways);
        final int random = new Random().nextInt(1000000);
        intent.putExtra("ID", random);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, random, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        long period = calender.getTimeInMillis() ;

        alarmManager.setExact(AlarmManager.RTC_WAKEUP,period,pendingIntent);
        Toast.makeText(context, "period : "+period, Toast.LENGTH_SHORT).show();
    }
}
