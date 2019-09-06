package com.uolinc.marvelapp.network;

import com.google.gson.Gson;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Classe responsável por configurar a API retrofit
 */
public class RetrofitConfig {
    private final Retrofit retrofit;
    private final String BASE_URL = "https://gateway.marvel.com/v1/public/";

    public RetrofitConfig() {
        this.retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .build();
    }

    public ApiCall apiCall(){
        return this.retrofit.create(ApiCall.class);
    }
}
