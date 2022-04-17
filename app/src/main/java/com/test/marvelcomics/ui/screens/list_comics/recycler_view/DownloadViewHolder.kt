package com.test.marvelcomics.ui.screens.list_comics.recycler_view

import android.view.View.*
import androidx.recyclerview.widget.RecyclerView
import com.test.marvelcomics.databinding.DownloadViewBinding

class DownloadViewHolder(private val binding: DownloadViewBinding) :
    RecyclerView.ViewHolder(binding.root) {
    private var stateLoadingProgressBar: Boolean = false

    fun initializeListenerProgressBar(): ListComicsAdapter.ListenerProgressBar {
        return (object : ListComicsAdapter.ListenerProgressBar {
            override fun setLoadState(isLoadState: Boolean) {
                if (isLoadState) {
                    binding.downloadProgressBar.visibility = VISIBLE
                } else {
                    binding.downloadProgressBar.visibility = INVISIBLE
                }
                stateLoadingProgressBar = isLoadState
            }

            override fun getLoadState(): Boolean {
                return stateLoadingProgressBar
            }
        })
    }
}