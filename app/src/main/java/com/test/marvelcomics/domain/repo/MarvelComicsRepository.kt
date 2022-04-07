package com.test.marvelcomics.domain.repo

import com.test.marvelcomics.domain.entity.Comic

interface MarvelComicsRepository {
    fun getPublishedMarvelComics(nowData: String, offset: Int = 0, callback: (List<Comic>) -> Unit)
}