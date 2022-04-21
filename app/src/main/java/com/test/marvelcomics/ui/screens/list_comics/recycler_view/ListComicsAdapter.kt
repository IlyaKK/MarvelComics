package com.test.marvelcomics.ui.screens.list_comics.recycler_view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.test.marvelcomics.databinding.ComicItemBinding
import com.test.marvelcomics.domain.entity.database.ComicWithWritersAndPainters

class ListComicsAdapter :
    PagingDataAdapter<ComicWithWritersAndPainters, RecyclerView.ViewHolder>(COMICS_COMPARATOR) {
    private var listenerCardComicClick: ListenerCardComicClick? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ListComicsViewHolder(
            ComicItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ListComicsViewHolder -> {
                holder.bind(getItem(position), listenerCardComicClick)
            }
        }
    }

    fun setOnCardClickListener(listenerCardComicClick: ListenerCardComicClick) {
        this.listenerCardComicClick = listenerCardComicClick
    }

    interface ListenerCardComicClick {
        fun onComicCardClickListener(comic: ComicWithWritersAndPainters?)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        listenerCardComicClick = null
        super.onDetachedFromRecyclerView(recyclerView)
    }

    companion object {
        private val COMICS_COMPARATOR =
            object : DiffUtil.ItemCallback<ComicWithWritersAndPainters>() {
                override fun areItemsTheSame(
                    oldItem: ComicWithWritersAndPainters,
                    newItem: ComicWithWritersAndPainters
                ): Boolean =
                    oldItem.comic.comicId == newItem.comic.comicId

                override fun areContentsTheSame(
                    oldItem: ComicWithWritersAndPainters,
                    newItem: ComicWithWritersAndPainters
                ): Boolean =
                    oldItem == newItem
            }
    }
}
