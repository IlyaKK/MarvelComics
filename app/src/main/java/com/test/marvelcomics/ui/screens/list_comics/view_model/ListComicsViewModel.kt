package com.test.marvelcomics.ui.screens.list_comics.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.test.marvelcomics.data.NetworkMarvelComicsRepositoryImpl
import com.test.marvelcomics.domain.entity.Comic
import com.test.marvelcomics.domain.repo.MarvelComicsRepository

class ListComicsViewModel : ViewModel(), ListComicsContract.ViewModel {
    private val marvelComicsRepository: MarvelComicsRepository by lazy { NetworkMarvelComicsRepositoryImpl() }

    private val _onGetListMarvelComics = MutableLiveData<List<Comic>>()
    val onGetListMarvelComics: LiveData<List<Comic>> = _onGetListMarvelComics

    override fun getPublishedMarvelComics(nowData: String) {
        marvelComicsRepository.getPublishedMarvelComics(nowData) {
            _onGetListMarvelComics.postValue(it.results)
        }
    }
}