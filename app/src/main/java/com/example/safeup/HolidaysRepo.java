package com.example.safeup;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.safeup.models.HolidayModelItem;
import com.example.safeup.models.HolidaysData;

import java.util.ArrayList;
import java.util.List;

public class HolidaysRepo {
    private static MutableLiveData<ArrayList<HolidayModelItem>> allHolidays;
    private static MutableLiveData<ArrayList<HolidayModelItem>> favHolidays;

    private static HolidaysRepo instance;

    private HolidaysRepo() {
        allHolidays = new MutableLiveData<ArrayList<HolidayModelItem>>();
        favHolidays = new MutableLiveData<ArrayList<HolidayModelItem>>();
    }

    public static HolidaysRepo getInstance(){
        if(instance == null){
            instance = new HolidaysRepo();
        }
        return instance;
    }


    public  MutableLiveData<ArrayList<HolidayModelItem>> getAllHolidays(){
        return allHolidays;
    }

    public  MutableLiveData<ArrayList<HolidayModelItem>> getFavHolidays(){
        return favHolidays;
    }
    public void removeFavHoliday(HolidayModelItem item){
        ArrayList<HolidayModelItem> x;
        x = favHolidays.getValue();
        x.remove(item);
        favHolidays.postValue(x);
    }

    public void appendFavHoliday(HolidayModelItem item){
        ArrayList<HolidayModelItem> x;
        x = favHolidays.getValue();
        if(x !=null) {
            x.add(item);
        }else {
            x = new ArrayList<>();
        }
        favHolidays.postValue(x);
    }


}
