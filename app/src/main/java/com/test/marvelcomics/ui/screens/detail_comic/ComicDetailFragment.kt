package com.test.marvelcomics.ui.screens.detail_comic

import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.test.marvelcomics.databinding.ComicDetailFragmentBinding
import com.test.marvelcomics.ui.view_models.SharedComicViewModel
import com.test.marvelcomics.util.UtilData

class ComicDetailFragment : Fragment() {
    private lateinit var binding: ComicDetailFragmentBinding

    companion object {
        fun newInstance(): ComicDetailFragment {
            return ComicDetailFragment()
        }
    }

    private val sharedComicViewModel: SharedComicViewModel by lazy {
        ViewModelProvider(requireActivity())[SharedComicViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ComicDetailFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedComicViewModel.comicLiveData.observe(viewLifecycleOwner) { comic ->
            binding.apply {

                comic?.comic?.imagePath?.let {
                    Glide
                        .with(root)
                        .load(
                            (it.replace("http", "https"))
                                    + "/portrait_fantastic.jpg"
                        )
                        .override(168, 252)
                        .listener(object : RequestListener<Drawable> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: Target<Drawable>?,
                                isFirstResource: Boolean
                            ): Boolean {
                                binding.imageDownloadingProgressBar.visibility = GONE
                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any?,
                                target: Target<Drawable>?,
                                dataSource: DataSource?,
                                isFirstResource: Boolean
                            ): Boolean {
                                binding.imageDownloadingProgressBar.visibility = GONE
                                return false
                            }
                        })
                        .into(pictureComicImageView)
                }

                titleComicTextView.text = comic?.comic?.title ?: ""

                if (!comic?.comic?.description.isNullOrEmpty())
                    descriptionComicTextView.text = comic?.comic?.description ?: ""
                else {
                    descriptionComicTextView.visibility = GONE
                    descriptionTitleTextView.visibility = GONE
                }

                if (comic?.comic?.issueNumber != null) {
                    issueNumberComicTextView.text = comic.comic.issueNumber.toInt().toString()
                } else {
                    issueNumberComicTextView.visibility = GONE
                    issueNumberTitleTextView.visibility = GONE
                }

                formatComicTextView.text = comic?.comic?.format ?: ""
                pageCountComicTextView.text = comic?.comic?.pageCount.toString()

                val saleDay = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val zonedDateTime = UtilData.createZoneDataForItemOfList(comic?.comic?.saleDay)
                    "${(zonedDateTime?.dayOfMonth)?.formatDayOrMonth()}." +
                            "${(zonedDateTime?.monthValue)?.formatDayOrMonth()}." +
                            "${zonedDateTime?.year}"
                } else {
                    ""
                }
                saleDayComicTextView.text = saleDay

                comic?.writers?.forEach {
                    if (writersComicTextView.text.isNotEmpty()) {
                        var nameWriter: String = writersComicTextView.text.toString()
                        nameWriter = "$nameWriter, ${it.name}"
                        writersComicTextView.text = nameWriter
                    } else {
                        writersComicTextView.text = it.name
                    }
                }

                if (writersComicTextView.text.isEmpty()) {
                    writersComicTitleTextView.visibility = GONE
                    writersComicTextView.visibility = GONE
                }

                comic?.painters?.forEach {
                    if (paintersComicTextView.text.isNotEmpty()) {
                        var namePainter: String = paintersComicTextView.text.toString()
                        namePainter = "$namePainter, ${it.name}"
                        paintersComicTextView.text = namePainter
                    } else {
                        paintersComicTextView.text = it.name
                    }
                }

                if (paintersComicTextView.text.isEmpty()) {
                    paintersComicTitleTextView.visibility = GONE
                    paintersComicTextView.visibility = GONE
                }

                priceComicTextView.text = comic?.comic?.price.toString()

                detailComicButton.setOnClickListener {
                    comic?.comic?.let {
                        val uri: Uri = Uri.parse(comic.comic.urlDetail)
                        val internetComicDetailIntent = Intent(
                            ACTION_VIEW, uri
                        )
                        startActivity(internetComicDetailIntent)
                    }
                }
            }
        }
    }


    private fun Int.formatDayOrMonth() = "%02d".format(this)
}