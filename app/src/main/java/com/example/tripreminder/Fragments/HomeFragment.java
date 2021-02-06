package com.example.tripreminder.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.tripreminder.Adapters.RecyclerHomeAdapter;
import com.example.tripreminder.AddTripActivity;
import com.example.tripreminder.MenuItemsForOneElement;
import com.example.tripreminder.NotesControl;
import com.example.tripreminder.R;
import com.example.tripreminder.RoomDataBase.TripTable;
import com.example.tripreminder.RoomDataBase.TripViewModel;
import com.example.tripreminder.serveses.FloatingViewService;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment {

    private TripViewModel tripViewModel;
    private RecyclerView recyclerView;
    private RecyclerHomeAdapter recyclerHomeAdapter;
    private List<TripTable> trips = new ArrayList<>();
    public static final String NOTE_INTENT_ID = "id";
    public static final String NOTE_INTENT_title = "title";
    public static final String NOTE_INTENT_time = "time";
    public static final String NOTE_INTENT_date = "date";
    public static final String NOTE_INTENT_status = "status";
    public static final String NOTE_INTENT_repetition = "repetition";
    public static final String NOTE_INTENT_ways = "ways";
    public static final String NOTE_INTENT_to = "to";
    public static final String NOTE_INTENT_Note = "notes";
    public static final String NOTE_INTENT_FROM = "from";
    public static final String DISTANCE = "DISTANCE";
    public static final String LatStart = "LatStart";
    public static final String LongStart = "LongStart";
    public static final String LatEnd = "LatEnd";
    public static final String LongEnd = "LongEnd";
//    public static Ringtone rigntone;
//    public static final String START_SERVICE = "com.example.tripreminder.StartService";
//    public static final String SNOOZE_SERVICE = "com.example.tripreminder.SnoozeService";
//    public static final String CANCEL_SERVICE = "com.example.tripreminder.CancelService";

    private final String SOURCE_URL = "http://maps.google.com/maps?saddr=";
    private final String DEST_URL = "http://maps.google.com/maps?daddr=";
    private int idT;
    Intent mapIntent;
    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            startFloatingIcon(note);
            return false;
        }
    });


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = view.findViewById(R.id.recyclerviewHome);
        ImageView imageView = view.findViewById(R.id.addTrip);
        recyclerHomeAdapter = new RecyclerHomeAdapter(getActivity());


        imageView.setOnClickListener(v -> startActivity(new Intent(getActivity(), AddTripActivity.class)));

        tripViewModel = new ViewModelProvider(getActivity(), ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication())).get(TripViewModel.class);
        tripViewModel.getAllTrips().observe(getActivity(), tripTables -> {
            trips = tripTables;
            recyclerHomeAdapter.setTrips(trips);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(recyclerHomeAdapter);
        });

        recyclerHomeAdapter.OnItemClickListener((type, tripTable) -> {
//            Toast.makeText(getActivity(), "id= " + type, Toast.LENGTH_SHORT).show();
            switch (type) {
                case RecyclerHomeAdapter.MENU:
                    menueItemOptions(tripTable);
                    break;
                case RecyclerHomeAdapter.NOTES:

                    notesItemOptions(tripTable);
                    break;
                case RecyclerHomeAdapter.START:
                    startItemOptions(tripTable);
                    break;
            }

        });

        return view;
    }

    private void startItemOptions(TripTable tripTable) {
        roundedTrip(tripTable);
        idT = tripTable.getId();
        UpdateStatusByID(idT, "Done");
        startTrip(tripTable);

    }

    private void notesItemOptions(TripTable tripTable) {
        Intent intent = new Intent(getActivity(), NotesControl.class);
        intent.putExtra(NOTE_INTENT_ID, tripTable.getId());
        intent.putExtra(NOTE_INTENT_title, tripTable.getTitle());
        intent.putExtra(NOTE_INTENT_date, tripTable.getDate());
        intent.putExtra(NOTE_INTENT_time, tripTable.getTime());
        intent.putExtra(NOTE_INTENT_status, tripTable.getStatus());
        intent.putExtra(NOTE_INTENT_repetition, tripTable.getRepetition());
        intent.putExtra(NOTE_INTENT_ways, tripTable.getWays());
        intent.putExtra(NOTE_INTENT_to, tripTable.getTo());
        intent.putExtra(NOTE_INTENT_FROM, tripTable.getFrom());
        intent.putExtra(NOTE_INTENT_Note, tripTable.getNotes());
        intent.putExtra(DISTANCE, tripTable.getDistance());
        intent.putExtra(LatStart, tripTable.getLatStart());
        intent.putExtra(LongStart, tripTable.getLongStart());
        intent.putExtra(LatEnd, tripTable.getLatEnd());
        intent.putExtra(LongEnd, tripTable.getLatEnd());
        startActivity(intent);
    }

    private void menueItemOptions(TripTable tripTable) {
        Intent intent = new Intent(getActivity(), MenuItemsForOneElement.class);
        intent.putExtra(NOTE_INTENT_ID, tripTable.getId());
//        Toast.makeText(getActivity(), "" + tripTable.getId(), Toast.LENGTH_SHORT).show();
        intent.putExtra(NOTE_INTENT_title, tripTable.getTitle());
        intent.putExtra(NOTE_INTENT_date, tripTable.getDate());
        intent.putExtra(NOTE_INTENT_time, tripTable.getTime());
        intent.putExtra(NOTE_INTENT_status, tripTable.getStatus());
        intent.putExtra(NOTE_INTENT_repetition, tripTable.getRepetition());
        intent.putExtra(NOTE_INTENT_ways, tripTable.getWays());
        intent.putExtra(NOTE_INTENT_to, tripTable.getTo());
        intent.putExtra(NOTE_INTENT_FROM, tripTable.getFrom());
        intent.putExtra(NOTE_INTENT_Note, tripTable.getNotes());
        intent.putExtra(DISTANCE, tripTable.getDistance());


        startActivity(intent);
    }

    private void startTrip(TripTable tripTable) {

//        Toast.makeText(getContext(), "start", Toast.LENGTH_SHORT).show();
        double sourceLat, sourceLon, destinationLat, destinationLon;
        String sourceName, destinationName;


        destinationLat = tripTable.getLatEnd();
        destinationLon = tripTable.getLongEnd();
        destinationName = tripTable.getTo();
        String my_data;

        if (!tripTable.getFrom().equals("null")) { // source exists
            Log.i("log", "not null notif");
            sourceLat = tripTable.getLatStart();
            sourceLon = tripTable.getLongStart();
            sourceName = tripTable.getFrom();


            my_data = String.format(Locale.ENGLISH, SOURCE_URL + sourceLat + "," + sourceLon + "(" + sourceName + ")&daddr=" + destinationLat + "," + destinationLon + "(" + destinationName + ")");
            mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(my_data));
            mapIntent.setPackage("com.google.android.apps.maps");
            mapIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } else {
            my_data = String.format(Locale.ENGLISH, DEST_URL + destinationLat + "," + destinationLon + "(" + destinationName + ")");
            mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(my_data));
            mapIntent.setPackage("com.google.android.apps.maps");
            mapIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            System.out.println("null source");
        }
        GetNotes(idT);

        ////Todo
        startActivity(mapIntent);
        //Todo: delete this trip
    }

    private void startFloatingIcon(String notes) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            getActivity().startService(new Intent(getActivity(), FloatingViewService.class).putExtra("Notes", notes));
        } else if (Settings.canDrawOverlays(getContext())) {
            getActivity().startService(new Intent(getActivity(), FloatingViewService.class).putExtra("Notes", notes));
        } else {
            Toast.makeText(getActivity(), "You need System Alert Window Permission to do this", Toast.LENGTH_SHORT).show();
        }
    }

    private void UpdateStatusByID(int idT, String status) {
        new Thread() {
            @Override
            public void run() {
                TripTable table1 = tripViewModel.getTripRowById(idT);
                table1.setStatus(status);
                table1.setId(idT);
                tripViewModel.update(table1);
            }
        }.start();

    }

    String note;

    private void GetNotes(int id) {
        tripViewModel.getNotes(id).observe(getActivity(), s -> {
            note = s;
            Log.d("TAG of notes", "" + note);
            handler.sendEmptyMessage(0);
        });


    }

    private void roundedTrip(TripTable tripTable) {
        Log.d("TAG", "roundedTrip: " + tripTable.getWays());
        if (!tripTable.getWays()) {
            Log.d("TAG", "roundedTrip: " + tripTable.getWays());
            new Thread(() -> tripViewModel.insert(new TripTable(tripTable.getTitle(),
                    tripTable.getTime(),
                    tripTable.getDate(),
                    "up Coming",
                    tripTable.getRepetition(),
                    true,
                    tripTable.getTo(),
                    tripTable.getFrom(),
                    tripTable.getNotes(),
                    tripTable.getDistance(),
                    tripTable.getLatEnd(),
                    tripTable.getLongEnd(),
                    tripTable.getLatStart(),
                    tripTable.getLatStart()))).start();

        }

    }

}