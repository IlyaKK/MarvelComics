package com.test.marvelcomics.ui.screens.list_comics.recycler_view

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.test.marvelcomics.databinding.ComicItemBinding
import com.test.marvelcomics.domain.entity.database.ComicWithWritersAndPainters

class ListComicsViewHolder(private val binding: ComicItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

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

            binding.downloadImageProgressBar.visibility = VISIBLE
            comic?.comic?.imagePath?.let {
                Glide
                    .with(root)
                    .load(
                        (it.replace("http", "https"))
                                + "/portrait_fantastic.jpg"
                    )
                    .override(100, 150)
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            binding.downloadImageProgressBar.visibility = GONE
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            binding.downloadImageProgressBar.visibility = GONE
                            return false
                        }
                    })
                    .into(pictureComicImageView)
            }
        }
    }
}
