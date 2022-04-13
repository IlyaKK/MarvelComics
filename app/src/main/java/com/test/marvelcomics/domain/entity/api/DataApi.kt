package com.test.marvelcomics.domain.entity.api

import com.google.gson.annotations.SerializedName

data class DataApi(
    @SerializedName("data")
    val dataComicsApi: ComicsApi
)
