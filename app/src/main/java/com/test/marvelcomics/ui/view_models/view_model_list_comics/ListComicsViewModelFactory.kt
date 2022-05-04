package com.test.marvelcomics.ui.view_models.view_model_list_comics

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import com.test.marvelcomics.data.MarvelComicsRepository

class ListComicsViewModelFactory(
    owner: SavedStateRegistryOwner,
    private val repository: MarvelComicsRepository
) :
    AbstractSavedStateViewModelFactory(owner, null) {
    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        if (modelClass.isAssignableFrom(ListComicsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ListComicsViewModel(repository, handle) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}