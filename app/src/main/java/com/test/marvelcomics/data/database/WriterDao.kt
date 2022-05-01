package com.test.marvelcomics.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.test.marvelcomics.domain.entity.database.WriterEntityDb

@Dao
interface WriterDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(writer: WriterEntityDb)

    @Query(
        "SELECT writerId FROM writer " +
                "WHERE name = :nameWriter"
    )
    suspend fun getIdWriterByName(nameWriter: String): Int

    @Query("DELETE FROM writer")
    suspend fun clearWriter()
}