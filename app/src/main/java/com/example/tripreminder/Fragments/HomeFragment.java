package com.example.tripreminder.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.tripreminder.Adapters.RecyclerHomeAdapter;
import com.example.tripreminder.R;
import com.example.tripreminder.RoomDataBase.TripTable;
import com.example.tripreminder.RoomDataBase.TripViewModel;
import com.example.tripreminder.model.Trip;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private TripViewModel tripViewModel;
    private RecyclerView recyclerView;
    private RecyclerHomeAdapter recyclerHomeAdapter;
    private List<TripTable> trips = new ArrayList<>();


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
//        trips.add(new Trip(1, "A", "00:00", "11/1/2010", true, "1", true, "her", "their", ""));
//        trips.add(new Trip(1, "B", "00:00", "11/1/2010", true, "1", true, "her", "their", ""));
//        trips.add(new Trip(1, "C", "00:00", "11/1/2010", true, "1", true, "her", "their", ""));
//        trips.add(new Trip(1, "D", "00:00", "11/1/2010", true, "1", true, "her", "their", ""));

//mViewModel = new ViewModelProvider(this,
// ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(MyViewModel.class);
        tripViewModel= new ViewModelProvider(getActivity(),ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication())).get(TripViewModel.class);
        tripViewModel.getAllTrips().observe(getActivity(), new Observer<List<TripTable>>() {
            @Override
            public void onChanged(List<TripTable> tripTables) {
                recyclerHomeAdapter = new RecyclerHomeAdapter(getActivity(), tripTables);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerView.setAdapter(recyclerHomeAdapter);            }
        });


        return view;
    }
}