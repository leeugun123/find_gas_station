package org.techtown.find_gas_station.MVVM;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.techtown.find_gas_station.set.Set;

import java.util.List;

public class SetViewModel extends AndroidViewModel {

    private SetRepository setRepository;
    private LiveData<Set> setLiveData;

    public SetViewModel(@NonNull Application application) {
        super(application);
        setRepository = new SetRepository(application);
        setLiveData = setRepository.getAllSets();
    }

    public void insert(Set set) {
        setRepository.insert(set);
    }

    public void update(Set set){
        setRepository.update(set);
    }

    public void delete(){
        setRepository.deleteAll();
    }

    public LiveData<Set> getSetLiveData() {
        return setLiveData;
    }


}

// ViewModel 클래스 내에서 LiveData 정의


