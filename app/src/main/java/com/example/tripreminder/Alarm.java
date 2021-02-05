package com.example.tripreminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.util.Log;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.google.android.libraries.places.api.model.Place;

import java.util.Calendar;
import java.util.Random;

public class Alarm {

    private Calendar calender;
    private Context context;
    private Place startPlace,endPlace;
    private String name,repetation;
    private long idT;
    private boolean ways;
    private Location startL;
    public Alarm(Context context, Calendar calendar, Place start, Place end , boolean way, String name,long idT,String repetation){
        this.context = context;
        this.calender = calendar;
        startPlace = start;
        endPlace = end;
        this.ways = way;
        this.name = name;
        this.idT=idT;
        this.repetation = repetation;
    }

    public Alarm(Context context, Calendar calendar, Location start, Place end , boolean way, String name,long idT,String repetation){
        this.context = context;
        this.calender = calendar;
        this.startL = start;
        endPlace = end;
        this.ways = way;
        this.name = name;
        this.idT=idT;
        this.repetation = repetation;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void prepareAlarm(){
        Intent intent = new Intent(context,TransparentActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        |Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_TOP

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
        intent.putExtra("repetation", repetation);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, (int)idT, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        long triggerAt = calender.getTimeInMillis() ;

        switch(repetation){
            case "No Repeated":
                Log.i("log", "no repetation");
                alarmManager.setExact(AlarmManager.RTC_WAKEUP,triggerAt,pendingIntent);
                break;
            case "Repeated Daily":
                Log.i("log", "repetation daily");
//                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, triggerAt, AlarmManager.INTERVAL_DAY, pendingIntent);
                alarmManager.setExact(AlarmManager.RTC_WAKEUP,triggerAt,pendingIntent);
                break;
            case "Repeated weekly":
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,triggerAt,AlarmManager.INTERVAL_DAY*7,pendingIntent);
                break;
            case "Repeated Monthly":
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,triggerAt,AlarmManager.INTERVAL_DAY * 30 ,pendingIntent);
                break;
        }

    }
}
