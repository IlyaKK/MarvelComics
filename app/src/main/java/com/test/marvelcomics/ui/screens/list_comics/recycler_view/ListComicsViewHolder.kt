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

            comic?.writers?.forEach {
                if (nameWriterTextView.text.isNotEmpty()) {
                    var nameWriter: String = nameWriterTextView.text.toString()
                    nameWriter = "$nameWriter, ${it.name}"
                    nameWriterTextView.text = nameWriter
                } else {
                    nameWriterTextView.text = it.name
                }
            }

            if (nameWriterTextView.text.isEmpty()) {
                nameWriterTextView.visibility = GONE
                titleWriterTextView.visibility = GONE
            } else {
                nameWriterTextView.visibility = VISIBLE
                titleWriterTextView.visibility = VISIBLE
            }

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

            Glide
                .with(root)
                .load(
                    (comic?.comic?.imagePath?.replace("http", "https"))
                            + "/portrait_fantastic.jpg"
                )
                .override(100, 150)
                .into(pictureComicImageView)
        }
    }
}
