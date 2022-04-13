package com.test.marvelcomics.domain.entity.api

import com.google.gson.annotations.SerializedName

data class Comic(
    val id: Int,
    val title: String,
    val issueNumber: Int,
    val description: String?,
    val format: String,
    val pageCount: Int,
    val text: String?,
    val urls: List<UrlsAboutComic>,
    val dates: List<DatesOfComic>,
    val prices: List<Price>,
    @SerializedName("thumbnail")
    val imagePath: Image,
    @SerializedName("creators")
    val creatorsComic: CreatorsComic
)
