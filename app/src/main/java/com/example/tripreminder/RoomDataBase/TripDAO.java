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

    @Query("SELECT * FROM trips WHERE status LIKE '%' || :Status || '%'")
    LiveData<List<TripTable>> getAllHomeTrips(String Status);

    @Query("SELECT * FROM trips WHERE status not LIKE '%' || :UpComming || '%'")
    LiveData<List<TripTable>> getHistory(String UpComming);

    @Query("SELECT * FROM trips")
    LiveData<List<TripTable>> getAllToAsync();

    @Query("SELECT notes FROM trips WHERE  id = :id LIMIT 1")
    LiveData<String> getNote(int id);

    @Query("SELECT * FROM trips WHERE  id = :id LIMIT 1")
    TripTable getTripTableById(long id);
}

