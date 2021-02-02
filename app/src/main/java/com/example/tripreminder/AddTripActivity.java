package com.example.tripreminder;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.DialogFragment;

import androidx.lifecycle.ViewModelProvider;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tripreminder.RoomDataBase.DatePickerClass;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import com.example.tripreminder.RoomDataBase.TripTable;
import com.example.tripreminder.RoomDataBase.TripViewModel;

public class AddTripActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    private EditText tripNameInput;
    private EditText startPointSearchView;
    private EditText endPointSearchView;

    private ImageView  calender_btn;
    private ImageView timeBtn;

    private TextView dateTextView;
    private TextView timeTextView;

    private Spinner repeating_spinner;
    private Spinner trip_type;

    private Button add_trip_btn;

    private TripViewModel tripViewModel;

    private static final String API_KEY = "AIzaSyA7dH75J8SZ0-GkeHqHANbflPhdpbfU5yI";
    private static final int START_REQUEST = 100;
    private static final int END_REQUEST = 101;


    private Place start,end;
    Calendar calender;

    static NotificationManagerCompat notificationManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);
        calender = Calendar.getInstance();
        calender.setTimeInMillis(System.currentTimeMillis());
//        calender.set

        calender = Calendar.getInstance();
        calender.setTimeInMillis(System.currentTimeMillis());

        initViews();
//        add_trip_btn.setEnabled(false);
        createNotificationChannel();



        Places.initialize(this, API_KEY);
        startPointSearchView.setFocusable(false);
        endPointSearchView.setFocusable(false);

        startPointSearchView.setOnClickListener(v -> {
            List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS,Place.Field.LAT_LNG,Place.Field.NAME);
            Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN,fieldList).build(this);
            startActivityForResult(intent, START_REQUEST);
        });
        endPointSearchView.setOnClickListener(v -> {
            List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS,Place.Field.LAT_LNG,Place.Field.NAME);
            Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN,fieldList).build(this);
            startActivityForResult(intent, END_REQUEST);
        });




        timeBtn.setOnClickListener(v -> {
            DialogFragment dialogFragment = new TimePickerClass();
            dialogFragment.show(getSupportFragmentManager(), "timepicker");

        });
        calender_btn.setOnClickListener(v -> {
            DialogFragment dialogFragment = new DatePickerClass();
            dialogFragment.show(getSupportFragmentManager(), "datepicker");
        });

        add_trip_btn.setOnClickListener(v -> {
            if(start == null || end == null ){
                Toast.makeText(this, "Please add Trip Data", Toast.LENGTH_LONG).show();
            }else
                prepareAlarm();
        });



    }

    private void initViews(){
        tripNameInput = findViewById(R.id.tripNameInput);
        startPointSearchView = findViewById(R.id.startPointSearchView);
        endPointSearchView = findViewById(R.id.endPointSearchView);

        calender_btn = findViewById(R.id.calender_btn);
        timeBtn = findViewById(R.id.timeBtn);

        dateTextView = findViewById(R.id.dateTextView);
        timeTextView = findViewById(R.id.timeTextView);

        repeating_spinner = findViewById(R.id.repeating_spinner);
        trip_type = findViewById(R.id.trip_type);


        add_trip_btn = findViewById(R.id.add_trip_btn);
        // TODO: this used to insert data in to room database
        tripViewModel= new ViewModelProvider(AddTripActivity.this, ViewModelProvider.AndroidViewModelFactory.getInstance(AddTripActivity.this.getApplication())).get(TripViewModel.class);
        tripViewModel.insert(new TripTable( "Added two", "01:33", "31/1/2021", false, "1", false, "zag", "italy", ""));

        timeTextView.setText(MessageFormat.format("{0}:{1}", calender.getTime().getHours(), calender.getTime().getMinutes()));
        dateTextView.setText(MessageFormat.format("{0}/{1}/{2}", calender.getTime().getDay(), calender.getTime().getMonth()+1, calender.getTime().getYear()+1900));
    }

    @Override // data from the place api
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case START_REQUEST:

                if(resultCode == RESULT_OK){
                    Place place = Autocomplete.getPlaceFromIntent(data); //place.getLatLng() place.getAddress()
                    startPointSearchView.setText(place.getAddress());
                    start = place;

                }else{
                    Toast.makeText(this, "An error occured , Try again...", Toast.LENGTH_SHORT).show();
                }

                break;
            case END_REQUEST:

                if(resultCode == RESULT_OK){
                    Place place = Autocomplete.getPlaceFromIntent(data); //place.getLatLng() place.getAddress()
                    endPointSearchView.setText(place.getAddress());
                    end = place;
                }else{
                    Toast.makeText(this, "An error occured , Try again...", Toast.LENGTH_SHORT).show();
                }

                break;

            default:
                Toast.makeText(this, "Please Select a Place !!", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onTimeSet(android.widget.TimePicker view, int hourOfDay, int minute) {

        calender.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calender.set(Calendar.MINUTE, minute);
        calender.set(Calendar.SECOND, 0);

        long seconds = calender.getTimeInMillis() - System.currentTimeMillis();
        seconds/= 1000;
        Toast.makeText(this, "Seconds :"+seconds, Toast.LENGTH_SHORT).show();

        timeTextView.setText(MessageFormat.format("{0}:{1}", hourOfDay, minute));
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        System.out.println("ONDate");
        calender.set(Calendar.YEAR, year);
        calender.set(Calendar.MONTH, month);
        calender.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        calender.set(Calendar.SECOND, 0);

        long days = calender.getTimeInMillis() - System.currentTimeMillis();
        days/= (1000*60*60);
        Toast.makeText(this, "days :"+days, Toast.LENGTH_SHORT).show();

        dateTextView.setText(MessageFormat.format("{0}/{1}/{2}", dayOfMonth, month, year));
    }

    private void prepareAlarm(){

        Alarm alarm = new Alarm(this,calender,start,end);
        alarm.prepareAlarm();
    }

    private void createNotificationChannel(){

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "notificationChannerl";
            String desc = "Channel for remind trip notification";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("notification",name,importance);
            channel.setDescription(desc);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}

//    String my_data= String.format(Locale.ENGLISH, "http://maps.google.com/maps?daddr=30.6178118,32.2761602(الممر)");
//    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(my_data));
//intent.setPackage("com.google.android.apps.maps");
//        startActivity(intent);