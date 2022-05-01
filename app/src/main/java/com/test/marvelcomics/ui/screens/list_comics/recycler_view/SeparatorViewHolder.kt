package com.test.marvelcomics.ui.screens.list_comics.recycler_view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.test.marvelcomics.R
import com.test.marvelcomics.databinding.SeparatorViewItemBinding

class SeparatorViewHolder(private val binding: SeparatorViewItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(separatorText: String) {
        binding.separatorTitleTextView.text = separatorText
    }

    companion object {
        fun create(parent: ViewGroup): SeparatorViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.separator_view_item, parent, false)
            val binding = SeparatorViewItemBinding.bind(view)
            return SeparatorViewHolder(binding)
        }
    }
}