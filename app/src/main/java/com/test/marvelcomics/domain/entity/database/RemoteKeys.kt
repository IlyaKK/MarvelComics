package com.test.marvelcomics.domain.entity.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKeys(
    @PrimaryKey
    val comicId: Int,
    val prevKey: Int?,
    val nextKey: Int?
)