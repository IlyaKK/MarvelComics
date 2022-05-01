package com.test.marvelcomics.domain.entity.api

import com.google.gson.annotations.SerializedName

data class ComicsApi(
    @SerializedName("results")
    val comicsList: List<ComicApi> = emptyList()
)
