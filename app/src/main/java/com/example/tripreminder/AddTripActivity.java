package com.example.tripreminder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import com.example.tripreminder.model.Trip;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

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
    private Long idT;
    private HandelLocation handelLocation;
    private double distance = 0.0;
    private Location mlocation;
    boolean way;

    Calendar calender;
    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            loadingBar.dismiss();
            ofterGetID();
            finish();
            return false;
        }
    });
    Handler handlerLocation = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            Location l = (Location) msg.obj;
            String fullAddress=getStringLocation(l);
            distance = calculationByDistance(l, end.getLatLng());
            TripTable table = new TripTable(binding.tripNameInput.getText().toString(),
                    binding.timeTextView.getText().toString(),
                    binding.dateTextView.getText().toString(),
                    "up Coming",
                    getRepetation(), getWay(),
                    fullAddress,
                    binding.endPointSearchView.getText().toString(),
                    "",
                    distance,
                    l.getLatitude(),
                    l.getLongitude(),
                    end.getLatLng().latitude,
                    end.getLatLng().longitude);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    idT = tripViewModel.insert(table);
                    loadingBar.dismiss();
                    ofterGetID();
                    finish();
                }
            }).start();
            return false;
        }
    });

    @Override

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == HandelLocation.LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                handelLocation.getLocation();

            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_trip);
        calender = Calendar.getInstance();
        tripViewModel = new ViewModelProvider(AddTripActivity.this, ViewModelProvider.AndroidViewModelFactory.getInstance(AddTripActivity.this.getApplication())).get(TripViewModel.class);
        handelLocation = new HandelLocation(this);
//        repetation = getRepetation();
//        way = getWay();
        calender.setTimeInMillis(System.currentTimeMillis());
        binding.timeTextView.setText(MessageFormat.format("{0}:{1}", calender.getTime().getHours(), calender.getTime().getMinutes()));
        binding.dateTextView.setText(MessageFormat.format("{0}/{1}/{2}", calender.getTime().getDate(), calender.getTime().getMonth() + 1, calender.getTime().getYear() + 1900));
        loadingBar = new ProgressDialog(this);
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
                    if (start == null) {
                        addTripToRoom(binding.tripNameInput.getText().toString(),
                                binding.timeTextView.getText().toString(), binding.dateTextView.getText().toString(), "up Coming",
                                getRepetation(), getWay(),
                                binding.startPointSearchView.getText().toString(),
                                binding.endPointSearchView.getText().toString(),
                                0.0,
                                0.0,
                                end.getLatLng().latitude,
                                end.getLatLng().longitude);
                    } else {
                        addTripToRoom(binding.tripNameInput.getText().toString(),
                                binding.timeTextView.getText().toString(), binding.dateTextView.getText().toString(), "up Coming",
                                getRepetation(), getWay(),
                                binding.startPointSearchView.getText().toString(),
                                binding.endPointSearchView.getText().toString(),
                                start.getLatLng().latitude,
                                start.getLatLng().longitude,
                                end.getLatLng().latitude, end.getLatLng().longitude);
                    }
                }

//                if (end == null)
//                    Toast.makeText(getApplicationContext(), "Please add Trip Destination", Toast.LENGTH_LONG).show();
//                else
//                    prepareAlarm();

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
        Alarm alarm;
        if (start == null)
            alarm = new Alarm(this, calender, mlocation, end, getWay(), binding.tripNameInput.getText().toString(), idT,getRepetation());
        else {
            alarm = new Alarm(this, calender, start, end, getWay(), binding.tripNameInput.getText().toString(), idT,getRepetation());
        }
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
                binding.endPointSearchView.getText().toString(),
                start.getLatLng().latitude,
                start.getLatLng().longitude,
                end.getLatLng().latitude,
                end.getLatLng().longitude);
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

    private void editTripInRoom(String title, String time, String date, String status, String repetition, boolean ways, String from, String to, double latStart, double LongStart, double LatEnd, double longEnd) {
        if (title.equals("") || time.equals("") || date.equals("") || repetition.equals("") || to.equals("")) {
            Toast.makeText(this, "Their is some data missed", Toast.LENGTH_SHORT).show();

        } else {
            distance = calculationByDistance(start.getLatLng(), end.getLatLng());
            TripTable table = new TripTable(title, time, date, status, repetition, ways, from, to, tripTable.getNotes(), distance, latStart, LongStart, LatEnd, longEnd);
            table.setId(id);
            tripViewModel.update(table);
            finish();

        }
    }

    private void addTripToRoom(String title, String time, String date, String status, String repetition, boolean ways, String from, String to, double latStart, double LongStart, double LatEnd, double longEnd) {
        if (title.equals("") || time.equals("") || date.equals("") || repetition.equals("") || to.equals("")) {
            Toast.makeText(this, "Their is some data missed", Toast.LENGTH_SHORT).show();
        } else {
            loadingBar.setTitle("Create new Trip");
            loadingBar.setMessage("Please wait some seconds...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            if (from.equals("")) {
                handelLocation.getLocation();
            } else {
                distance = calculationByDistance(start.getLatLng(), end.getLatLng());
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("TAG", "run distance: " + distance);
                        TripTable table = new TripTable(title, time, date, status, repetition, ways, from, to, "", distance, latStart, LongStart, LatEnd, longEnd);
                        idT = tripViewModel.insert(table);
                        handler.sendEmptyMessage(1);
                    }
                }).start();
            }

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
                    intent.getDoubleExtra(HomeFragment.DISTANCE, 0.0),
                    intent.getDoubleExtra(HomeFragment.LatStart, 0.0),
                    intent.getDoubleExtra(HomeFragment.LongStart, 0.0),
                    intent.getDoubleExtra(HomeFragment.LatEnd, 0.0),
                    intent.getDoubleExtra(HomeFragment.LongEnd, 0.0));
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


    private TripTable getTripTableById(long idT) {
        /// TODO call in thread
        TripTable table1 = tripViewModel.getTripRowById(idT);
        return table1;
    }

    private String getStringLocation(Location mlocation){
        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(AddTripActivity.this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(mlocation.getLatitude(), mlocation.getLongitude(), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String address = addresses.get(0).getAddressLine(0);
        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();
        return country+","+state+", "+city+", "+address;
    }
    private double calculationByDistance(LatLng StartP, LatLng EndP) {
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

    private double calculationByDistance(Location location, LatLng EndP) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = location.getLatitude();
        double lat2 = EndP.latitude;
        double lon1 = location.getLongitude();
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


    public class HandelLocation {
        private final Context context;
        public final static int LOCATION_PERMISSION_REQUEST_CODE = 1234;
        private final FusedLocationProviderClient fusedLocationProviderClient;
        private Location currentLocation;
        private HandelLocation mangingLocation;
        Geocoder geocoder;

        @SuppressLint("MissingPermission")
        private HandelLocation(Context context) {
            this.context = context;
            geocoder = new Geocoder(context);
            LocationCallback mLocationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    super.onLocationResult(locationResult);
                }
            };

            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setInterval(10000);
            locationRequest.setFastestInterval(5000);
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, mLocationCallback, Looper.myLooper());
        }

        public HandelLocation getMangingLocationinstance(Context context) {
            if (mangingLocation == null) {
                mangingLocation = new HandelLocation(context);
            }
            return mangingLocation;
        }

        @SuppressLint("MissingPermission")
        public void getLocation() {
            Task<Location> task;
            if (chickPermition()) {
                task = fusedLocationProviderClient.getLastLocation();
                task.addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        mlocation = location;
                        Message message = new Message();
                        message.obj = mlocation;
                        handlerLocation.sendMessage(message);



                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("TAG", "Failed");
                    }
                });
            } else {
                requestPremition();
            }
            //return currentLocation;
        }


        public boolean chickPermition() {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                return true;
            }
            return false;
        }

        public void requestPremition() {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }

        public void requestResult(int requestCode, String[] permissions, int[] grantResults) {
            Log.d("TAG", "requestResult: ");
        }


        public void verifyLocationEnabled() {
            LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            final boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (!gpsEnabled) {
                enableLocationSitting();
            }

        }

        public void enableLocationSitting() {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            context.startActivity(intent);
        }

    }


}