package com.example.tripreminder.Fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.tripreminder.Adapters.HistoryAdapter;
import com.example.tripreminder.R;
import com.example.tripreminder.RoomDataBase.TripTable;
import com.example.tripreminder.RoomDataBase.TripViewModel;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
public class HistoryFragment extends Fragment {
    private RecyclerView recyclerView;
    private HistoryAdapter historyAdapter;


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
      // List<TripTable> list = new ArrayList<>();
        historyAdapter=new HistoryAdapter(getContext());
        returnHistory();
        historyAdapter.OnItemClickListener((type, tripTable) -> {
            if (type.equals(HistoryAdapter.DELETE)) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setCancelable(false);
                builder.setMessage("Are you sure?");
                builder.setPositiveButton("Yes", (dialogInterface, i) -> {
                    deleteFromRoom(tripTable);
                    dialogInterface.dismiss();
                });
                builder.setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss());
                final AlertDialog dialog = builder.create();
                dialog.show();
                dialog.getButton(dialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrim));
                dialog.getButton(dialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrim));
            }
        });

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

    private void returnHistory() {
        TripViewModel tripViewModel = new ViewModelProvider(getActivity(), ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication())).get(TripViewModel.class);


        tripViewModel.getHistory("up Coming").observe(getActivity(), tripTables -> {
            historyAdapter.setItems(tripTables);
            recyclerView.setAdapter(historyAdapter);
        });
    }

}