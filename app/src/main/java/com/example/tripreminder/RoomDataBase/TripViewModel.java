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
    private LiveData<List<TripTable>> historyLisDonet;
    private LiveData<List<TripTable>> getAllAsync;

    public TripViewModel(@NonNull Application application) {
        super(application);
        tripRepository=new TripRepository(application);
        listLiveData=tripRepository.getAllRecord();
        historyList=tripRepository.getHistory("up Coming");
        getAllAsync=tripRepository.getAllToSync();
        historyLisDonet=tripRepository.getHistoryDone("Done");
    }
    public String getStatusById(long id){
        return tripRepository.getStatTusById(id);
    }

    public List<TripTable> getTitleDistance(){
        return tripRepository.getTitleDistance();
    }
    public TripTable getTripRowById(long id){
        return tripRepository.getTripTableById(id);
    }
    public LiveData<String> getNotes(int idQuary){
        return tripRepository.getNotes(idQuary);
    }
    public long insert(TripTable tripTable){
        return tripRepository.insert(tripTable);
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
    public LiveData<List<TripTable>>  getHistoryDone(String upComing){
        return historyLisDonet;
    }
    public LiveData<List<TripTable>> getGetAllAsync(){
        return getAllAsync;
    }

}
