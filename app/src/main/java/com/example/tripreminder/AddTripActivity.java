package com.example.tripreminder;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.DialogFragment;

import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
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
import java.util.List;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);
        initViews();

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

        // TODO: this used to insert data in to room database
        tripViewModel= new ViewModelProvider(AddTripActivity.this, ViewModelProvider.AndroidViewModelFactory.getInstance(AddTripActivity.this.getApplication())).get(TripViewModel.class);
        tripViewModel.insert(new TripTable( "Added two", "01:33", "31/1/2021", false, "1", false, "zag", "italy", ""));

    }

    @Override // data from the place api
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case START_REQUEST:

                if(resultCode == RESULT_OK){
                    Place place = Autocomplete.getPlaceFromIntent(data); //place.getLatLng() place.getAddress()
                    startPointSearchView.setText(place.getAddress());
                }else{
                    Toast.makeText(this, "An error occured , Try again...", Toast.LENGTH_SHORT).show();
                }

                break;
            case END_REQUEST:

                if(resultCode == RESULT_OK){
                    Place place = Autocomplete.getPlaceFromIntent(data); //place.getLatLng() place.getAddress()
                    endPointSearchView.setText(place.getAddress());
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
        Toast.makeText(this, "hour : "+hourOfDay+"minute", Toast.LENGTH_SHORT).show();
        timeTextView.setText(MessageFormat.format("{0}:{1}", hourOfDay, minute));
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        dateTextView.setText(MessageFormat.format("{0}/{1}/{2}", dayOfMonth, month, year));
    }
}

//    String my_data= String.format(Locale.ENGLISH, "http://maps.google.com/maps?daddr=30.6178118,32.2761602(الممر)");
//    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(my_data));
//intent.setPackage("com.google.android.apps.maps");
//        startActivity(intent);