package com.example.tripreminder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.tripreminder.Fragments.HomeFragment;
import com.example.tripreminder.RoomDataBase.TripTable;
import com.example.tripreminder.RoomDataBase.TripViewModel;

public class NotesControl extends AppCompatActivity {
//    private TripViewModel tripViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_control);
        Intent intent=getIntent();
        int id = intent.getIntExtra(HomeFragment.NOTE_INTENT_ID,-1);
        TripTable tripTable=new TripTable(
                intent.getStringExtra(HomeFragment.NOTE_INTENT_title),
                intent.getStringExtra(HomeFragment.NOTE_INTENT_time),
                intent.getStringExtra(HomeFragment.NOTE_INTENT_date),
                "edited",
                intent.getStringExtra(HomeFragment.NOTE_INTENT_repetition),
                intent.getBooleanExtra(HomeFragment.NOTE_INTENT_ways,true),
                intent.getStringExtra(HomeFragment.NOTE_INTENT_FROM),
                intent.getStringExtra(HomeFragment.NOTE_INTENT_to),
                intent.getStringExtra(HomeFragment.NOTE_INTENT_Note)+"her");
        Toast.makeText(this, "id", Toast.LENGTH_SHORT).show();
        upDateToRoom(tripTable,id);
        Toast.makeText(this, "done", Toast.LENGTH_SHORT).show();


    }

    private void upDateToRoom(TripTable tripTable,int id ) {
        if (id!=-1) {
            TripViewModel tripViewModel;
            tripViewModel = new ViewModelProvider(NotesControl.this, ViewModelProvider.AndroidViewModelFactory.getInstance(NotesControl.this.getApplication())).get(TripViewModel.class);
            tripTable.setId(id);
            tripViewModel.update(tripTable);
        }
    }
}