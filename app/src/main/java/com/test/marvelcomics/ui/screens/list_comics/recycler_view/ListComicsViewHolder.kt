package com.test.marvelcomics.ui.screens.list_comics.recycler_view

import android.annotation.SuppressLint
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.test.marvelcomics.databinding.ComicItemBinding
import com.test.marvelcomics.domain.entity.database.ComicWithWritersAndPainters

class ListComicsViewHolder(private val binding: ComicItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    @SuppressLint("CheckResult")
    fun bind(
        comic: ComicWithWritersAndPainters?,
        listenerCardComicClick: ListComicsAdapter.ListenerCardComicClick?
    ) {
        binding.oneComicInListCardView.setOnClickListener {
            listenerCardComicClick?.onComicCardClickListener(comic)
        }

        binding.apply {
            nameComicTextView.text = comic?.comic?.title

            nameWriterTextView.text = ""
            comic?.writers?.forEach {
                if (nameWriterTextView.text.isEmpty()) {
                    nameWriterTextView.text = it.name
                } else {
                    var nameWrite: String = nameWriterTextView.text.toString()
                    nameWrite = "$nameWrite, ${it.name}"
                    nameWriterTextView.text = nameWrite
                }
            }

            if (nameWriterTextView.text.isEmpty()) {
                nameWriterTextView.visibility = GONE
                titleWriterTextView.visibility = GONE
            } else {
                nameWriterTextView.visibility = VISIBLE
                titleWriterTextView.visibility = VISIBLE
            }

            namePencilerTextView.text = ""
            comic?.painters?.forEach {
                if (namePencilerTextView.text.isNotEmpty()) {
                    var namePainter: String = namePencilerTextView.text.toString()
                    namePainter = "$namePainter, ${it.name}"
                    namePencilerTextView.text = namePainter
                } else {
                    namePencilerTextView.text = it.name
                }
            }

            if (namePencilerTextView.text.isEmpty()) {
                namePencilerTextView.visibility = GONE
                titlePencilerTextView.visibility = GONE
            } else {
                namePencilerTextView.visibility = VISIBLE
                titlePencilerTextView.visibility = VISIBLE
            }

            comic?.comic?.imagePath?.let {
                Glide
                    .with(root)
                    .load(
                        (it.replace("http", "https"))
                                + "/portrait_fantastic.jpg"
                    )
                    .override(100, 150)
                    .into(pictureComicImageView)
            }
        }
    }
}
