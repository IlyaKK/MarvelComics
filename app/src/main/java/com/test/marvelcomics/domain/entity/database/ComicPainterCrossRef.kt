package com.test.marvelcomics.domain.entity.database

import androidx.room.Entity

@Entity(tableName = "comic_painter_cross_ref", primaryKeys = ["comicId", "painterId"])
data class ComicPainterCrossRef(
    val comicId: Int,
    val painterId: Int
)
