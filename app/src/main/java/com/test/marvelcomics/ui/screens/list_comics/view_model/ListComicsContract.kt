package com.test.marvelcomics.ui.screens.list_comics.view_model

import androidx.lifecycle.LiveData
import com.test.marvelcomics.domain.entity.Comic

class ListComicsContract {
    interface ViewModel {
        val listMarvelComicsLiveData: LiveData<List<Comic>>

        fun getPublishedMarvelComics(dataRange: String, offset: Int = 0)
    }
}