package com.example.tripreminder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TableLayout;
import android.widget.Toast;

import com.example.tripreminder.Fragments.HomeFragment;
import com.example.tripreminder.RoomDataBase.TripTable;
import com.example.tripreminder.RoomDataBase.TripViewModel;
import com.example.tripreminder.databinding.ActivityAddTripBinding;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import static androidx.core.app.ActivityCompat.startActivityForResult;

public class AddTripActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {
    ActivityAddTripBinding binding;
    private int id = -1;
    private TripTable tripTable;

    private static final int SYSTEM_ALERT_WINDOW_PERMISSION = 2084;
    private static final String API_KEY = "AIzaSyA7dH75J8SZ0-GkeHqHANbflPhdpbfU5yI";
    private static final int START_REQUEST = 100;
    private static final int END_REQUEST = 101;
    private Place start, end;
    private ProgressDialog loadingBar;
    private TripViewModel tripViewModel;
    Long idT;
    private double distance=0.0;


    Calendar calender;
    Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            loadingBar.dismiss();
            ofterGetID();
            finish();
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_trip);
        calender = Calendar.getInstance();
        tripViewModel = new ViewModelProvider(AddTripActivity.this, ViewModelProvider.AndroidViewModelFactory.getInstance(AddTripActivity.this.getApplication())).get(TripViewModel.class);

        calender.setTimeInMillis(System.currentTimeMillis());
        binding.timeTextView.setText(MessageFormat.format("{0}:{1}", calender.getTime().getHours(), calender.getTime().getMinutes()));
        binding.dateTextView.setText(MessageFormat.format("{0}/{1}/{2}", calender.getTime().getDay(), calender.getTime().getMonth() + 1, calender.getTime().getYear() + 1900));
        loadingBar= new ProgressDialog(this);
        getIntentToEditTrip();
        createNotificationChannel();

        Places.initialize(this, API_KEY);
        binding.startPointSearchView.setFocusable(false);
        binding.endPointSearchView.setFocusable(false);

        binding.startPointSearchView.setOnClickListener(v -> {
            List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.NAME);
            Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fieldList).build(this);
            startActivityForResult(intent, START_REQUEST);
        });

        binding.endPointSearchView.setOnClickListener(v -> {
            List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.NAME);
            Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fieldList).build(this);
            startActivityForResult(intent, END_REQUEST);
        });

        binding.timeBtn.setOnClickListener(v -> {
            DialogFragment dialogFragment = new TimePickerClass();
            dialogFragment.show(getSupportFragmentManager(), "timepicker");
        });
        binding.calenderBtn.setOnClickListener(v -> {
            DialogFragment dialogFragment = new DatePickerClass();
            dialogFragment.show(getSupportFragmentManager(), "datepicker");
        });


        binding.addTripBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.addTripBtn.getText().equals("Edit")) {
                    Toast.makeText(AddTripActivity.this, "bla bla", Toast.LENGTH_SHORT).show();
                    editTripFromUI();
                } else {
                    String repetation = getRepetation();
                    boolean way = getWay();
                    addTripToRoom(binding.tripNameInput.getText().toString(),
                            binding.timeTextView.getText().toString(), binding.dateTextView.getText().toString(), "up Coming",
                            repetation, way,
                            binding.startPointSearchView.getText().toString(),
                            binding.endPointSearchView.getText().toString());
                }

            }
        });

    }

    private void ofterGetID() {
        if (end == null)
            Toast.makeText(getApplicationContext(), "Please add Trip Data", Toast.LENGTH_LONG).show();
        else
            prepareAlarm();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(getApplicationContext())) {
            askPermission();
        }
    }

    @Override // data from the place api
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case START_REQUEST:

                if (resultCode == RESULT_OK) {
                    Place place = Autocomplete.getPlaceFromIntent(data); //place.getLatLng() place.getAddress()
                    binding.startPointSearchView.setText(place.getAddress());
                    start = place;

                } else {
                    Toast.makeText(this, "An error occured , Try again...", Toast.LENGTH_SHORT).show();
                }

                break;
            case END_REQUEST:

                if (resultCode == RESULT_OK) {
                    Place place = Autocomplete.getPlaceFromIntent(data); //place.getLatLng() place.getAddress()
                    binding.endPointSearchView.setText(place.getAddress());
                    end = place;
                } else {
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
        seconds /= 1000;
        Toast.makeText(this, "Seconds :" + seconds, Toast.LENGTH_SHORT).show();

        binding.timeTextView.setText(MessageFormat.format("{0}:{1}", hourOfDay, minute));
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        System.out.println("ONDate");
        calender.set(Calendar.YEAR, year);
        calender.set(Calendar.MONTH, month);
        calender.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        calender.set(Calendar.SECOND, 0);

        long days = calender.getTimeInMillis() - System.currentTimeMillis();
        days /= (1000 * 60 * 60);
        Toast.makeText(this, "days :" + days, Toast.LENGTH_SHORT).show();

        binding.dateTextView.setText(MessageFormat.format("{0}/{1}/{2}", dayOfMonth, month, year));
    }

    private void prepareAlarm() {

        Alarm alarm = new Alarm(this, calender, start, end);
        alarm.prepareAlarm();
    }

    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "notificationChannerl";
            String desc = "Channel for remind trip notification";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("notification", name, importance);
            channel.setDescription(desc);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void editTripFromUI() {
        String repetation = getRepetation();
        boolean way = getWay();
        editTripInRoom(binding.tripNameInput.getText().toString(),
                binding.timeTextView.getText().toString(), binding.dateTextView.getText().toString(), tripTable.getStatus(),
                repetation, way,
                binding.startPointSearchView.getText().toString(),
                binding.endPointSearchView.getText().toString());
    }

    private boolean getWay() {
        boolean way = true;
        switch (binding.tripType.getSelectedItemPosition()) {
            case 0:
                /// one way
                way = true;
                break;
            case 1:
                /// rounded trip
                way = false;
        }
        return way;
    }

    private String getRepetation() {
        String repetation = "";
        switch (binding.repeatingSpinner.getSelectedItemPosition()) {
            case 0:
                repetation = "No Repeated";
                break;
            case 1:
                repetation = "Repeated Daily";
                break;
            case 2:
                repetation = "Repeated weekly";
                break;
            case 3:
                repetation = "Repeated Monthly";
                break;
        }
        return repetation;
    }

    private void editTripInRoom(String title, String time, String date, String status, String repetition, boolean ways, String from, String to) {
        if (title.equals("") || time.equals("") || date.equals("") || repetition.equals("") || to.equals("")) {
            Toast.makeText(this, "Their is some data missed", Toast.LENGTH_SHORT).show();

        } else {
            distance=calculationByDistance(start.getLatLng(),end.getLatLng());
            TripTable table = new TripTable(title, time, date, status, repetition, ways, from, to, tripTable.getNotes(),distance);
            table.setId(id);
            tripViewModel.update(table);
            finish();

        }
    }

    private void addTripToRoom(String title, String time, String date, String status, String repetition, boolean ways, String from, String to) {
        if (title.equals("") || time.equals("") || date.equals("") || repetition.equals("") || to.equals("")) {
            Toast.makeText(this, "Their is some data missed", Toast.LENGTH_SHORT).show();
        } else {
            loadingBar.setTitle("Creating new account");
            loadingBar.setMessage("Please wait, while we are creating an account for you");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    distance=calculationByDistance(start.getLatLng(),end.getLatLng());
                    Log.d("TAG", "run distance: "+distance);
                    TripTable table = new TripTable(title, time, date, status, repetition, ways, from, to, "",distance);
                    idT = tripViewModel.insert(table);
                    handler.sendEmptyMessage(1);

                }
            }).start();
        }
    }

    @SuppressLint("SetTextI18n")
    private void getIntentToEditTrip() {
        Intent intent = getIntent();
        if (intent.getStringExtra("key") != null) {
            binding.addTripBtn.setText("Edit");
            id = intent.getIntExtra(HomeFragment.NOTE_INTENT_ID, -1);
            Toast.makeText(this, "id = " + id, Toast.LENGTH_SHORT).show();
            tripTable = new TripTable(
                    intent.getStringExtra(HomeFragment.NOTE_INTENT_title),
                    intent.getStringExtra(HomeFragment.NOTE_INTENT_time),
                    intent.getStringExtra(HomeFragment.NOTE_INTENT_date),
                    intent.getStringExtra(HomeFragment.NOTE_INTENT_status),
                    intent.getStringExtra(HomeFragment.NOTE_INTENT_repetition),
                    intent.getBooleanExtra(HomeFragment.NOTE_INTENT_ways, true),
                    intent.getStringExtra(HomeFragment.NOTE_INTENT_FROM),
                    intent.getStringExtra(HomeFragment.NOTE_INTENT_to),
                    intent.getStringExtra(HomeFragment.NOTE_INTENT_Note),
                    intent.getDoubleExtra(HomeFragment.DISTANCE,0.0));
            editTrip(tripTable);
        }
    }

    private void editTrip(TripTable tripTable) {
        // TODO add text to edit text in ui
        binding.tripNameInput.setText(tripTable.getTitle());
        binding.startPointSearchView.setText(tripTable.getFrom());
        binding.endPointSearchView.setText(tripTable.getTo());
        binding.dateTextView.setText(tripTable.getDate());
        binding.timeTextView.setText(tripTable.getTime());

        switch (tripTable.getRepetition()) {
            case "No Repeated":
                binding.repeatingSpinner.setSelection(0);
                break;
            case "Repeated Daily":
                binding.repeatingSpinner.setSelection(1);
                break;
            case "Repeated weekly":
                binding.repeatingSpinner.setSelection(2);
                break;
            case "Repeated Monthly":
                binding.repeatingSpinner.setSelection(3);
                break;
        }


        if (tripTable.getWays())
            binding.tripType.setSelection(0);
        else
            binding.tripType.setSelection(1);

    }

    private void askPermission() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + getPackageName()));
        startActivityForResult(intent, SYSTEM_ALERT_WINDOW_PERMISSION);
    }


    private TripTable getTripTableById(long idT){
        /// TODO call in thread
        TripTable table1= tripViewModel.getTripRowById(idT);
        return table1;
    }
    private void UpdateStatusByID(int idT,String status){
        /// TODO call in thread
        TripTable table1= tripViewModel.getTripRowById(idT);
        tripTable.setStatus(status);
        tripTable.setId(idT);
        tripViewModel.update(tripTable);
    }

    public double calculationByDistance(LatLng StartP, LatLng EndP) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);

        return Radius * c;
    }

}