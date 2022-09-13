package com.example.safeup.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.safeup.HolidaysRepo;
import com.example.safeup.models.HolidayModelItem;

import java.util.ArrayList;

public class HolidaysViewModel extends AndroidViewModel {

    private MutableLiveData<ArrayList<HolidayModelItem>> allHolidays;
    private HolidaysRepo repository;

    public HolidaysViewModel(@NonNull Application application){
        super(application);
        repository = HolidaysRepo.getInstance();
        allHolidays = repository.getAllHolidays();
    }

    public LiveData<ArrayList<HolidayModelItem>> getAllHolidays() {
        return allHolidays;
    }

    public void setAllHolidays(MutableLiveData<ArrayList<HolidayModelItem>> list) {
        allHolidays = list;
    }
}