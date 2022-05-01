package com.test.marvelcomics.domain.entity.database

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class ComicWithWritersAndPainters(
    @Embedded val comic: ComicEntityDb,
    @Relation(
        parentColumn = "comicId",
        entityColumn = "writerId",
        associateBy = Junction(ComicWriterCrossRef::class)
    )
    val writers: List<WriterEntityDb>,
    @Relation(
        parentColumn = "comicId",
        entityColumn = "painterId",
        associateBy = Junction(ComicPainterCrossRef::class)
    )
    val painters: List<PainterEntityDb>
)
