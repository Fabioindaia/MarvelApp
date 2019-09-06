package com.uolinc.marvelapp.network;

import com.uolinc.marvelapp.model.ReturnData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Interface responsável para fazer a chamada com a API
 */
public interface ApiCall {

    @GET("characters")
    Call<ReturnData> listCharacter(@Query("ts") String timeStamp,
                                   @Query("apikey") String apiKey,
                                   @Query("hash") String hash,
                                   @Query("limit") int limit,
                                   @Query("offset") int offset,
                                   @Query("orderBy") String orderBy);

}
