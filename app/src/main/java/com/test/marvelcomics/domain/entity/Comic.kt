package com.test.marvelcomics.domain.entity

data class Comic(
    val id: Int,
    val title: String,
    val issueNumber: Int?,
    val description: String?,
    val format: String?,
    val pageCount: Int?,
    val urlDetail: String?,
    val saleDay: String?,
    val price: Double?,
    val imagePath: String,
    val writers: List<String>?,
    val painters: List<String>?
)
