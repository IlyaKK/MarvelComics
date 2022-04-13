package com.test.marvelcomics.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.test.marvelcomics.domain.entity.database.PainterEntityDb

@Dao
interface PainterDao {
    @Insert
    fun insert(painter: PainterEntityDb)

    @Query("SELECT name FROM painter")
    fun getPaintersName(): List<String>

    @Query(
        "SELECT id FROM painter " +
                "WHERE name = :namePainter"
    )
    fun getIdByName(namePainter: String): Int
}