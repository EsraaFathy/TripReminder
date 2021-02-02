package com.example.tripreminder.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import java.util.ArrayList;
import java.util.List;

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
    private ImageView imageView;
    List<TripTable> tripsRoom;



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
        imageView = view.findViewById(R.id.addTrip);
        recyclerHomeAdapter = new RecyclerHomeAdapter(getActivity());


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddTripActivity.class));
            }
        });

        tripViewModel = new ViewModelProvider(getActivity(), ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication())).get(TripViewModel.class);
        tripViewModel.getAllTrips().observe(getActivity(), new Observer<List<TripTable>>() {
            @Override
            public void onChanged(List<TripTable> tripTables) {
                trips = tripTables;
                recyclerHomeAdapter.setTrips(trips);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerView.setAdapter(recyclerHomeAdapter);
            }
        });
        //saveFromFirebaseToRoom(tripTables);

        recyclerHomeAdapter.OnItemClickListener(new RecyclerHomeAdapter.OcCLickListenerAble() {
            @Override
            public void onItemClick(String type, TripTable tripTable) {
                Toast.makeText(getActivity(), "id= " + type, Toast.LENGTH_SHORT).show();
                if (type.equals(RecyclerHomeAdapter.MENU)) {
                    menueItemOptions(tripTable);
//                    Toast.makeText(getActivity(), ""+tripTable.getId(), Toast.LENGTH_SHORT).show();
                } else if (type.equals(RecyclerHomeAdapter.NOTES)) {

                    notesItemOptions(tripTable);
                } else if (type.equals(RecyclerHomeAdapter.START)) {
                    startItemOptions();
                }

            }
        });

        return view;
    }

    private void startItemOptions() {

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
        startActivity(intent);
    }

    private void menueItemOptions(TripTable tripTable) {
        Intent intent = new Intent(getActivity(), MenuItemsForOneElement.class);
        intent.putExtra(NOTE_INTENT_ID, tripTable.getId());
        Toast.makeText(getActivity(), "" + tripTable.getId(), Toast.LENGTH_SHORT).show();
        intent.putExtra(NOTE_INTENT_title, tripTable.getTitle());
        intent.putExtra(NOTE_INTENT_date, tripTable.getDate());
        intent.putExtra(NOTE_INTENT_time, tripTable.getTime());
        intent.putExtra(NOTE_INTENT_status, tripTable.getStatus());
        intent.putExtra(NOTE_INTENT_repetition, tripTable.getRepetition());
        intent.putExtra(NOTE_INTENT_ways, tripTable.getWays());
        intent.putExtra(NOTE_INTENT_to, tripTable.getTo());
        intent.putExtra(NOTE_INTENT_FROM, tripTable.getFrom());
        intent.putExtra(NOTE_INTENT_Note, tripTable.getNotes());
        startActivity(intent);
    }


    private void saveFromFirebaseToRoom(List<TripTable> trips) {
        tripViewModel.deleteAllTrips();
        for (TripTable table : trips) {
            tripViewModel.insert(table);
        }

    }

    private List<TripTable> saveFromRoomToFirebase() {
        tripViewModel.getAllTrips().observe(getActivity(), new Observer<List<TripTable>>() {
            @Override
            public void onChanged(List<TripTable> tripTables) {
                tripsRoom = tripTables;
            }
        });
        return tripsRoom;
    }


}