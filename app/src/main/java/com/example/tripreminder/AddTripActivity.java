package com.example.tripreminder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import com.example.tripreminder.RoomDataBase.TripTable;
import com.example.tripreminder.RoomDataBase.TripViewModel;

public class AddTripActivity extends AppCompatActivity {
    private TripViewModel tripViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);
        // TODO: this used to insert data in to room database
        tripViewModel= new ViewModelProvider(AddTripActivity.this, ViewModelProvider.AndroidViewModelFactory.getInstance(AddTripActivity.this.getApplication())).get(TripViewModel.class);
        tripViewModel.insert(new TripTable( "Added two", "01:33", "31/1/2021", false, "1", false, "zag", "italy", ""));

    }
}