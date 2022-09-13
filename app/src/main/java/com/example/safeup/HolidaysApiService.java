package com.example.safeup;

import com.example.safeup.models.HolidaysData;

import retrofit2.Call;
import retrofit2.http.GET;

public interface HolidaysApiService {
    @GET(".")
    Call<HolidaysData> fetchHolidays();

}
