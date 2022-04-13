package com.test.marvelcomics.domain.entity.database

import androidx.room.*

@Entity(
    tableName = "comic_writer_join",
    foreignKeys = [
        ForeignKey(
            entity = ComicEntityDb::class,
            parentColumns = ["id"],
            childColumns = ["comic_id"]
        ),
        ForeignKey(
            entity = WriterEntityDb::class,
            parentColumns = ["id"],
            childColumns = ["writer_id"]
        )
    ],
    indices = [
        Index(name = "comic_writer", value = ["comic_id", "writer_id"], unique = true)
    ]
)
data class ComicWriterJoinDb(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "comic_id")
    val comicId: Int,
    @ColumnInfo(name = "writer_id")
    val writerId: Int
)
