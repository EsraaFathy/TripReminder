package com.example.tripreminder.RoomDataBase;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = TripTable.class, version = 1)
public abstract class TripRoomDataBase extends RoomDatabase {

    private static TripRoomDataBase instance;

    public abstract TripDAO tripDao();

    public static synchronized TripRoomDataBase getInstance(Context context) {
        if (instance==null){
            instance= Room.databaseBuilder(context.getApplicationContext(),
                    TripRoomDataBase.class,"trip-database").
                    fallbackToDestructiveMigration()
                    .addCallback(roomCallBack)
                    .build();
        }
        return instance;
    }
//KKNK
    private static RoomDatabase.Callback roomCallBack=new RoomDatabase.Callback()
    {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDataAsyncTask(instance).execute();
        }

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
        }
    };



    private static class PopulateDataAsyncTask extends AsyncTask<Void,Void,Void>{

        private TripDAO tripDAO;

        public PopulateDataAsyncTask(TripRoomDataBase tripRoomDataBase) {
            tripDAO=tripRoomDataBase.tripDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
//            tripDAO.Insert(new TripTable("tiletest","00:00","11/1/2010",true,"1",true,"her","their",""));
//            tripDAO.Insert(new TripTable("tiletest","00:00","11/1/2010",true,"1",true,"her","their",""));
//            tripDAO.Insert(new TripTable("tiletest","00:00","11/1/2010",true,"1",true,"her","their",""));
//            tripDAO.Insert(new TripTable("tiletest","00:00","11/1/2010",true,"1",true,"her","their",""));
//            tripDAO.Insert(new TripTable("tiletest","00:00","11/1/2010",true,"1",true,"her","their",""));
//            tripDAO.Insert(new TripTable("tiletest","00:00","11/1/2010",true,"1",true,"her","their",""));
            return null;
        }
    }
}
