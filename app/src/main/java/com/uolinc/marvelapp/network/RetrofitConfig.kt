package com.uolinc.marvelapp.network

import com.google.gson.Gson
import com.uolinc.marvelapp.util.Keys
import com.uolinc.marvelapp.util.Tools
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Classe responsável por configurar a API retrofit
 */
class RetrofitConfig {
    private val retrofit: Retrofit
    private val baseUrl = "https://gateway.marvel.com/v1/public/"

    init {
        val logging = HttpLoggingInterceptor()
        val httpClient = OkHttpClient.Builder()
        logging.level = HttpLoggingInterceptor.Level.BODY
        httpClient.addInterceptor(logging)
        httpClient.addInterceptor {
            val original = it.request()
            val originalHttpUrl = original.url

            val ts = Tools.ts
            val url = originalHttpUrl.newBuilder()
                    .addQueryParameter("apikey", Keys.apiKey)
                    .addQueryParameter("ts", ts)
                    .addQueryParameter("hash", Tools.getHash(ts)!!)
                    .build()

            it.proceed(original.newBuilder().url(url).build())
        }

        retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(Gson()))
                .client(httpClient.build())
                .build()
    }

    fun apiCall(): ApiCall = retrofit.create(ApiCall::class.java)
}