package com.test.marvelcomics.ui.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.test.marvelcomics.domain.entity.Comic

class SharedComicViewModel: ViewModel() {
    val comicMutableLiveData = MutableLiveData<Comic>()
    val comicLiveData: LiveData<Comic> = comicMutableLiveData
}