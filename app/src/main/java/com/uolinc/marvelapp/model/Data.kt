package com.uolinc.marvelapp.model

import java.util.ArrayList

data class Data(
        var offset: Int,
        var limit: Int,
        var total: Int,
        var count: Int,
        var results: List<Result> = ArrayList()
)