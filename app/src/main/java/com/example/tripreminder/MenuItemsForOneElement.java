package com.example.tripreminder;

import android.content.Intent;
import android.os.Bundle;

import com.example.tripreminder.Fragments.HomeFragment;
import com.example.tripreminder.RoomDataBase.TripTable;
import com.example.tripreminder.RoomDataBase.TripViewModel;
import com.example.tripreminder.databinding.ActivityMenuItemsForOneElementBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.installations.Utils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.view.View;
import android.widget.Toast;

public class MenuItemsForOneElement extends AppCompatActivity {
    ActivityMenuItemsForOneElementBinding binding;
    TripTable tripTable;
    int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView();
        binding= DataBindingUtil.setContentView(this,R.layout.activity_menu_items_for_one_element);

        Intent intent=getIntent();
         id = intent.getIntExtra(HomeFragment.NOTE_INTENT_ID,-1);
         tripTable=new TripTable(
                intent.getStringExtra(HomeFragment.NOTE_INTENT_title),
                intent.getStringExtra(HomeFragment.NOTE_INTENT_time),
                intent.getStringExtra(HomeFragment.NOTE_INTENT_date),
                intent.getStringExtra(HomeFragment.NOTE_INTENT_status),
                intent.getStringExtra(HomeFragment.NOTE_INTENT_repetition),
                intent.getBooleanExtra(HomeFragment.NOTE_INTENT_ways,true),
                intent.getStringExtra(HomeFragment.NOTE_INTENT_FROM),
                intent.getStringExtra(HomeFragment.NOTE_INTENT_to),
                intent.getStringExtra(HomeFragment.NOTE_INTENT_Note));

        binding.AllNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO i dont know
            }
        });
        binding.editTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToEditTrip();
            }
        });
        binding.CancelTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                caancelTrip(tripTable);
                finish();
            }
        });
        binding.DeleteTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteTrip(tripTable);
                finish();
            }
        });
    }

    private void deleteTrip(TripTable tripTable) {
        if (id!=-1){
            TripViewModel tripViewModel;
            tripViewModel = new ViewModelProvider(MenuItemsForOneElement.this, ViewModelProvider.AndroidViewModelFactory.getInstance(MenuItemsForOneElement.this.getApplication())).get(TripViewModel.class);
            tripTable.setId(id);
            tripViewModel.delete(tripTable);
        }
    }

    private void caancelTrip(TripTable tripTable) {
        if (id!=-1){
            TripViewModel tripViewModel;
            tripViewModel = new ViewModelProvider(MenuItemsForOneElement.this, ViewModelProvider.AndroidViewModelFactory.getInstance(MenuItemsForOneElement.this.getApplication())).get(TripViewModel.class);
            tripTable.setId(id);
            tripTable.setStatus("Canceled");
            tripViewModel.update(tripTable);
        }
    }

    private void moveToEditTrip(){
        Intent intent = new Intent(this, AddTripActivity.class);
        intent.putExtra("key","edit");
        intent.putExtra(HomeFragment.NOTE_INTENT_ID, id);
        Toast.makeText(this, ""+id, Toast.LENGTH_SHORT).show();
        intent.putExtra(HomeFragment.NOTE_INTENT_title, tripTable.getTitle());
        intent.putExtra(HomeFragment.NOTE_INTENT_date, tripTable.getDate());
        intent.putExtra(HomeFragment.NOTE_INTENT_time, tripTable.getTime());
        intent.putExtra(HomeFragment.NOTE_INTENT_status, tripTable.getStatus());
        intent.putExtra(HomeFragment.NOTE_INTENT_repetition, tripTable.getRepetition());
        intent.putExtra(HomeFragment.NOTE_INTENT_ways, tripTable.getWays());
        intent.putExtra(HomeFragment.NOTE_INTENT_to, tripTable.getTo());
        intent.putExtra(HomeFragment.NOTE_INTENT_FROM, tripTable.getFrom());
        intent.putExtra(HomeFragment.NOTE_INTENT_Note, tripTable.getNotes());
        startActivity(intent);
    }
}