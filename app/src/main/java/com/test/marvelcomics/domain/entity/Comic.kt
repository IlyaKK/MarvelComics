package com.test.marvelcomics.domain.entity

data class Comic(
    val id: Int,
    val title: String,
    val issueNumber: Int?,
    val description: String?,
    val format: String,
    val pageCount: Int?,
    val text: String?,
    val urlDetail: String,
    val published: String?,
    val price: Int?,
    val imagePath: String,
    val writers: List<String>?,
    val painters: List<String>?
)
