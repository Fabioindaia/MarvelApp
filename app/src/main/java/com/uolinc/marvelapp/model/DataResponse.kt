package com.uolinc.marvelapp.model.response

import com.google.gson.annotations.SerializedName

data class DataResponse(
        @SerializedName("code") val code: Int,
        @SerializedName("status") val status: String,
        @SerializedName("data") val data: ResultResponse
)