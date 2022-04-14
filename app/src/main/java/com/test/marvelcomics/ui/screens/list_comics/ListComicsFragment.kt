package com.test.marvelcomics.ui.screens.list_comics

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.test.marvelcomics.databinding.ListComicsFragmentBinding
import com.test.marvelcomics.domain.entity.Comic
import com.test.marvelcomics.domain.repo.MarvelComicsRepository
import com.test.marvelcomics.ui.screens.list_comics.recycler_view.ListComicsAdapter
import com.test.marvelcomics.ui.view_models.ListComicsViewModelFactory
import com.test.marvelcomics.ui.view_models.view_model_list_comics.ListComicsViewModel

class ListComicsFragment(
    private val comicRepository: MarvelComicsRepository
) : Fragment() {
    companion object {
        fun newInstance(
            comicRepository: MarvelComicsRepository
        ): ListComicsFragment {
            return ListComicsFragment(comicRepository)
        }
    }

    private val listComicsAdapter by lazy { ListComicsAdapter() }
    private lateinit var linearLayoutManager: LinearLayoutManager

    private lateinit var binding: ListComicsFragmentBinding

    private val listComicsViewModel: ListComicsViewModel by lazy {
        ViewModelProvider(
            this,
            ListComicsViewModelFactory(comicRepository)
        ).get(ListComicsViewModel::class.java)
    }

    private var controller: Controller? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is Controller) {
            controller = context
        } else {
            throw IllegalStateException("Activity doesn't have impl ListComicsFragment.Controller interface")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ListComicsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeListComicsRecycleView()
        initializeReceiveListMarvelComics()

        if (savedInstanceState == null) {
            getComics("1949-01-01, 2022-04-05")
        }
    }

    private fun initializeListComicsRecycleView() {
        linearLayoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.listsComicsRecyclerView.layoutManager = linearLayoutManager
        binding.listsComicsRecyclerView.adapter = listComicsAdapter
        listComicsAdapter.setOnCardClickListener(
            object : ListComicsAdapter.ListenerCardComicClick {
                override fun onComicCardClickListener(comic: Comic) {
                    controller?.displayComicDetail(comic)
                }
            })
    }

    private fun initializeReceiveListMarvelComics() {
        initializeScrollingRecyclerView()

        listComicsViewModel.listMarvelComicsLiveData.observe(viewLifecycleOwner) { newListComic ->
            listComicsAdapter.setStateProgressBar(false)
            listComicsAdapter.submitList(newListComic)
        }
    }

    private fun initializeScrollingRecyclerView() {
        binding.listsComicsRecyclerView.addOnScrollListener(
            object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    val totalItemCount = recyclerView.layoutManager?.itemCount
                    val lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition()
                    val progressBarLoadState = listComicsAdapter.getStateProgressBar()
                    if (totalItemCount == lastVisibleItemPosition + 1 && progressBarLoadState != true) {
                        listComicsAdapter.setStateProgressBar(true)
                        getComics("1949-01-01, 2022-04-05", listComicsAdapter.currentList.size)
                    }
                }
            })
    }

    private fun getComics(dataRange: String, offset: Int = 0) {
        val stateInternet = checkInternet()
        listComicsViewModel.getPublishedMarvelComics(
            stateInternet = stateInternet,
            dataRange = dataRange,
            offset = offset
        )
    }

    private fun checkInternet(): Boolean {
        return true
    }

    override fun onDestroy() {
        controller = null
        super.onDestroy()
    }

    internal interface Controller {
        fun displayComicDetail(comic: Comic?)
    }
}