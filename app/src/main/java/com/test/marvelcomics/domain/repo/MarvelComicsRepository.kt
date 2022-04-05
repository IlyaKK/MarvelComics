package com.test.marvelcomics.domain.repo

import com.test.marvelcomics.domain.entity.Comic
import com.test.marvelcomics.domain.entity.Comics

interface MarvelComicsRepository {
    fun getPublishedMarvelComics(nowData: String, callback: (Comics) -> Unit)
}