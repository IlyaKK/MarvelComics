package com.test.marvelcomics.ui.screens.list_comics.recycler_view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.test.marvelcomics.databinding.ComicItemBinding
import com.test.marvelcomics.domain.entity.database.ComicWithWritersAndPainters
import com.test.marvelcomics.ui.view_models.view_model_list_comics.UiModel

const val COMIC_ITEM = 0
const val SEPARATOR_ITEM = 1

class ListComicsAdapter :
    PagingDataAdapter<UiModel, RecyclerView.ViewHolder>(UI_MODEL_COMPARATOR) {
    private var listenerCardComicClick: ListenerCardComicClick? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            COMIC_ITEM -> ListComicsViewHolder(
                ComicItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            SEPARATOR_ITEM -> SeparatorViewHolder.create(parent)
            else -> throw UnsupportedOperationException("Unknown view type")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is UiModel.ComicItem -> COMIC_ITEM
            is UiModel.SeparatorItem -> SEPARATOR_ITEM
            null -> throw UnsupportedOperationException("Unknown view")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ListComicsViewHolder -> {
                holder.bind((getItem(position) as UiModel.ComicItem).comic, listenerCardComicClick)
            }
            is SeparatorViewHolder -> {
                holder.bind((getItem(position) as UiModel.SeparatorItem).description)
            }
        }
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        listenerCardComicClick = null
        super.onDetachedFromRecyclerView(recyclerView)
    }

    fun setOnCardClickListener(listenerCardComicClick: ListenerCardComicClick) {
        this.listenerCardComicClick = listenerCardComicClick
    }

    interface ListenerCardComicClick {
        fun onComicCardClickListener(comic: ComicWithWritersAndPainters?)
    }

    companion object {
        private val UI_MODEL_COMPARATOR = object : DiffUtil.ItemCallback<UiModel>() {
            override fun areItemsTheSame(oldItem: UiModel, newItem: UiModel): Boolean {
                return (oldItem is UiModel.ComicItem && newItem is UiModel.ComicItem &&
                        oldItem.comic.comic.comicId == newItem.comic.comic.comicId) ||
                        (oldItem is UiModel.SeparatorItem && newItem is UiModel.SeparatorItem &&
                                oldItem.description == newItem.description)
            }

            override fun areContentsTheSame(oldItem: UiModel, newItem: UiModel): Boolean =
                oldItem == newItem
        }
    }
}
