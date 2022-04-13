package com.test.marvelcomics.ui.screens.list_comics.view_model

import androidx.lifecycle.LiveData
import com.test.marvelcomics.domain.entity.api.Comic

class ListComicsContract {
    interface ViewModel {
        val listMarvelComicsLiveData: LiveData<List<Comic>>

        fun getPublishedMarvelComics(stateInternet: Boolean, dataRange: String, offset: Int = 0)
    }
}