package com.example.tripreminder.RoomDataBase;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class TripViewModel extends AndroidViewModel {
    private TripRepository tripRepository;
    private LiveData<List<TripTable>> listLiveData;
    private LiveData<List<TripTable>> historyList;


    public TripViewModel(@NonNull Application application) {
        super(application);
        tripRepository=new TripRepository(application);
        listLiveData=tripRepository.getAllRecord();
        historyList=tripRepository.getHistory("up Coming");
    }

    public void insert(TripTable tripTable){
        tripRepository.insert(tripTable);
    }
    public void delete(TripTable tripTable){
        tripRepository.delete(tripTable);
    }
    public void update(TripTable tripTable){
        tripRepository.update(tripTable);
    }
    public void deleteAllTrips(){
        tripRepository.deleteAllRecords();
    }

    public LiveData<List<TripTable>>  getAllTrips(){
        return listLiveData;
    }
    public LiveData<List<TripTable>>  getHistory(String upComing){
        return historyList;
    }

}
