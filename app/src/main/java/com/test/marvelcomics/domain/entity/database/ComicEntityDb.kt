package com.test.marvelcomics.domain.entity.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "comic")
data class ComicEntityDb(
    @PrimaryKey
    val comicId: Int,
    val title: String,
    val issueNumber: Double?,
    val description: String?,
    val format: String?,
    val pageCount: Int?,
    val urlDetail: String?,
    val saleDay: String?,
    val price: Double?,
    val imagePath: String?,
    val timeDownload: String
)