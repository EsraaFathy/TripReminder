package com.example.tripreminder.RoomDataBase;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = TripTable.class, version = 1)
public abstract class TripRoomDataBase extends RoomDatabase {

    private static TripRoomDataBase instance;

    public abstract TripDAO tripDao();

    public static synchronized TripRoomDataBase getInstance(Context context) {
        if (instance==null){
            instance= Room.databaseBuilder(context.getApplicationContext(),
                    TripRoomDataBase.class,"trip-database").
                    fallbackToDestructiveMigration().build();
        }
        return instance;
    }
}
