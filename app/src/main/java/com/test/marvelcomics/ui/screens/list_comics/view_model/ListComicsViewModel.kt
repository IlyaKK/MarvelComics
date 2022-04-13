package com.test.marvelcomics.ui.screens.list_comics.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.test.marvelcomics.data.DataBaseComicsRepository
import com.test.marvelcomics.data.MarvelComicsRepositoryImpl
import com.test.marvelcomics.data.NetworkMarvelComicsRepository
import com.test.marvelcomics.domain.entity.Comic
import com.test.marvelcomics.domain.repo.MarvelComicsRepository

class ListComicsViewModel(
    private val networkRepository: NetworkMarvelComicsRepository,
    private val databaseRepository: DataBaseComicsRepository
) : ViewModel(), ListComicsContract.ViewModel {
    private val marvelComicsRepository: MarvelComicsRepository by lazy { MarvelComicsRepositoryImpl(networkRepository, databaseRepository) }

    private val _listMarvelComicsLiveData = MutableLiveData<List<Comic>>()
    override val listMarvelComicsLiveData: LiveData<List<Comic>> = _listMarvelComicsLiveData

    override fun getPublishedMarvelComics(stateInternet: Boolean, dataRange: String, offset: Int) {
        marvelComicsRepository.getComicsData(stateInternet, dataRange, offset) {
            _listMarvelComicsLiveData.postValue(it)
        }
    }

}