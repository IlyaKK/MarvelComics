package com.test.marvelcomics.ui.screens.list_comics.recycler_view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.test.marvelcomics.databinding.DownloadViewBinding
import com.test.marvelcomics.databinding.ListComicsItemBinding
import com.test.marvelcomics.domain.entity.Comic

const val LOAD_TYPE = 0
const val COMICS_TYPE = 1

class ListComicsAdapter :
    ListAdapter<Comic, RecyclerView.ViewHolder>(ComicsItemCallBack()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            COMICS_TYPE -> ListComicsViewHolder(
                ListComicsItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            else -> DownloadViewHolder(
                DownloadViewBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ListComicsViewHolder -> {
                holder.bind(getItem(position))
            }
            is DownloadViewHolder -> {
                holder.bind(true)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount - 1) {
            LOAD_TYPE
        } else {
            COMICS_TYPE
        }
    }

    override fun getItemCount(): Int {
        return if (currentList.isEmpty()) 0 else currentList.size + 1
    }

    override fun getItem(position: Int): Comic {
        return currentList[position]
    }
}
