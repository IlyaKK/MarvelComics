package com.test.marvelcomics.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.test.marvelcomics.domain.entity.database.*

@Database(
    entities = [
        ComicEntityDb::class,
        WriterEntityDb::class,
        PainterEntityDb::class,
        ComicPainterJoinDb::class,
        ComicWriterJoinDb::class
    ],
    version = 1
)
abstract class ComicsDatabase : RoomDatabase() {
    abstract fun comicDao(): ComicDao
    abstract fun comicPainterJoinDao(): ComicPainterJoinDao
    abstract fun comicWriterJoinDao(): ComicWriterJoinDao
    abstract fun painterDao(): PainterDao
    abstract fun writerDao(): WriterDao
}