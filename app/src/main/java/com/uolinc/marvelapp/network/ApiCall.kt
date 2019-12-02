package com.uolinc.marvelapp.network

import com.uolinc.marvelapp.model.ReturnData
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Interface responsável para fazer a chamada com a API
 */
interface ApiCall {

    @GET("characters")
    suspend fun listCharacter(@Query("ts") timeStamp: String,
                              @Query("apikey") apiKey: String,
                              @Query("hash") hash: String,
                              @Query("limit") limit: Int,
                              @Query("offset") offset: Int,
                              @Query("orderBy") orderBy: String): ReturnData
}