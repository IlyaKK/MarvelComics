package com.test.marvelcomics.ui.view_models.view_model_list_comics

import androidx.lifecycle.LiveData
import com.test.marvelcomics.domain.entity.Comic

class ListComicsContract {
    interface ViewModel {
        val listMarvelComicsLiveData: LiveData<List<Comic>>

        fun getPublishedMarvelComics(stateInternet: Boolean, dataRange: String, offset: Int = 0)
    }
}