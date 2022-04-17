package com.test.marvelcomics.domain.entity.database

import androidx.room.*

@Entity(
    tableName = "comic_painter_join",
    foreignKeys = [
        ForeignKey(
            entity = ComicEntityDb::class,
            parentColumns = ["id"],
            childColumns = ["comic_id"]
        ),
        ForeignKey(
            entity = PainterEntityDb::class,
            parentColumns = ["id"],
            childColumns = ["painter_id"]
        )
    ],
    indices = [
        Index(name = "comic_painter", value = ["comic_id", "painter_id"], unique = true)
    ]
)
data class ComicPainterJoinDb(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "comic_id")
    val comicId: Int,
    @ColumnInfo(name = "painter_id")
    val painterId: Int
)
