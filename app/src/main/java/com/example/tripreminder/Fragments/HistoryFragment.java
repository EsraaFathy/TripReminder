package com.example.tripreminder.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tripreminder.Adapters.HistoryAdapter;
import com.example.tripreminder.Adapters.RecyclerHomeAdapter;
import com.example.tripreminder.R;
import com.example.tripreminder.RoomDataBase.TripTable;
import com.example.tripreminder.RoomDataBase.TripViewModel;

import java.util.ArrayList;
import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.tripreminder.Adapters.HistoryAdapter;
import com.example.tripreminder.R;
import com.example.tripreminder.model.HistoryItem;

import java.util.ArrayList;
import java.util.List;


public class HistoryFragment extends Fragment {
    private TripViewModel tripViewModel;
    private List<TripTable> trips = new ArrayList<>();
    RecyclerView recyclerView;
    HistoryAdapter historyAdapter;
    List<TripTable> list;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.History_List);
        list=new ArrayList<>();
        historyAdapter=new HistoryAdapter(getContext());
        list =returnHistory();
        historyAdapter.OnItemClickListener(new HistoryAdapter.OcCLickListenerAble() {
            @Override
            public void onItemClick(String type, TripTable tripTable) {
                if (type.equals(HistoryAdapter.DELETE)) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setCancelable(false);
                    builder.setMessage("Are you sure?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            TripTable historyItem = tripTable;
                            deleteFromRoom(tripTable);
                            dialogInterface.dismiss();
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    final AlertDialog dialog = builder.create();
                    dialog.show();
                    dialog.getButton(dialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrim));
                    dialog.getButton(dialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrim));
                }
            }
        });

        Toast.makeText(getActivity(), "in history fragment", Toast.LENGTH_SHORT).show();

        return view;

    }

    private void deleteFromRoom(TripTable tripTable) {
        if (tripTable.getId() != -1) {
            TripViewModel tripViewModel;
            tripViewModel = new ViewModelProvider(getActivity(), ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication())).get(TripViewModel.class);
            tripTable.setId(tripTable.getId());
            tripViewModel.delete(tripTable);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    private List<TripTable> returnHistory() {
        tripViewModel = new ViewModelProvider(getActivity(), ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication())).get(TripViewModel.class);


        tripViewModel.getHistory("up Coming").observe(getActivity(), new Observer<List<TripTable>>() {
            @Override
            public void onChanged(List<TripTable> tripTables) {
                trips = tripTables;
                historyAdapter.setItems(tripTables);
                recyclerView.setAdapter(historyAdapter);
            }
        });
        return trips;
    }

}