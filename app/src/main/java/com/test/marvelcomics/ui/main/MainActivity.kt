package com.test.marvelcomics.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.test.marvelcomics.R
import com.test.marvelcomics.ui.screens.list_comics.ListComicsFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, ListComicsFragment.newInstance())
                .commitNow()
        }
    }
}