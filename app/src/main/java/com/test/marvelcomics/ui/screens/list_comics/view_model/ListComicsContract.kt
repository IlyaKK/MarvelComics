package com.test.marvelcomics.ui.screens.list_comics.view_model

class ListComicsContract {
    interface ViewModel {
        fun getPublishedMarvelComics(nowData: String)
    }
}