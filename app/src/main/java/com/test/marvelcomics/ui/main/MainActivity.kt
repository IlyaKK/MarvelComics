package com.test.marvelcomics.ui.main

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.test.marvelcomics.R
import com.test.marvelcomics.ui.screens.detail_comic.ComicDetailFragment
import com.test.marvelcomics.ui.screens.list_comics.ListComicsFragment

@RequiresApi(Build.VERSION_CODES.O)
class MainActivity : AppCompatActivity(), ListComicsFragment.Controller {
    //private lateinit var receiver: NetworkReceiver
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        /*val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        receiver = NetworkReceiver {
            Snackbar.make(binding.container, it, Snackbar.LENGTH_LONG).show()
        }
        this.registerReceiver(receiver, filter)*/
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(
                    R.id.container,
                    ListComicsFragment.newInstance()
                )
                .commit()
        }
    }

    override fun displayComicDetail() {
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.container,
                ComicDetailFragment.newInstance()
            )
            .addToBackStack("Start detail fragment")
            .commit()
    }

    override fun onDestroy() {
        //this.unregisterReceiver(receiver)
        super.onDestroy()
    }
}