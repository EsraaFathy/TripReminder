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
    void Insert(TripTable tripTable);

    @Update
    void Update(TripTable tripTable);

    @Delete
    void Delete(TripTable tripTable);

    @Query("DELETE FROM trips")
    void deleteAllRecords();

    @Query("SELECT * FROM trips WHERE status LIKE '%' || :Status || '%'")
    LiveData<List<TripTable>> getAllRecords(String Status);

    @Query("SELECT * FROM trips WHERE status not LIKE '%' || :UpComming || '%'")
    LiveData<List<TripTable>> getHistory(String UpComming);
}
//J
