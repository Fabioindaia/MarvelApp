package com.uolinc.marvelapp.network

import com.google.gson.Gson
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Classe responsável por configurar a API retrofit
 */
class RetrofitConfig {
    private val retrofit: Retrofit
    private val BASE_URL = "https://gateway.marvel.com/v1/public/"

    init {
        this.retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(Gson()))
                .build()
    }

    fun apiCall(): ApiCall = this.retrofit.create(ApiCall::class.java)
}