package com.test.marvelcomics.ui.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.test.marvelcomics.domain.repo.MarvelComicsRepository
import com.test.marvelcomics.ui.view_models.view_model_list_comics.ListComicsViewModel

class ListComicsViewModelFactory(private val comicsRepository: MarvelComicsRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass == ListComicsViewModel::class.java) {
            @Suppress("UNCHECKED_CAST")
            return ListComicsViewModel(comicsRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}