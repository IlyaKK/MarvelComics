package com.test.marvelcomics.domain.repo

import com.test.marvelcomics.domain.entity.Comic

interface MarvelComicsRepository {
    fun getComicsData(
        stateInternet: Boolean,
        dataRange: String,
        offset: Int = 0,
        callback: (List<Comic>) -> Unit
    )
}