package com.example.safeup;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestClient {

    public static final String BASE_URL = "https://date.nager.at/api/v3/publicholidays/";
    private static Retrofit retrofit = null;

    public static Retrofit getClient(String year,String CountryCode) {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL+year+"/"+CountryCode+"/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

}
