package com.example.tripreminder.RoomDataBase;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class TripRepository {
    private TripDAO tripDAO;
    private LiveData<List<TripTable>> getAllData;

    public TripRepository(Application application) {
        TripRoomDataBase tripRoomDataBase =TripRoomDataBase.getInstance(application);
        tripDAO=tripRoomDataBase.tripDao();
        getAllData=tripDAO.getAllRecords();
    }

    public void insert(TripTable tripTable){
        new InsertAsyncTask(tripDAO).execute(tripTable);

    }


    public void update(TripTable tripTable){
        new UpDateAsyncTask(tripDAO).execute(tripTable);

    }

    public LiveData<List<TripTable>> getAllRecord(TripTable tripTable){
        return getAllData;
    }

    public void Delete(TripTable tripTable){
        new DeleteAsyncTask(tripDAO).execute(tripTable);
    }

    public void deleteAllRecords(){
        new DeleteAllAsyncTask(tripDAO).execute();
    }

    private static class InsertAsyncTask extends AsyncTask<TripTable,Void,Void>{
        private TripDAO tripDAO;

        public InsertAsyncTask(TripDAO tripDAO) {
            this.tripDAO = tripDAO;
        }

        @Override
        protected Void doInBackground(TripTable... tripTables) {
            tripDAO.Insert(tripTables[0]);
            return null;
        }
    }


    private static class DeleteAsyncTask extends AsyncTask<TripTable,Void,Void>{
        private TripDAO tripDAO;

        public DeleteAsyncTask(TripDAO tripDAO) {
            this.tripDAO = tripDAO;
        }

        @Override
        protected Void doInBackground(TripTable... tripTables) {
            tripDAO.Delete(tripTables[0]);
            return null;
        }
    }



    private static class UpDateAsyncTask extends AsyncTask<TripTable,Void,Void>{
        private TripDAO tripDAO;

        public UpDateAsyncTask(TripDAO tripDAO) {
            this.tripDAO = tripDAO;
        }

        @Override
        protected Void doInBackground(TripTable... tripTables) {
            tripDAO.Update(tripTables[0]);
            return null;
        }
    }


    private static class DeleteAllAsyncTask extends AsyncTask<Void,Void,Void>{
        private TripDAO tripDAO;

        public DeleteAllAsyncTask(TripDAO tripDAO) {
            this.tripDAO = tripDAO;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            tripDAO.deleteAllRecords();
            return null;
        }
    }
}
