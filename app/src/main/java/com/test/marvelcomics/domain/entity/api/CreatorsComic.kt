package com.test.marvelcomics.domain.entity.api

import com.google.gson.annotations.SerializedName

data class CreatorsComic(
    @SerializedName("items")
    val listCreatorsComic: List<CreatorComic>
)