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
import android.widget.Toast;

import com.example.tripreminder.Adapters.RecyclerHomeAdapter;
import com.example.tripreminder.MenuItemsForOneElement;
import com.example.tripreminder.NotesControl;
import com.example.tripreminder.R;
import com.example.tripreminder.RoomDataBase.TripTable;
import com.example.tripreminder.RoomDataBase.TripViewModel;
import com.example.tripreminder.model.Trip;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private TripViewModel tripViewModel;
    private RecyclerView recyclerView;
    private RecyclerHomeAdapter recyclerHomeAdapter;
    private List<TripTable> trips = new ArrayList<>();
    public static final String NOTE_INTENT="notes";


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
        recyclerHomeAdapter = new RecyclerHomeAdapter(getActivity());


        tripViewModel= new ViewModelProvider(getActivity(),ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication())).get(TripViewModel.class);
        tripViewModel.getAllTrips().observe(getActivity(), new Observer<List<TripTable>>() {
            @Override
            public void onChanged(List<TripTable> tripTables) {
                trips=tripTables;
                recyclerHomeAdapter.setTrips(trips);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


            }
        });

        recyclerView.setAdapter(recyclerHomeAdapter);

        recyclerHomeAdapter.OnItemClickListener(new RecyclerHomeAdapter.OcCLickListenerAble() {
            @Override
            public void onItemClick(String type, TripTable tripTable) {
                Toast.makeText(getActivity(), "id= " + type, Toast.LENGTH_SHORT).show();
                if (type.equals(RecyclerHomeAdapter.MENU)){
                    menueItemOptions();
                }else if (type.equals(RecyclerHomeAdapter.NOTES)){

                    notesItemOptions(tripTable);
                }else if (type.equals(RecyclerHomeAdapter.START)){
                    startItemOptions();
                }

            }
        });

        return view;
    }

    private void startItemOptions() {

    }

    private void notesItemOptions(TripTable tripTable) {
        Intent intent=new Intent(getActivity(), NotesControl.class);
        Bundle bundle=new Bundle();
        //bundle.putInt(tripTable);
        intent.putExtra(NOTE_INTENT, tripTable.getId());
        startActivity(intent);
    }

    private void menueItemOptions() {
        Intent intent=new Intent(getActivity(), MenuItemsForOneElement.class);
        Bundle bundle=new Bundle();
        //bundle.putInt(tripTable);
       // intent.putExtra(NOTE_INTENT, tripTable.getId());
        startActivity(intent);
    }
}