package com.test.marvelcomics.ui.screens.list_comics.recycler_view

import android.annotation.SuppressLint
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.test.marvelcomics.databinding.ListComicsItemBinding
import com.test.marvelcomics.domain.entity.Comic

class ListComicsViewHolder(private val binding: ListComicsItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    @SuppressLint("CheckResult")
    fun bind(comic: Comic, listenerCardComicClick: ListComicsAdapter.ListenerCardComicClick?) {
        val nameWriters: StringBuilder = StringBuilder()
        val namePainters: StringBuilder = StringBuilder()

        binding.oneComicInListCardView.setOnClickListener {
            listenerCardComicClick?.onComicCardClickListener(comic)
        }

        binding.apply {
            nameComicTextView.text = comic.title
            comic.writers?.forEach {
                if (nameWriters.isEmpty()) {
                    nameWriters.append(it)
                } else {
                    nameWriters.append(
                        ", $it"
                    )
                }
            }

            comic.painters?.forEach {
                if (namePainters.isEmpty())
                    namePainters.append(it)
                else {
                    namePainters.append(
                        ", $it"
                    )
                }
            }

            if (nameWriters.isEmpty()) {
                titleWriterTextView.visibility = GONE
            } else {
                titleWriterTextView.visibility = VISIBLE
            }

            if (namePainters.isEmpty()) {
                titlePencilerTextView.visibility = GONE
            } else {
                titlePencilerTextView.visibility = VISIBLE
            }

            nameWriterTextView.text = nameWriters
            namePencilerTextView.text = namePainters

            Glide
                .with(root)
                .load(
                    (comic.imagePath.replace("http", "https"))
                            + "/portrait_fantastic.jpg"
                )
                .override(100, 150)
                .into(pictureComicImageView)
        }
    }
}
