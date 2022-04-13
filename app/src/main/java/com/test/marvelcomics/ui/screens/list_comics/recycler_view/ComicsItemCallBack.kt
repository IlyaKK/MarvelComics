package com.test.marvelcomics.ui.screens.list_comics.recycler_view

import androidx.recyclerview.widget.DiffUtil
import com.test.marvelcomics.domain.entity.api.Comic

class ComicsItemCallBack : DiffUtil.ItemCallback<Comic>() {
    override fun areItemsTheSame(oldItem: Comic, newItem: Comic): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Comic, newItem: Comic): Boolean {
        return oldItem == newItem
    }
}