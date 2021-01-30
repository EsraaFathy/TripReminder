package com.example.tripreminder.Fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tripreminder.Adapters.RecyclerHomeAdapter;
import com.example.tripreminder.R;
import com.example.tripreminder.RoomDataBase.TripTable;
import com.example.tripreminder.model.Trip;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {


private RecyclerView recyclerView;
private RecyclerHomeAdapter recyclerHomeAdapter;
private List<Trip> trips=new ArrayList<>();
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
        View view=inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView=view.findViewById(R.id.recyclerviewHome);
        trips.add(new Trip(1,"A","00:00","11/1/2010",true,"1",true,"her","their",""));
        trips.add(new Trip(1,"B","00:00","11/1/2010",true,"1",true,"her","their",""));
        trips.add(new Trip(1,"C","00:00","11/1/2010",true,"1",true,"her","their",""));
        trips.add(new Trip(1,"D","00:00","11/1/2010",true,"1",true,"her","their",""));
        recyclerHomeAdapter=new RecyclerHomeAdapter(getActivity(),trips);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(recyclerHomeAdapter);
        return view;
    }
}