package com.test.marvelcomics.ui.screens.list_comics.recycler_view

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.test.marvelcomics.databinding.ListComicsItemBinding
import com.test.marvelcomics.domain.entity.Comic

class ListComicsViewHolder(private val binding: ListComicsItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(oneComic: Comic) {
        binding.apply {
            nameComicTextView.text = oneComic.title
            oneComic.creators.items.forEach {
                when (it.role) {
                    "writer" -> binding.nameWriterTextView.text = it.name
                    "penciler (cover)",
                    "penciler",
                    "penciller (cover)",
                    "penciller" -> binding.namePencilerTextView.text =
                        it.name
                }
            }
            Glide.with(binding.root)
                .load(oneComic.thumbnail.path.replace("http", "https")
                        + "/portrait_medium.jpg")
                .into(binding.pictureComicImageView)
        }
    }
}
