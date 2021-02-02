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

import com.example.tripreminder.Adapters.HistoryAdapter;
import com.example.tripreminder.Adapters.RecyclerHomeAdapter;
import com.example.tripreminder.R;
import com.example.tripreminder.RoomDataBase.TripTable;
import com.example.tripreminder.RoomDataBase.TripViewModel;

import java.util.ArrayList;
import java.util.List;


public class HistoryFragment extends Fragment {
    private TripViewModel tripViewModel;
    private RecyclerView recyclerView;
    private HistoryAdapter historyAdapter;
    private List<TripTable> trips = new ArrayList<>();


    public HistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_history, container, false);
        recyclerView = view.findViewById(R.id.recyclerviewHistory);
        historyAdapter = new HistoryAdapter();
        tripViewModel = new ViewModelProvider(getActivity(), ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication())).get(TripViewModel.class);
        tripViewModel.getHistory("up Coming").observe(getActivity(), new Observer<List<TripTable>>() {
            @Override
            public void onChanged(List<TripTable> tripTables) {
                trips = tripTables;
                historyAdapter.setTrips(trips);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            }
        });
        //saveFromFirebaseToRoom(tripTables);
        recyclerView.setAdapter(historyAdapter);
        return view;
    }
}