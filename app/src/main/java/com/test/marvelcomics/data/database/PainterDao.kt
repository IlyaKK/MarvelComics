package com.test.marvelcomics.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.test.marvelcomics.domain.entity.database.PainterEntityDb

@Dao
interface PainterDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(painter: PainterEntityDb)

    @Query(
        "SELECT painterId FROM painter " +
                "WHERE name = :namePainter"
    )
    suspend fun getIdPainterByName(namePainter: String): Int

    @Query("DELETE FROM painter")
    suspend fun clearPainter()
}