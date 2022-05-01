package com.test.marvelcomics

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.savedstate.SavedStateRegistryOwner
import com.test.marvelcomics.data.MarvelComicsRepository
import com.test.marvelcomics.data.database.ComicsDatabase
import com.test.marvelcomics.data.network.MarvelComicsNetworkRepository
import com.test.marvelcomics.data.network.MarvelComicsService
import com.test.marvelcomics.ui.view_models.view_model_list_comics.ListComicsViewModelFactory

object Injection {
    private fun provideMarvelComicsRepository(context: Context): MarvelComicsRepository {
        val marvelComicsNetworkRepository = MarvelComicsNetworkRepository(context, MarvelComicsService.create())
        return MarvelComicsRepository(
            marvelComicsNetworkRepository,
            ComicsDatabase.getInstance(context)
        )
    }

    fun provideViewModelFactory(
        context: Context,
        owner: SavedStateRegistryOwner
    ): ViewModelProvider.Factory {
        return ListComicsViewModelFactory(owner, provideMarvelComicsRepository(context))
    }
}