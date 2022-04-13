package com.test.marvelcomics.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.test.marvelcomics.R
import com.test.marvelcomics.data.DataBaseComicsRepository
import com.test.marvelcomics.data.NetworkMarvelComicsRepository
import com.test.marvelcomics.data.repositoryApp
import com.test.marvelcomics.domain.entity.Comic
import com.test.marvelcomics.ui.screens.list_comics.ListComicsFragment

class MainActivity : AppCompatActivity(), ListComicsFragment.Controller {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        val networkRepository: NetworkMarvelComicsRepository = repositoryApp.comicNetworkRepo
        val databaseRepository: DataBaseComicsRepository = repositoryApp.comicDatabaseRepo
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(
                    R.id.container,
                    ListComicsFragment.newInstance(networkRepository, databaseRepository)
                )
                .commit()
        }
    }

    override fun displayComicDetail(comic: Comic?) {

    }
}