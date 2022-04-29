package com.test.marvelcomics.ui.main

import android.content.IntentFilter
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.test.marvelcomics.R
import com.test.marvelcomics.databinding.MainActivityBinding
import com.test.marvelcomics.ui.screens.detail_comic.ComicDetailFragment
import com.test.marvelcomics.ui.screens.list_comics.ListComicsFragment
import com.test.marvelcomics.ui.view_models.view_model_main_activity.MainActivityViewModel
import com.test.marvelcomics.ui.view_models.view_model_main_activity.MainActivityViewModelFactory
import com.test.marvelcomics.ui.view_models.view_model_main_activity.StateScreen


@RequiresApi(Build.VERSION_CODES.O)
class MainActivity : AppCompatActivity(), ListComicsFragment.Controller {
    private lateinit var binding: MainActivityBinding
    private lateinit var receiver: NetworkReceiver
    private val mainActivityViewModel by lazy {
        ViewModelProvider(
            this,
            MainActivityViewModelFactory(this)
        )[MainActivityViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        super.onCreate(savedInstanceState)
        initialiseReceiver()
        initializeLaunchFragment()
    }

    private fun initialiseReceiver() {
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        receiver = NetworkReceiver {
            val messageReceiver = if (it == null) {
                getString(R.string.no_internet)
            } else {
                getString(R.string.exist_internet)
            }
            Snackbar.make(binding.root, messageReceiver, Snackbar.LENGTH_LONG).show()
        }
        this.registerReceiver(receiver, filter)
    }

    private fun initializeLaunchFragment() {
        mainActivityViewModel.state.observe(this) {
            when (it.stateScreen) {
                StateScreen.SCREEN_LIST_COMICS_FRAGMENT -> {
                    launchComicsListScreen()
                }
                StateScreen.SCREEN_COMIC_DETAIL_FRAGMENT -> {
                    if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                        launchDetailComicFragmentPortrait()
                    } else {
                        launchDetailComicFragmentLandscape()
                    }
                }
                else -> {
                    mainActivityViewModel.setState(StateScreen.SCREEN_LIST_COMICS_FRAGMENT)
                }
            }
        }
    }


    private fun createNewListComicsFragment() {
        val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(
            binding.firstFragmentContainer.id,
            ListComicsFragment.newInstance(),
            StateScreen.SCREEN_LIST_COMICS_FRAGMENT.toString()
        )
        fragmentTransaction.commit()
    }

    private fun launchComicsListScreen() {
        val listComicsFragment: ListComicsFragment? =
            supportFragmentManager.findFragmentByTag(StateScreen.SCREEN_LIST_COMICS_FRAGMENT.toString()) as? ListComicsFragment
        if (listComicsFragment == null) {
            createNewListComicsFragment()
        } else {
            val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(
                binding.firstFragmentContainer.id,
                listComicsFragment,
                StateScreen.SCREEN_LIST_COMICS_FRAGMENT.toString()
            )
            fragmentTransaction.commit()
        }

    }

    private fun createNewComicDetailFragment() {
        val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        if (binding.secondFragmentContainer == null) {
            val tag = StateScreen.SCREEN_COMIC_DETAIL_FRAGMENT.toString()
            fragmentTransaction.replace(
                binding.firstFragmentContainer.id,
                ComicDetailFragment.newInstance(),
                tag
            )
            fragmentTransaction.addToBackStack("")
        } else {
            val tag = StateScreen.SCREEN_COMIC_DETAIL_LANDSCAPE_FRAGMENT.toString()
            binding.secondFragmentContainer?.let {
                fragmentTransaction.replace(
                    it.id,
                    ComicDetailFragment.newInstance(),
                    tag
                )
            }
        }
        fragmentTransaction.commit()
    }

    private fun removeLandscapeVersionComicDetailIfExist() {
        val detailLandscapeComicFragment: ComicDetailFragment? =
            supportFragmentManager.findFragmentByTag(
                StateScreen.SCREEN_COMIC_DETAIL_LANDSCAPE_FRAGMENT.toString()
            ) as? ComicDetailFragment
        detailLandscapeComicFragment?.let {
            supportFragmentManager.popBackStack()
            supportFragmentManager
                .beginTransaction()
                .remove(detailLandscapeComicFragment)
                .commit()
        }
    }

    private fun launchDetailComicFragmentPortrait() {
        val detailPortraitComicFragment: ComicDetailFragment? =
            supportFragmentManager.findFragmentByTag(
                StateScreen.SCREEN_COMIC_DETAIL_FRAGMENT.toString()
            ) as? ComicDetailFragment
        removeLandscapeVersionComicDetailIfExist()
        if (detailPortraitComicFragment == null) {
            createNewComicDetailFragment()
        } else {
            val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(
                binding.firstFragmentContainer.id,
                detailPortraitComicFragment,
                StateScreen.SCREEN_COMIC_DETAIL_FRAGMENT.toString()
            )
            fragmentTransaction.addToBackStack("")
            fragmentTransaction.commit()
        }
    }

    private fun removePortraitVersionComicDetailIfExist() {
        val detailPortraitComicFragment: ComicDetailFragment? =
            supportFragmentManager.findFragmentByTag(
                StateScreen.SCREEN_COMIC_DETAIL_FRAGMENT.toString()
            ) as? ComicDetailFragment
        detailPortraitComicFragment?.let {
            supportFragmentManager.popBackStack()
            supportFragmentManager
                .beginTransaction()
                .remove(detailPortraitComicFragment)
                .commit()
        }
    }

    private fun launchDetailComicFragmentLandscape() {
        val detailLandscapeComicFragment: ComicDetailFragment? =
            supportFragmentManager.findFragmentByTag(
                StateScreen.SCREEN_COMIC_DETAIL_LANDSCAPE_FRAGMENT.toString()
            ) as? ComicDetailFragment

        removePortraitVersionComicDetailIfExist()
        if (detailLandscapeComicFragment == null) {
            createNewComicDetailFragment()
        } else {
            val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
            binding.secondFragmentContainer?.let {
                fragmentTransaction.replace(
                    it.id,
                    detailLandscapeComicFragment,
                    StateScreen.SCREEN_COMIC_DETAIL_LANDSCAPE_FRAGMENT.toString()
                )
            }
            fragmentTransaction.commit()
        }
    }

    override fun displayComicDetail() {
        mainActivityViewModel.setState(StateScreen.SCREEN_COMIC_DETAIL_FRAGMENT)
    }

    override fun onDestroy() {
        this.unregisterReceiver(receiver)
        super.onDestroy()
    }
}