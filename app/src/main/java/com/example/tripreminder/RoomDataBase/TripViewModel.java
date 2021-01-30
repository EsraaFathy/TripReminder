package com.example.tripreminder.RoomDataBase;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class TripViewModel extends AndroidViewModel {
    private TripRepository tripRepository;
    private LiveData<List<TripTable>> listLiveData;


    public TripViewModel(@NonNull Application application) {
        super(application);
        tripRepository=new TripRepository(application);
        listLiveData=tripRepository.getAllRecord();
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

    private LiveData<List<TripTable>>  getAllTrips(){
        return listLiveData;
    }

}
