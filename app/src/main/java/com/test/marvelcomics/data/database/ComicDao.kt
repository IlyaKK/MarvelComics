package com.test.marvelcomics.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.test.marvelcomics.domain.entity.database.ComicEntityDb

@Dao
interface ComicDao {
    @Insert
    fun insert(comic: ComicEntityDb)

    @Query("SELECT id FROM comic")
    fun getComicsId(): List<Int>

    @Query(
        "SELECT * FROM comic " +
                "WHERE saleDay <= :endRange AND saleDay >= :startRange " +
                "ORDER BY saleDay DESC"
    )
    fun getComics(startRange: String, endRange: String): List<ComicEntityDb>

    @Delete
    fun delete(comic: ComicEntityDb)
}