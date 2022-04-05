package com.test.marvelcomics.domain.entity

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
    val price: Int,
    val thumbnail: Image,
    val creators: CreatorsComic
)
