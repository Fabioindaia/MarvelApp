package com.uolinc.marvelapp.network

enum class Status {
    LOADING,
    LOADED,
    EMPTY,
    FAILED
}

@Suppress("DataClassPrivateConstructor")
data class NetworkState private constructor(
    val status: Status,
    val message: String? = null) {
    companion object {
        val LOADING = NetworkState(Status.LOADING)
        val LOADED = NetworkState(Status.LOADED)
        val EMPTY = NetworkState(Status.EMPTY)
        fun error(msg: String?) = NetworkState(Status.FAILED, msg)
    }
}

