package com.test.marvelcomics.domain.entity.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "comic")
data class ComicEntityDb(
    @PrimaryKey
    val id: Int,
    val title: String,
    val issueNumber: Int?,
    val description: String?,
    val format: String?,
    val pageCount: Int?,
    val solicitText: String?,
    val previewText: String?,
    val urlDetail: String?,
    val saleDay: String?,
    val price: Double?,
    val imagePath: String
)