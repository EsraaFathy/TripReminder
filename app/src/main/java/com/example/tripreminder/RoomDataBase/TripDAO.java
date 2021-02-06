package com.example.tripreminder.RoomDataBase;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TripDAO {

    @Insert
    long Insert(TripTable tripTable);

    @Update
    void Update(TripTable tripTable);

    @Delete
    void Delete(TripTable tripTable);

    @Query("DELETE FROM trips")
    void deleteAllRecords();

    @Query("SELECT * FROM trips WHERE status LIKE:Status "+ "OR status LIKE :is")
    LiveData<List<TripTable>> getAllHomeTrips(String Status ,String is);

    @Query("SELECT * FROM trips WHERE status LIKE:UpComming " +"OR status LIKE :is")
    LiveData<List<TripTable>> getHistory(String UpComming,String is);

    @Query("SELECT * FROM trips WHERE status LIKE '%' || :done || '%'")
    LiveData<List<TripTable>> getHistoryDone(String done);

    @Query("SELECT * FROM trips")
    LiveData<List<TripTable>> getAllToAsync();

    @Query("SELECT notes FROM trips WHERE  id = :id LIMIT 1")
    LiveData<String> getNote(int id);

    @Query("SELECT * FROM trips WHERE  id = :id LIMIT 1")
    TripTable getTripTableById(long id);

    @Query("SELECT * FROM trips ORDER BY distance")
    List<TripTable> getAllDistance();

    @Query("SELECT status FROM trips WHERE  id = :id LIMIT 1")
    String getStatusById(long id);

}

