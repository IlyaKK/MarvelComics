package com.test.marvelcomics.ui.screens.list_comics.recycler_view

import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import androidx.recyclerview.widget.RecyclerView
import com.test.marvelcomics.databinding.DownloadViewBinding

class DownloadViewHolder(private val binding: DownloadViewBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(loadState: Boolean) {
        if(loadState){
            binding.downloadProgressBar.visibility = VISIBLE
        }else{
            binding.downloadProgressBar.visibility = INVISIBLE
        }
    }
}