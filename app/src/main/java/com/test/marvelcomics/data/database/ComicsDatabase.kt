package com.test.marvelcomics.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.test.marvelcomics.domain.entity.database.*

@Database(
    entities = [
        ComicEntityDb::class,
        WriterEntityDb::class,
        PainterEntityDb::class,
        RemoteKeys::class,
        ComicWriterCrossRef::class,
        ComicPainterCrossRef::class
    ],
    version = 1,
    exportSchema = false
)
abstract class ComicsDatabase : RoomDatabase() {
    abstract fun comicDao(): ComicDao
    abstract fun painterDao(): PainterDao
    abstract fun writerDao(): WriterDao
    abstract fun remoteKeysDao(): RemoteKeysDao

    companion object {

        @Volatile
        private var INSTANCE: ComicsDatabase? = null

        fun getInstance(context: Context): ComicsDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                ComicsDatabase::class.java, "comics.db"
            )
                .build()
    }
}