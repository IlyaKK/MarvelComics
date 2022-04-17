package com.test.marvelcomics.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.test.marvelcomics.domain.entity.database.WriterEntityDb

@Dao
interface WriterDao {
    @Insert
    fun insert(writer: WriterEntityDb)

    @Query("SELECT name FROM writer")
    fun getWritersName(): List<String>

    @Query(
        "SELECT id FROM writer " +
                "WHERE name = :nameWriter"
    )
    fun getIdByName(nameWriter: String): Int
}