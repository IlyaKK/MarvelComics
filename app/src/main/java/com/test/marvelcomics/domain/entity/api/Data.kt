package com.test.marvelcomics.domain.entity.api

import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("data")
    val dataComics: Comics
)
