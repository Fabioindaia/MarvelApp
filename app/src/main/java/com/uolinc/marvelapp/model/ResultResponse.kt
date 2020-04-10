package com.uolinc.marvelapp.model.response

import com.google.gson.annotations.SerializedName

data class ResultResponse(
        @SerializedName("offset") val offset: Int,
        @SerializedName("limit") val limit: Int,
        @SerializedName("total") val total: Int,
        @SerializedName("count") val count: Int,
        @SerializedName("results") val characters: List<CharacterResponse>
)