package com.test.marvelcomics.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.test.marvelcomics.R
import com.test.marvelcomics.data.DataBaseComicsRepository
import com.test.marvelcomics.data.NetworkMarvelComicsRepository
import com.test.marvelcomics.data.repositoryApp
import com.test.marvelcomics.domain.entity.Comic
import com.test.marvelcomics.ui.screens.detail_comic.ComicDetailFragment
import com.test.marvelcomics.ui.screens.list_comics.ListComicsFragment

class MainActivity : AppCompatActivity(), ListComicsFragment.Controller {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            repositoryApp.comicRepository?.let {
                ListComicsFragment.newInstance(it)
            }?.let {
                supportFragmentManager.beginTransaction()
                    .replace(
                        R.id.container,
                        it
                    )
                    .commit()
            }
        }
    }

    override fun displayComicDetail() {
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.container,
                ComicDetailFragment.newInstance()
            )
            .commit()
    }

    override fun onDestroy() {
        repositoryApp.comicRepository = null
        super.onDestroy()
    }
}