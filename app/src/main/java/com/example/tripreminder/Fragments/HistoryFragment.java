package com.example.tripreminder.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

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

    RecyclerView recyclerView;
    HistoryAdapter historyAdapter;
    List<HistoryItem> list;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_history, container, false);
        recyclerView=(RecyclerView)view.findViewById(R.id.History_List);
        list=new ArrayList<>();
        HistoryItem historyItem1=new HistoryItem("tree clup","12:33","9/12/2020",
                "Done","Elmammar","resturant");
        HistoryItem historyItem2=new HistoryItem("tree clup","12:33","9/12/2020",
                "Done","Elmammar","resturant");
        list.add(historyItem1);
        list.add(historyItem2);
        historyAdapter=new HistoryAdapter(list);
        recyclerView.setAdapter(historyAdapter);
        Toast.makeText(getActivity(), "in history fragment", Toast.LENGTH_SHORT).show();

        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



    }
}