package com.test.marvelcomics.domain.entity.database

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "writer",
    indices = [Index("name", unique = true)]
)
data class WriterEntityDb(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val name: String
)
