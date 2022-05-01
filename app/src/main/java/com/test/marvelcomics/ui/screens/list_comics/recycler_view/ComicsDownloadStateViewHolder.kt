package com.test.marvelcomics.ui.screens.list_comics.recycler_view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.test.marvelcomics.R
import com.test.marvelcomics.databinding.ComicsDownloadStateViewItemBinding

class ComicsDownloadStateViewHolder(
    private val binding: ComicsDownloadStateViewItemBinding,
    retry: () -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    init {
        binding.retryButton.setOnClickListener { retry.invoke() }
    }

    fun bind(loadState: LoadState) {
        if (loadState is LoadState.Error) {
            binding.errorMessageTextView.text = loadState.error.localizedMessage
        }

        binding.downloadProgressBar.isVisible = loadState is LoadState.Loading
        binding.retryButton.isVisible = loadState is LoadState.Error
        binding.errorMessageTextView.isVisible = loadState is LoadState.Error
    }

    companion object {
        fun create(parent: ViewGroup, retry: () -> Unit): ComicsDownloadStateViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.comics_download_state_view_item, parent, false)
            val binding = ComicsDownloadStateViewItemBinding.bind(view)
            return ComicsDownloadStateViewHolder(binding, retry)
        }
    }
}