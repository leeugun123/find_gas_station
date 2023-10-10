package org.techtown.find_gas_station.MVVM;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import org.techtown.find_gas_station.set.RoomDB;
import org.techtown.find_gas_station.set.Set;
import org.techtown.find_gas_station.set.SetDao;

public class SetRepository {

    private SetDao setDao;
    private LiveData<Set> allSetsLiveData;

    public SetRepository(Application application){

        RoomDB roomDB = RoomDB.getAppDatabase(application);
        setDao = roomDB.setDao();
        allSetsLiveData = setDao.getAllLiveData();

    }

    public LiveData<Set> getAllSets() {
        return allSetsLiveData;
    }

    public void insert(Set set) {
        // 백그라운드 스레드에서 데이터베이스 삽입 작업 수행
        new InsertAsyncTask(setDao).execute(set);
    }

    public void deleteAll() {
        // 백그라운드 스레드에서 데이터베이스 삭제 작업 수행
        new DeleteAllAsyncTask(setDao).execute();
    }

    public void update(Set set) {
        // 백그라운드 스레드에서 데이터베이스 업데이트 작업 수행
        new UpdateAsyncTask(setDao).execute(set);
    }

    private static class InsertAsyncTask extends AsyncTask<Set, Void, Void> {
        private SetDao setDao;

        private InsertAsyncTask(SetDao setDao) {
            this.setDao = setDao;
        }

        @Override
        protected Void doInBackground(Set... sets) {
            setDao.insert(sets[0]);
            return null;
        }

    }

    private static class DeleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private SetDao setDao;

        private DeleteAllAsyncTask(SetDao setDao) {
            this.setDao = setDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            setDao.deleteAll();
            return null;
        }

    }

    private static class UpdateAsyncTask extends AsyncTask<Set, Void, Void> {
        private SetDao setDao;

        private UpdateAsyncTask(SetDao setDao) {
            this.setDao = setDao;
        }

        @Override
        protected Void doInBackground(Set... sets) {
            setDao.update(sets[0]);
            return null;
        }


    }






}
