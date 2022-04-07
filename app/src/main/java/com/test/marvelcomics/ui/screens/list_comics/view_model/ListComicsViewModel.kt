package com.test.marvelcomics.ui.screens.list_comics.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.test.marvelcomics.data.NetworkMarvelComicsRepositoryImpl
import com.test.marvelcomics.domain.entity.Comic
import com.test.marvelcomics.domain.repo.MarvelComicsRepository

class ListComicsViewModel : ViewModel(), ListComicsContract.ViewModel {
    private val marvelComicsRepository: MarvelComicsRepository by lazy { NetworkMarvelComicsRepositoryImpl() }

    private val _listMarvelComicsLiveData = MutableLiveData<List<Comic>>()
    override val listMarvelComicsLiveData: LiveData<List<Comic>> = _listMarvelComicsLiveData

    override fun getPublishedMarvelComics(dataRange: String, offset: Int) {
        marvelComicsRepository.getPublishedMarvelComics(dataRange, offset) {
            _listMarvelComicsLiveData.postValue(it)
        }
    }

}