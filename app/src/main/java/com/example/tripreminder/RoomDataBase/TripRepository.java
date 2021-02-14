package com.example.tripreminder.RoomDataBase;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class TripRepository {
    private static TripDAO tripDAO;
    private final LiveData<List<TripTable>> getAllData;
    private final LiveData<List<TripTable>> history;
    private final LiveData<List<TripTable>> historydone;
    private static LiveData<List<TripTable>> allToSync;


    public TripRepository(Application application) {
        TripRoomDataBase tripRoomDataBase = TripRoomDataBase.getInstance(application);
        tripDAO = tripRoomDataBase.tripDao();
        getAllData = tripDAO.getAllHomeTrips("up Coming","Second Way");
        history = tripDAO.getHistory("Canceled","Done");
        allToSync = tripDAO.getAllToAsync();
        historydone=tripDAO.getHistoryDone("Done");
    }

    public String getStatTusById(long id){
        return tripDAO.getStatusById(id);
    }

    public List<TripTable> getTitleDistance(){
        return tripDAO.getAllDistance();
    }
    public TripTable getTripTableById(long l){
        return tripDAO.getTripTableById(l);
    }

    public long insert(TripTable tripTable) {
        return tripDAO.Insert(tripTable);
    }
    public void insertAll(List<TripTable> tripTable) {
//         tripDAO.insertAll(tripTable);
        new InsertAllAsyncTask(tripDAO).execute(tripTable);
    }

    public void update(TripTable tripTable) {
        new UpDateAsyncTask(tripDAO).execute(tripTable);

    }

    public LiveData<String>  getNotes(int id) {
        return tripDAO.getNote(id);
    }

    public LiveData<List<TripTable>> getAllToSync() {
        return allToSync;
    }

    public LiveData<List<TripTable>> getAllRecord() {
        return getAllData;
    }

    public LiveData<List<TripTable>> getHistory(String upComing) {
        return history;
    }

    public LiveData<List<TripTable>> getHistoryDone(String upComing) {
        return historydone;
    }

    public void delete(TripTable tripTable) {
        new DeleteAsyncTask(tripDAO).execute(tripTable);
    }

    public void deleteAllRecords() {
        new DeleteAllAsyncTask(tripDAO).execute();
    }


    private static class DeleteAsyncTask extends AsyncTask<TripTable, Void, Void> {
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


    private static class UpDateAsyncTask extends AsyncTask<TripTable, Void, Void> {
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


    private static class DeleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
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

    private static class InsertAllAsyncTask extends AsyncTask<List<TripTable>, Void, Void> {
        private TripDAO tripDAO;

        public InsertAllAsyncTask(TripDAO tripDAO) {
            this.tripDAO = tripDAO;
        }

        @Override
        protected Void doInBackground(List<TripTable>... lists) {
            tripDAO.insertAll(lists[0]);
            return null;
        }

//        @Override
//        protected Void doInBackground(List<TripTable>... lists) {
//            tripDAO.insertAll(lists[0]);
//        }
    }
}
