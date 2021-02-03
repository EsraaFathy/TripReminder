package com.example.tripreminder.RoomDataBase;

import android.app.Application;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import java.util.List;

public class TripRepository {
    private static TripDAO tripDAO;
    private LiveData<List<TripTable>> getAllData;
    private LiveData<List<TripTable>> history;
    private static LiveData<List<TripTable>> allToSync;


    public TripRepository(Application application) {
        TripRoomDataBase tripRoomDataBase = TripRoomDataBase.getInstance(application);
        tripDAO = tripRoomDataBase.tripDao();
        getAllData = tripDAO.getAllHomeTrips("up Coming");
        history = tripDAO.getHistory("up Coming");
        allToSync = tripDAO.getAllToAsync();
    }

    public List<String> getTitleDistance(){
        return tripDAO.getAllDistance();
    }
    public TripTable getTripTableById(long l){
        return tripDAO.getTripTableById(l);
    }

    public long insert(TripTable tripTable) {
        return tripDAO.Insert(tripTable);
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

    public void delete(TripTable tripTable) {
        new DeleteAsyncTask(tripDAO).execute(tripTable);
    }

    public void deleteAllRecords() {
        new DeleteAllAsyncTask(tripDAO).execute();
    }

//    private static class NoteQuery extends AsyncTask<Void, Void, LiveData<String>> {
//        int id;
//        TripDAO tripDAO;
//        public NoteQuery(TripDAO tripDAO,int id) {
//            this.id = id;
//            this.tripDAO=tripDAO;
//        }
//
//        @Override
//        protected LiveData<String> doInBackground(Void... voids) {
//            Log.d("TAG", "doInBackground: triprepo"+ id);
//           return tripDAO.getNote(id);
//        }
//
//        @Override
//        protected void onPostExecute(LiveData<String> stringLiveData) {
//            notes=stringLiveData;
//            handler.sendEmptyMessage(0);
//            Log.d("TAG on post",""+notes );
//
//        }
//    }


//    private static class InsertAsyncTask extends AsyncTask<TripTable, Long, Long> {
//        AsuncFinishListener asuncFinishListener;
//        private TripDAO tripDAO;
//
//        public static interface AsuncFinishListener{
//            public void returnData(Long l);
//        }
//
//        public InsertAsyncTask(AsuncFinishListener asuncFinishListener, TripDAO tripDAO) {
//            this.asuncFinishListener = asuncFinishListener;
//            this.tripDAO = tripDAO;
//        }
//
//        @Override
//        protected Long doInBackground(TripTable... tripTables) {
//            return tripDAO.Insert(tripTables[0]);
//        }
//
//        @Override
//        protected void onPostExecute(Long aVoid) {
//            asuncFinishListener.returnData(aVoid);
//        }
//    }


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
}
