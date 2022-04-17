package com.test.marvelcomics.ui.view_models.view_model_list_comics

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.test.marvelcomics.data.DataBaseComicsRepository
import com.test.marvelcomics.data.MarvelComicsRepositoryImpl
import com.test.marvelcomics.data.NetworkMarvelComicsRepository
import com.test.marvelcomics.domain.entity.Comic
import com.test.marvelcomics.domain.repo.MarvelComicsRepository

class ListComicsViewModel(
    private val comicsRepository: MarvelComicsRepository
) : ViewModel(), ListComicsContract.ViewModel {

    private val _listMarvelComicsLiveData = MutableLiveData<List<Comic>>()
    override val listMarvelComicsLiveData: LiveData<List<Comic>> = _listMarvelComicsLiveData

    override fun getPublishedMarvelComics(stateInternet: Boolean, dataRange: String, offset: Int) {
        comicsRepository.getComicsData(stateInternet, dataRange, offset) {
            _listMarvelComicsLiveData.postValue(it)
        }
    }

}