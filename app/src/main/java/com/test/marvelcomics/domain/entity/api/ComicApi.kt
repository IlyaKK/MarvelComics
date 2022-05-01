package com.test.marvelcomics.domain.entity.api

import android.os.Build
import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class ComicApi(
    val id: Int,
    val title: String,
    val issueNumber: Double?,
    val description: String?,
    val format: String?,
    val pageCount: Int?,
    val textObjects: List<AdditionalDescriptionComic?>?,
    val urls: List<UrlsAboutComicApi?>?,
    val dates: List<DatesOfComicApi?>?,
    val prices: List<PriceApi?>?,
    @SerializedName("thumbnail")
    val imageApiPath: ImageApi?,
    @SerializedName("creators")
    val creatorsComicApi: CreatorsComicApi?
) {
    val timeDownload: String
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDateTime.now().toString()
        } else {
            ""
        }
}
