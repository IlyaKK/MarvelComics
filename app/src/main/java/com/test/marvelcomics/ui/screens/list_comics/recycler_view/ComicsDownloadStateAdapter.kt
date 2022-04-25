package com.test.marvelcomics.ui.screens.list_comics.recycler_view

import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter

class ComicsDownloadStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<ComicsDownloadStateViewHolder>() {
    override fun onBindViewHolder(holder: ComicsDownloadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): ComicsDownloadStateViewHolder {
        return ComicsDownloadStateViewHolder.create(parent, retry)
    }
}