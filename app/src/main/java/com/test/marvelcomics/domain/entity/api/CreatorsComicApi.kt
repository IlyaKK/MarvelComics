package com.test.marvelcomics.domain.entity.api

import com.google.gson.annotations.SerializedName

data class CreatorsComicApi(
    @SerializedName("items")
    val listCreatorsComicApi: List<CreatorComicApi?>?
)