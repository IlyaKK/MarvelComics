package com.test.marvelcomics.ui.screens.list_comics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.test.marvelcomics.databinding.ListComicsFragmentBinding
import com.test.marvelcomics.ui.screens.list_comics.recycler_view.ListComicsAdapter
import com.test.marvelcomics.ui.screens.list_comics.view_model.ListComicsViewModel

class ListComicsFragment : Fragment() {
    companion object {
        fun newInstance() = ListComicsFragment()
    }

    private val listComicsAdapter by lazy { ListComicsAdapter() }
    private lateinit var linearLayoutManager: LinearLayoutManager

    private lateinit var binding: ListComicsFragmentBinding

    private lateinit var listComicsViewModel: ListComicsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ListComicsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listComicsViewModel = ViewModelProvider(this)[ListComicsViewModel::class.java]
        initializeListComicsRecycleView()
        initializeReceiveListMarvelComics()
    }

    private fun initializeListComicsRecycleView() {
        linearLayoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.listsComicsRecyclerView.layoutManager = linearLayoutManager
        binding.listsComicsRecyclerView.adapter = listComicsAdapter

    }

    private fun initializeReceiveListMarvelComics() {
        listComicsViewModel.getPublishedMarvelComics(
            nowData = "1949-01-01,2022-04-05",
        )

        initializeScrollingRecyclerView()
        listComicsViewModel.listMarvelComicsLiveData.observe(viewLifecycleOwner) { newListComic ->
            if (listComicsAdapter.currentList.isEmpty()) {
                listComicsAdapter.submitList(newListComic)
            } else {
                val listComics = listComicsAdapter.currentList.toMutableList()
                listComics.addAll(newListComic)
                listComicsAdapter.submitList(listComics)
            }
        }
    }

    private fun initializeScrollingRecyclerView() {
        binding.listsComicsRecyclerView.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val totalItemCount = recyclerView.layoutManager?.itemCount
                val lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition()
                if (totalItemCount == lastVisibleItemPosition + 1) {
                    listComicsViewModel.getPublishedMarvelComics(
                        nowData = "1949-01-01,2022-04-05",
                        offset = totalItemCount - 1
                    )
                }
            }
        })
    }
}