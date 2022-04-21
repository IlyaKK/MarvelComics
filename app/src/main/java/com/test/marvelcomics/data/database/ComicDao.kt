package com.test.marvelcomics.data.database

import androidx.paging.PagingSource
import androidx.room.*
import com.test.marvelcomics.domain.entity.database.*

@Dao
interface ComicDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertComic(comic: ComicEntityDb)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComicWriterCrossRef(comicWriterCrossRef: ComicWriterCrossRef)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComicPainterCrossRef(comicPainterCrossRef: ComicPainterCrossRef)

    @Transaction
    @Query(
        "SELECT * FROM comic " +
                "WHERE saleDay <= :endRange AND saleDay >= :startRange " +
                "ORDER BY saleDay DESC"
    )
    fun getComics(
        startRange: String,
        endRange: String
    ): PagingSource<Int, ComicWithWritersAndPainters>

    @Query("DELETE FROM comic")
    suspend fun clearComics()

    @Query("DELETE FROM comic_writer_cross_ref")
    suspend fun clearComicWriterCrossRef()

    @Query("DELETE FROM comic_painter_cross_ref")
    suspend fun clearComicPainterCrossRef()
}