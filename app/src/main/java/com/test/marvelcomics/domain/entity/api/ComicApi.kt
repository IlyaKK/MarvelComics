package com.test.marvelcomics.domain.entity.api

import com.google.gson.annotations.SerializedName

data class ComicApi(
    val id: Int,
    val title: String,
    val issueNumber: Int,
    val description: String?,
    val format: String,
    val pageCount: Int?,
    val textObjects: List<AdditionalDescriptionComic>?,
    val urls: List<UrlsAboutComicApi>,
    val dates: List<DatesOfComicApi>,
    val prices: List<PriceApi>,
    @SerializedName("thumbnail")
    val imageApiPath: ImageApi,
    @SerializedName("creators")
    val creatorsComicApi: CreatorsComicApi
)
