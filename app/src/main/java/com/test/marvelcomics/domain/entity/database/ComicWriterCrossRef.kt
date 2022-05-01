package com.test.marvelcomics.domain.entity.database

import androidx.room.Entity

@Entity(tableName = "comic_writer_cross_ref", primaryKeys = ["comicId", "writerId"])
data class ComicWriterCrossRef(
    val comicId: Int,
    val writerId: Int
)
