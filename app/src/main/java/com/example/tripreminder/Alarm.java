package com.example.tripreminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;
import com.google.android.libraries.places.api.model.Place;

import java.util.Random;


public class Alarm {

    private long calender;
    private Context context;
    private Place startPlace,endPlace;
    private String name,repetation;
    private long idT;
    private int notificationID;
    private boolean ways;
    private Location startL;

    public Alarm(Context context, long calendar, Place start, Place end , boolean way, String name,long idT,String repetation){
        this.context = context;
        this.calender = calendar;
        startPlace = start;
        endPlace = end;
        this.ways = way;
        this.name = name;
        this.idT=idT;
        this.notificationID = (int)idT;
        this.repetation = repetation;
    }

    public Alarm(Context context, long calendar, Location start, Place end , boolean way, String name,long idT,String repetation){
        this.context = context;
        this.calender = calendar;
        this.startL = start;
        endPlace = end;
        this.ways = way;
        this.name = name;
        this.idT=idT;
        this.notificationID = (int) idT;
        this.repetation = repetation;

    }
    public Alarm(){

    }

    public void prepareAlarm(){
        Intent intent = new Intent(context,TransparentActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        |Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_TOP

        if(startPlace !=null){
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

        intent.putExtra("ID", idT);
        intent.putExtra("notificationID",notificationID);
        intent.putExtra("repetation", repetation);
        intent.putExtra("calendar",calender);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, notificationID, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        long triggerAt = calender;
        alarmManager.setExact(AlarmManager.RTC_WAKEUP,triggerAt,pendingIntent);
    }

    public void prepareAlarm(Context context,long calender,double sLat,double sLon,String sName,double dLat,double dLong,String dName,boolean way,String name,long idT,String repetation){
        Intent intent = new Intent(context,TransparentActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        |Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_TOP


            intent.putExtra("sourceLat",sLat);
            intent.putExtra("sourceLon", sLon);
            intent.putExtra("sourceName", sName);

        intent.putExtra("destinationLat", dLat);
        intent.putExtra("destinationLon", dLong);
        intent.putExtra("destinationName", dName);
        intent.putExtra("tripName", name);
        intent.putExtra("ways", way);

        intent.putExtra("ID", idT);
        intent.putExtra("repetation", repetation);
        intent.putExtra("calendar",calender);
        intent.putExtra("notificationID",(int)idT);


        PendingIntent pendingIntent = PendingIntent.getActivity(context, (int)(idT+calender), intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        long triggerAt = calender;
        alarmManager.setExact(AlarmManager.RTC_WAKEUP,triggerAt,pendingIntent);
    }
}
//1612882072721