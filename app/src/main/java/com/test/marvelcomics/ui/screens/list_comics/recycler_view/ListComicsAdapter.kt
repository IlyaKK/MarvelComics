package com.test.marvelcomics.ui.screens.list_comics.recycler_view

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.test.marvelcomics.databinding.ListComicsItemBinding
import com.test.marvelcomics.domain.entity.Comic

class ListComicsAdapter : RecyclerView.Adapter<ListComicsViewHolder>() {
    private var dataComics: List<Comic> = emptyList()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(dataComics: List<Comic>) {
        this.dataComics = dataComics
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListComicsViewHolder {
        val binding =
            ListComicsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListComicsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListComicsViewHolder, position: Int) {
        holder.bind(dataComics[position])
    }

    override fun getItemCount(): Int {
        return dataComics.size
    }

}
