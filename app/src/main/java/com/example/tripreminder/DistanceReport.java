package com.example.tripreminder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.tripreminder.Adapters.HistoryAdapter;
import com.example.tripreminder.Adapters.ReportAdapter;
import com.example.tripreminder.RoomDataBase.TripTable;
import com.example.tripreminder.RoomDataBase.TripViewModel;

import java.util.ArrayList;
import java.util.List;

public class DistanceReport extends AppCompatActivity {
    private TripViewModel tripViewModel;
    private List<TripTable> trips = new ArrayList<>();
    RecyclerView recyclerView;
    ReportAdapter reportAdapter;
    List<TripTable> list;

    private Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            reportAdapter.setItems(list);
            recyclerView.setAdapter(reportAdapter);
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distance_report);
        tripViewModel = new ViewModelProvider(DistanceReport.this, ViewModelProvider.AndroidViewModelFactory.getInstance(DistanceReport.this.getApplication())).get(TripViewModel.class);

        recyclerView = findViewById(R.id.Report_List);
        list=new ArrayList<>();
        reportAdapter=new ReportAdapter(this);
        getDistanceReport();
    }

    private void getDistanceReport(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                list=tripViewModel.getTitleDistance();
                handler.sendEmptyMessage(0);

            }
        }).start();
    }

}