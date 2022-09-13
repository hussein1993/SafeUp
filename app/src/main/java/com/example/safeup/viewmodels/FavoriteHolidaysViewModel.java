package com.example.safeup.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.safeup.HolidaysRepo;
import com.example.safeup.models.HolidayModelItem;

import java.util.ArrayList;

public class FavoriteHolidaysViewModel extends AndroidViewModel {
    private MutableLiveData<ArrayList<HolidayModelItem>> favHolidays;
    private HolidaysRepo repository;

    public FavoriteHolidaysViewModel(@NonNull Application application){
        super(application);
        repository = HolidaysRepo.getInstance();
        favHolidays = repository.getFavHolidays();
    }

    public LiveData<ArrayList<HolidayModelItem>> getFavHolidays() {
        return favHolidays;
    }

    public void setFavHolidays(MutableLiveData<ArrayList<HolidayModelItem>> list) {
        favHolidays = list;
    }
}