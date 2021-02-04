package com.example.tripreminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
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
    long idT;
    boolean ways;
    Location startL;
    public Alarm(Context context, Calendar calendar, Place start, Place end , boolean way, String name,long idT){
        this.context = context;
        this.calender = calendar;
        startPlace = start;
        endPlace = end;
        this.ways = way;
        this.name = name;
        this.idT=idT;
    }

    public Alarm(Context context, Calendar calendar, Location start, Place end , boolean way, String name,long idT){
        this.context = context;
        this.calender = calendar;
        this.startL = start;
        endPlace = end;
        this.ways = way;
        this.name = name;
        this.idT=idT;
    }

    public void prepareAlarm(){
        Intent intent = new Intent(context,TransparentActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        if(startPlace !=null){
            Log.i("log", "not null alarm");
            intent.putExtra("sourceLat", startPlace.getLatLng().latitude);
            intent.putExtra("sourceLon", startPlace.getLatLng().longitude);
            intent.putExtra("sourceName", startPlace.getName());
        }else {
            intent.putExtra("sourceLat", startL.getLatitude());
            intent.putExtra("sourceLon", startL.getLongitude());
            intent.putExtra("sourceName", "Your Location");
        }
        intent.putExtra("destinationLat", endPlace.getLatLng().latitude);
        intent.putExtra("destinationLon", endPlace.getLatLng().longitude);
        intent.putExtra("destinationName", endPlace.getName());
        intent.putExtra("tripName", name);
        intent.putExtra("ways", ways);
        //intent.putExtra("tripId",idT);
        //final int random = new Random().nextInt(1000000);
        intent.putExtra("ID", idT);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, (int)idT, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        long period = calender.getTimeInMillis() ;

        alarmManager.setExact(AlarmManager.RTC_WAKEUP,period,pendingIntent);
    }
}
