package com.uolinc.marvelapp.model

data class Result(
        var id: Int = 0,
        var name: String = "",
        var description: String = "",
        var resourceURI: String = "",
        var thumbnail: Thumbnail? = null
)
