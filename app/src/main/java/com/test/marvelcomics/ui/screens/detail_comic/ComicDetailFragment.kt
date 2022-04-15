package com.test.marvelcomics.ui.screens.detail_comic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.test.marvelcomics.databinding.ComicDetailFragmentBinding
import com.test.marvelcomics.ui.view_models.SharedComicViewModel

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
                titleComicTextView.text = comic.title
                descriptionComicTextView.text = comic.description
                issueNumberComicTextView.text = comic.issueNumber.toString()
                formatComicTextView.text = comic.format
                pageCountComicTextView.text = comic.pageCount.toString()
                saleDayComicTextView.text = comic.saleDay

                comic.writers?.forEach {
                    if (writersComicTextView.text.isEmpty()) {
                        writersComicTextView.text = it
                    } else {
                        val oldWritersComic: String = writersComicTextView.text as String
                        val newWritersComic = "$oldWritersComic, $it"
                        writersComicTextView.text = newWritersComic
                    }
                }

                comic.painters?.forEach {
                    if (paintersComicTextView.text.isEmpty())
                        paintersComicTextView.text = it
                    else {
                        val oldPaintersComic: String = paintersComicTextView.text as String
                        val newPaintersComic = "$oldPaintersComic, $it"
                        paintersComicTextView.text = newPaintersComic
                    }
                }
                priceComicTextView.text = comic.price.toString()

                Glide
                    .with(root)
                    .load(
                        (comic.imagePath.replace("http", "https"))
                                + "/portrait_fantastic.jpg"
                    )
                    .override(168, 252)
                    .into(pictureComicImageView)
            }
        }
    }
}