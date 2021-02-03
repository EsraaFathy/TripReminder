package com.example.tripreminder;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.util.StringUtil;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.tripreminder.Adapters.NoteAdapter;
import com.example.tripreminder.Fragments.HomeFragment;
import com.example.tripreminder.RoomDataBase.TripTable;
import com.example.tripreminder.RoomDataBase.TripViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NotesControl extends AppCompatActivity {
    private TripViewModel tripViewModel;
    RecyclerView recyclerView;
    ImageView addNote;
    NoteAdapter noteAdapter;
    TripTable tripTable;
    String notes;
    String newNote;
    List<String> tripNotes = new ArrayList<>();
    private ImageView back;

    int id = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_control);
        back=findViewById(R.id.backNote);
        getTripIntent();
        recyclerViewPopulating();
        tripViewModel = new ViewModelProvider(NotesControl.this, ViewModelProvider.AndroidViewModelFactory.getInstance(NotesControl.this.getApplication())).get(TripViewModel.class);
        tripViewModel.getNotes(id).observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                tripNotes.clear();
                notes = s;
                if (notes.equals("no Notes yet") || notes.equals("")) {
                    recyclerView.setVisibility(View.INVISIBLE);
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                    splitNotes(notes);
                    noteAdapter.setNotesList(tripNotes);
                    recyclerView.setAdapter(noteAdapter);
                }
            }
        });
        editNotePopulating();
        upDateToRoom(tripTable, id);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    private void editNotePopulating() {
        addNote = findViewById(R.id.addNote);
        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialgImplementation();
            }
        });

    }

    private void dialgImplementation() {
        AlertDialog alertDialog = new AlertDialog.Builder(NotesControl.this).create();
        final EditText input = new EditText(NotesControl.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);
        alertDialog.setTitle("Write Your Note");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        newNote = input.getText().toString();
                        if (notes.equals("no Notes yet") || notes.equals("")) {
                            notes = newNote;
                        } else {
                            notes = notes + "#" + newNote;
                        }
                        updateNotesInRoom(tripTable, notes);
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    private void getTripIntent() {
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
                intent.getStringExtra(HomeFragment.NOTE_INTENT_Note));
        notes = intent.getStringExtra(HomeFragment.NOTE_INTENT_Note);
    }

    private void recyclerViewPopulating() {
        recyclerView = findViewById(R.id.recyclerViewNotes);
        noteAdapter = new NoteAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        noteAdapter.OnItemClickListener(new NoteAdapter.SetOnClickListener() {
            @Override
            public void onClickListener(int position, String viewType) {
                // TODO switch to definnd the view
                //deleteNote
                if (viewType.equals("delete")) {
                    Toast.makeText(NotesControl.this, "deledte", Toast.LENGTH_SHORT).show();
                    deleteNote(position);
                    updateNotesInRoom(tripTable, notes);
                }
            }
        });
    }

    private void deleteNote(int position) {
        tripNotes.remove(position);
        notes = "no Notes yet";
        for (int i = 0; i < tripNotes.size(); i++) {

            if (!tripNotes.get(i).equals("no Notes yet")) {
                if (i == 0)
                    notes = tripNotes.get(i);
                else
                    notes = notes + "#" + tripNotes.get(i);
            }

        }
        updateNotesInRoom(tripTable, notes);
        splitNotes(notes);
        recyclerView.removeAllViews();
        noteAdapter.setNotesList(tripNotes);
        recyclerView.setAdapter(noteAdapter);

    }

    private void upDateToRoom(TripTable tripTable, int id) {
        if (id != -1) {
            tripTable.setId(id);
            tripViewModel.update(tripTable);
        }
    }

    private void splitNotes(String notes) {
        String[] strings;
        strings = notes.split("#");
        tripNotes.clear();
        tripNotes.addAll(Arrays.asList(strings));
    }

    private void updateNotesInRoom(TripTable tripTable, String notes) {
        tripTable.setId(id);
        tripTable.setNotes(notes);
        tripViewModel.update(tripTable);
    }




}

