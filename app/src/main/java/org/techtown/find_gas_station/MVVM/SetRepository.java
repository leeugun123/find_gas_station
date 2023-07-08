package org.techtown.find_gas_station.MVVM;

import android.app.Application;

import org.techtown.find_gas_station.set.RoomDB;
import org.techtown.find_gas_station.set.Set;
import org.techtown.find_gas_station.set.SetDao;

public class SetRepository {

    private SetDao setDao;
    private Set allsets;

    public SetRepository(Application application){

        RoomDB roomDB = RoomDB.getAppDatabase(application);
        setDao = roomDB.setDao();
        allsets = setDao.getAll();


    }

    public void insert(Set set){

        setDao.insert(set);
    }

    public Set getSets(){
        return allsets;
    }

    public void delete(){
        setDao.deleteAll();
    }

    public void update(Set set){
        setDao.update(set);
    }





}
