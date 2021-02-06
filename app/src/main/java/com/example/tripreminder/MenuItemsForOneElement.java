package com.example.tripreminder;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.example.tripreminder.Fragments.HomeFragment;
import com.example.tripreminder.RoomDataBase.TripTable;
import com.example.tripreminder.RoomDataBase.TripViewModel;
import com.example.tripreminder.databinding.ActivityMenuItemsForOneElementBinding;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MenuItemsForOneElement extends AppCompatActivity {
    private ActivityMenuItemsForOneElementBinding binding;
    private TripTable tripTable;
    private TripViewModel tripViewModel;
    private String[] stringList;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_menu_items_for_one_element);
        tripViewModel = new ViewModelProvider(MenuItemsForOneElement.this, ViewModelProvider.AndroidViewModelFactory.getInstance(MenuItemsForOneElement.this.getApplication())).get(TripViewModel.class);
        AlertDialog alertDialog = new AlertDialog.Builder(MenuItemsForOneElement.this).create();

        Intent intent = getIntent();
        id = intent.getIntExtra(HomeFragment.NOTE_INTENT_ID, -1);
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
        setTitle(intent.getStringExtra(HomeFragment.NOTE_INTENT_title) + " trip");
        binding.AllNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAllNoes(id);

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
                alertDialog.setTitle("Are you sure you want to cancel the trip");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                caancelTrip(tripTable);
                                dialog.cancel();
                                finish();
                            }});
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                finish();
                            }
                        });
                alertDialog.show();

            }
        });
        binding.DeleteTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.setTitle("Are you sure you want to delete the trip");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                deleteTrip(tripTable);
                                dialog.dismiss();
                                finish();
                            }});
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                finish();
                            }
                        });
                alertDialog.show();

            }
        });
    }

    private void deleteTrip(TripTable tripTable) {
        if (id != -1) {
            TripViewModel tripViewModel;
            tripViewModel = new ViewModelProvider(MenuItemsForOneElement.this, ViewModelProvider.AndroidViewModelFactory.getInstance(MenuItemsForOneElement.this.getApplication())).get(TripViewModel.class);
            tripTable.setId(id);
            tripViewModel.delete(tripTable);
        }
    }

    private void caancelTrip(TripTable tripTable) {
        if (id != -1) {
            TripViewModel tripViewModel;
            tripViewModel = new ViewModelProvider(MenuItemsForOneElement.this, ViewModelProvider.AndroidViewModelFactory.getInstance(MenuItemsForOneElement.this.getApplication())).get(TripViewModel.class);
            tripTable.setId(id);
            tripTable.setStatus("Canceled");
            tripViewModel.update(tripTable);
        }
    }

    private void moveToEditTrip() {
        Intent intent = new Intent(this, AddTripActivity.class);
        intent.putExtra("key", "edit");
        intent.putExtra(HomeFragment.NOTE_INTENT_ID, id);
        Toast.makeText(this, "" + id, Toast.LENGTH_SHORT).show();
        intent.putExtra(HomeFragment.NOTE_INTENT_title, tripTable.getTitle());
        intent.putExtra(HomeFragment.NOTE_INTENT_date, tripTable.getDate());
        intent.putExtra(HomeFragment.NOTE_INTENT_time, tripTable.getTime());
        intent.putExtra(HomeFragment.NOTE_INTENT_status, tripTable.getStatus());
        intent.putExtra(HomeFragment.NOTE_INTENT_repetition, tripTable.getRepetition());
        intent.putExtra(HomeFragment.NOTE_INTENT_ways, tripTable.getWays());
        intent.putExtra(HomeFragment.NOTE_INTENT_to, tripTable.getTo());
        intent.putExtra(HomeFragment.NOTE_INTENT_FROM, tripTable.getFrom());
        intent.putExtra(HomeFragment.NOTE_INTENT_Note, tripTable.getNotes());
        intent.putExtra(HomeFragment.LatStart, tripTable.getLatStart());
        intent.putExtra(HomeFragment.LongStart, tripTable.getLongStart());
        intent.putExtra(HomeFragment.LatEnd, tripTable.getLatEnd());
        intent.putExtra(HomeFragment.LongEnd, tripTable.getLatEnd());
        startActivity(intent);
    }
    // TODO used in float icon

    private String[] getAllNoes(int id) {

        tripViewModel.getNotes(id).observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                stringList = s.split("#");
                AlertDialog alertDialog = new AlertDialog.Builder(MenuItemsForOneElement.this).create();
                final TextView n = new TextView(MenuItemsForOneElement.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                n.setLayoutParams(lp);
                alertDialog.setView(n);
                for (String value : stringList) {
                    n.append("   " + value + "\n");
                }
                alertDialog.setTitle("Your Notes");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        });
        return stringList;
    }

    private void showDialog() {

    }
}