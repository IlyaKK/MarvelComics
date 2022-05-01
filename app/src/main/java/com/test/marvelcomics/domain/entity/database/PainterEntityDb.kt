package com.test.marvelcomics.domain.entity.database

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "painter",
    indices = [Index("name", unique = true)]
)
data class PainterEntityDb(
    @PrimaryKey(autoGenerate = true)
    val painterId: Int,
    val name: String
)
