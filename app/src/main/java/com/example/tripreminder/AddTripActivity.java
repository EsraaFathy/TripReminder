package com.example.tripreminder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TableLayout;
import android.widget.Toast;

import com.example.tripreminder.Fragments.HomeFragment;
import com.example.tripreminder.RoomDataBase.TripTable;
import com.example.tripreminder.RoomDataBase.TripViewModel;
import com.example.tripreminder.databinding.ActivityAddTripBinding;

public class AddTripActivity extends AppCompatActivity {
    ActivityAddTripBinding binding;
    private int id=-1;
    private TripTable tripTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_trip);
        getIntentToEditTrip();

        // TODO her you have to call addTripToRoom method to store trip data in room
        binding.addTripBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.addTripBtn.getText().equals("Edit")) {
                    Toast.makeText(AddTripActivity.this, "bla bla", Toast.LENGTH_SHORT).show();
                    editTripFromUI();
                } else {
                    addTripToRoom("Esraa Fathy",
                            "01:43 AA",
                            "01-02-2021",
                            "comming",
                            "Repeated Monthly",
                            true,
                            "Egypt",
                            "Italy");
                }
            }
        });


    }

    private void editTripFromUI() {
        String repetation = "";
        // TODO her you have to call editTripInRoom method to edit trip data in room
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
        boolean way = true;
        switch (binding.tripType.getSelectedItemPosition()) {
            case 0:
                way = true;
                break;
            case 1:
                way = false;
        }

        editTripInRoom(binding.tripNameInput.getText().toString(),
                binding.timeTextView.getText().toString(), binding.dateTextView.getText().toString(), tripTable.getStatus(),
                repetation, way,
                binding.startPointSearchView.getText().toString(),
                binding.endPointSearchView.getText().toString());
    }

    private void editTripInRoom(String title, String time, String date, String status, String repetition, boolean ways, String from, String to) {
        if (title.equals("") || time.equals("") || date.equals("") || repetition.equals("") || from.equals("") || to.equals("")) {
            Toast.makeText(this, "Their is some data missed", Toast.LENGTH_SHORT).show();

        } else {
            TripViewModel tripViewModel;
            TripTable table = new TripTable(title, time, date, status, repetition, ways, from, to, "");
            tripViewModel = new ViewModelProvider(AddTripActivity.this, ViewModelProvider.AndroidViewModelFactory.getInstance(AddTripActivity.this.getApplication())).get(TripViewModel.class);
            table.setId(id);
            tripViewModel.update(table);
        }
    }

    private void addTripToRoom(String title, String time, String date, String status, String repetition, boolean ways, String from, String to) {
        if (title.equals("") || time.equals("") || date.equals("") || repetition.equals("") || from.equals("") || to.equals("")) {
            Toast.makeText(this, "Their is some data missed", Toast.LENGTH_SHORT).show();

        } else {
            TripViewModel tripViewModel;
            tripViewModel = new ViewModelProvider(AddTripActivity.this, ViewModelProvider.AndroidViewModelFactory.getInstance(AddTripActivity.this.getApplication())).get(TripViewModel.class);
            tripViewModel.insert(new TripTable(title, time, date, status,
                    repetition, ways, from, to, ""));
        }
    }

    @SuppressLint("SetTextI18n")
    private void getIntentToEditTrip() {
        Intent intent = getIntent();
        if (intent.getStringExtra("key") != null) {
            binding.addTripBtn.setText("Edit");
            id = intent.getIntExtra(HomeFragment.NOTE_INTENT_ID, -1);
            Toast.makeText(this, "id = "+id, Toast.LENGTH_SHORT).show();
            tripTable = new TripTable(
                    intent.getStringExtra(HomeFragment.NOTE_INTENT_title),
                    intent.getStringExtra(HomeFragment.NOTE_INTENT_time),
                    intent.getStringExtra(HomeFragment.NOTE_INTENT_date),
                    intent.getStringExtra(HomeFragment.NOTE_INTENT_status),
                    intent.getStringExtra(HomeFragment.NOTE_INTENT_repetition),
                    intent.getBooleanExtra(HomeFragment.NOTE_INTENT_ways, true),
                    intent.getStringExtra(HomeFragment.NOTE_INTENT_FROM),
                    intent.getStringExtra(HomeFragment.NOTE_INTENT_to),
                    intent.getStringExtra(HomeFragment.NOTE_INTENT_Note));
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
//                <item>No Repeated</item>
//        <item>Repeated Daily</item>
//        <item>Repeated weekly</item>
//        <item>Repeated Monthly</item>
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

//            <string-array name="trip_options">
//        <item>One Way Trip</item>
//        <item>Rounded Trip</item>

        if (tripTable.getWays())
            binding.tripType.setSelection(0);
        else
            binding.tripType.setSelection(1);

    }
}