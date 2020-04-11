package com.uolinc.marvelapp.network

import com.uolinc.marvelapp.model.response.DataResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Interface responsável para fazer a chamada com a API
 */
interface ApiCall {

    @GET("characters")
    fun listCharacter(@Query("ts") timeStamp: String,
                      @Query("apikey") apiKey: String,
                      @Query("hash") hash: String,
                      @Query("offset") offset: Int = 0,
                      @Query("orderBy") orderBy: String): Observable<DataResponse>
}