package com.test.marvelcomics.ui.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.test.marvelcomics.domain.entity.database.ComicWithWritersAndPainters

class SharedComicViewModel : ViewModel() {
    val comicMutableLiveData = MutableLiveData<ComicWithWritersAndPainters>()
    val comicLiveData: LiveData<ComicWithWritersAndPainters> = comicMutableLiveData
}