package com.test.marvelcomics.ui.screens.list_comics.recycler_view

import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.test.marvelcomics.databinding.ListComicsItemBinding
import com.test.marvelcomics.domain.entity.Comic

class ListComicsViewHolder(private val binding: ListComicsItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(oneComic: Comic) {
        val nameWriters: StringBuilder = StringBuilder()
        val namePencilers: StringBuilder = StringBuilder()

        binding.apply {
            nameComicTextView.text = oneComic.title
            oneComic.creators.items.forEach {
                when (it.role) {
                    "writer" -> {
                        if (nameWriters.isEmpty()) {
                            nameWriters.append(it.name)
                        } else {
                            nameWriters.append(
                                ", " + it.name
                            )
                        }

                    }
                    "penciler (cover)",
                    "penciler",
                    "penciller (cover)",
                    "penciller" -> {
                        if (namePencilers.isEmpty())
                            namePencilers.append(it.name)
                        else {
                            namePencilers.append(
                                ", " + it.name
                            )
                        }
                    }
                }
            }

            if (nameWriters.isEmpty()) {
                titleWriterTextView.visibility = GONE
            } else {
                titleWriterTextView.visibility = VISIBLE
            }

            if (namePencilers.isEmpty()) {
                titlePencilerTextView.visibility = GONE
            } else {
                titlePencilerTextView.visibility = VISIBLE
            }

            nameWriterTextView.text = nameWriters
            namePencilerTextView.text = namePencilers

            Glide.with(root)
                .load(
                    oneComic.thumbnail.path.replace("http", "https")
                            + "/portrait_medium.jpg"
                )
                .into(pictureComicImageView)
        }
    }
}
