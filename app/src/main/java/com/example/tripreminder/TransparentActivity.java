package com.example.tripreminder;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.tripreminder.RoomDataBase.TripTable;
import com.example.tripreminder.RoomDataBase.TripViewModel;
import com.example.tripreminder.serveses.FloatingViewService;

import java.util.Locale;

public class TransparentActivity extends AppCompatActivity {

    public static Ringtone rigntone;
    public static final String START_SERVICE = "com.example.tripreminder.StartService";
    public static final String SNOOZE_SERVICE = "com.example.tripreminder.SnoozeService";
    public static final String CANCEL_SERVICE = "com.example.tripreminder.CancelService";

    private String SOURCE_URL= "http://maps.google.com/maps?saddr=";
    private String DEST_URL= "http://maps.google.com/maps?daddr=";
    private String note;

    Intent myIntent;
    PendingIntent startPendingIntent;
    private TripViewModel tripViewModel;
    NotificationUtils notificationUtils;
    NotificationManager notificationManager;
    long idT;
    private String status;
    Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            startFloatingIcon(note);
            return false;
        }
    });
    Handler statusHandler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            if (!status.equals("up Coming")){
                Log.d("TAG", "handleMessage: ");
                finish();
            }else {
                afterHanderStatus();
            }
            return false;
        }
    });
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myIntent = getIntent();
        idT= myIntent.getLongExtra("ID",-1);
        Log.i("ID",""+idT);
        tripViewModel = new ViewModelProvider(TransparentActivity.this, ViewModelProvider.AndroidViewModelFactory.getInstance(TransparentActivity.this.getApplication())).get(TripViewModel.class);
        getStatusById(idT);
    }

    private void createNotification(Context context,long ID){
        Intent tapNotification = new Intent(getApplicationContext(), com.example.tripreminder.TransparentActivity.class).setAction(START_SERVICE);
        tapNotification.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //long id in intent
        Log.i("create noti",ID+"");
        tapNotification.putExtra("ID",ID);
        setTripDate(tapNotification);
        // int id to cancel
        int id = (int)ID;
        startPendingIntent = PendingIntent.getActivity(getApplicationContext(), id, tapNotification, PendingIntent.FLAG_ONE_SHOT);


         notificationUtils = new NotificationUtils(context);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationCompat.Builder nb = notificationUtils.getAndroidChannelNotification(myIntent.getStringExtra("tripName"),startPendingIntent);
            notificationUtils.getManager().notify(id, nb.build());

        }

    }

    private String GetNotes(long id){
        tripViewModel.getNotes((int)id).observe(TransparentActivity.this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                note =s;
                handler.sendEmptyMessage(0);
            }
        });


        return note;
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
        pass.putExtra("ID", myIntent.getLongExtra("ID", 0));
        pass.putExtra("tripName", myIntent.getStringExtra("tripName"));
        pass.putExtra("ways", myIntent.getBooleanExtra("ways",false));
        pass.putExtra("repetation", myIntent.getStringExtra("repetation"));
    }

    private void startTrip(){

        Toast.makeText(getApplicationContext(), "start", Toast.LENGTH_SHORT).show();
        double sourceLat,sourceLon,destinationLat,destinationLon;
        String sourceName,destinationName;


        destinationLat = myIntent.getDoubleExtra("destinationLat", 0);
        destinationLon = myIntent.getDoubleExtra("destinationLon", 0);
        destinationName = myIntent.getStringExtra("destinationName");
        idT= (int) myIntent.getLongExtra("ID",-1);
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
        GetNotes(idT);

        startActivity(mapIntent);

        //Todo: delete this trip
    }

    private void startFloatingIcon(String notes){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            startService(new Intent(getApplicationContext(), FloatingViewService.class).putExtra("Notes",notes));
        }else if (Settings.canDrawOverlays(this)) {
            startService(new Intent(getApplicationContext(), FloatingViewService.class).putExtra("Notes",notes));
        }else {
            Toast.makeText(this, "You need System Alert Window Permission to do this", Toast.LENGTH_SHORT).show();
        }
    }

    private void UpdateStatusByID(long idT,String status){
        new Thread(){
            @Override
            public void run() {
                TripTable table1= tripViewModel.getTripRowById(idT);
                table1.setStatus(status);
                table1.setId((int)idT);
                tripViewModel.update(table1);
            }
        }.start();

    }
    private void afterHanderStatus(){
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        rigntone = RingtoneManager.getRingtone(getApplicationContext(), uri);
        if(!rigntone.isPlaying())
            rigntone.play();

        notificationUtils=new NotificationUtils(getApplicationContext());
        notificationManager=notificationUtils.getManager();
        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle(myIntent.getStringExtra("tripName"));
        builder.setMessage("Now it's time for trip !!");
        builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
//                idT= (int) myIntent.getLongExtra("ID",-1);
                rigntone.stop();

                if(!myIntent.getExtras().getString("repetation","null").equals("No Repeated")){
                    // notification won't be canceld
                    //todo:: adding this trip in history as canceled but not to be deleted
                    Log.i("log", "onClick: Cancel");
                }else{
                    UpdateStatusByID(idT,"Canceled");
                }
                notificationManager.cancel((int)idT);
                finishAndRemoveTask();

            }
        });
        builder.setNegativeButton("Later", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // id long
                finishAndRemoveTask();
                createNotification(getApplicationContext(),idT);
                rigntone.stop();
            }
        });
        builder.setNeutralButton("Start", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.i("startID",""+idT);
                roundedTrip(idT);
                rigntone.stop();

                if(!myIntent.getExtras().getString("repetation","null").equals("No Repeated")){
                    // notification won't be canceld
                    //todo:: adding this trip in history but not to be deleted

                }else{
                    UpdateStatusByID(idT,"Done");
                }
                notificationManager.cancel((int)idT);
                startTrip();
                finishAndRemoveTask();

            }
        });
        final AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(dialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(this, R.color.colorPrim));
        dialog.getButton(dialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(this, R.color.colorPrim));

    }
    private void getStatusById(long id){
        new Thread(new Runnable() {
            @Override
            public void run() {
                status=tripViewModel.getStatusById(id);
                statusHandler.sendEmptyMessage(0);
            }
        }).start();
    }

    private void roundedTrip(long idT) {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    TripTable tripTable=tripViewModel.getTripRowById(idT);
                    if (!tripTable.getWays()) {
                        tripViewModel.insert(new TripTable(tripTable.getTitle(),
                                tripTable.getTime(),
                                tripTable.getDate(),
                                "up Coming",
                                tripTable.getRepetition(),
                            true,
                            tripTable.getTo(),
                            tripTable.getFrom(),
                            tripTable.getNotes(),
                            tripTable.getDistance(),
                            tripTable.getLatEnd(),
                            tripTable.getLongEnd(),
                            tripTable.getLatStart(),
                            tripTable.getLatStart()));
                    }
                }
            }).start();
    }

}
