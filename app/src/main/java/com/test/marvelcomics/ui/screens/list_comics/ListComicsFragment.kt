package com.test.marvelcomics.ui.screens.list_comics

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.core.util.Pair
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.test.marvelcomics.Injection
import com.test.marvelcomics.R
import com.test.marvelcomics.databinding.ListComicsFragmentBinding
import com.test.marvelcomics.domain.entity.database.ComicWithWritersAndPainters
import com.test.marvelcomics.ui.screens.list_comics.recycler_view.ComicsDownloadStateAdapter
import com.test.marvelcomics.ui.screens.list_comics.recycler_view.ListComicsAdapter
import com.test.marvelcomics.ui.view_models.SharedComicViewModel
import com.test.marvelcomics.ui.view_models.view_model_list_comics.ListComicsViewModel
import com.test.marvelcomics.ui.view_models.view_model_list_comics.UiAction
import com.test.marvelcomics.util.UtilData
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.*

class ListComicsFragment : Fragment() {
    companion object {
        fun newInstance(): ListComicsFragment {
            return ListComicsFragment()
        }
    }

    private val listComicsAdapter by lazy { ListComicsAdapter() }
    private lateinit var linearLayoutManager: LinearLayoutManager

    private lateinit var binding: ListComicsFragmentBinding

    private val listComicsViewModel: ListComicsViewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            Injection.provideViewModelFactory(owner = requireActivity(), context = requireContext())
        )[ListComicsViewModel::class.java]
    }

    private val sharedComicViewModel: SharedComicViewModel by lazy {
        ViewModelProvider(requireActivity())[SharedComicViewModel::class.java]
    }

    private var controller: Controller? = null

    private lateinit var dateRangePicker: MaterialDatePicker<Pair<Long, Long>>

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
        setHasOptionsMenu(true)
        binding = ListComicsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialiseTopAppBar()
        initializeListComicsRecycleView()
        initialiseDataRangeCreateDataPicker()
    }

    private fun initialiseTopAppBar() {
        controller?.setTopAppBar()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_comic_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.filter_data_range_item) {
            dateRangePicker.show(childFragmentManager, "tag")
            return true
        }
        return false
    }

    private fun initialiseDataRangeCreateDataPicker() {
        lifecycleScope.launch {
            listComicsViewModel.stateRangeData.map {
                it.dataRange
            }.distinctUntilChanged()
                .collect {
                    val pairDataRange = UtilData.createPairDataRange(it)
                    dateRangePicker = MaterialDatePicker.Builder.dateRangePicker()
                        .setSelection(
                            pairDataRange
                        )
                        .build()

                    dateRangePicker.addOnPositiveButtonClickListener { dataRangeDataPicker ->
                        val firstDate = dataRangeDataPicker.first
                        val secondDate = dataRangeDataPicker.second
                        listComicsViewModel.accept(
                            UiAction.ShowComics(
                                UtilData.createStringDataRange(
                                    Date(firstDate),
                                    Date(secondDate)
                                )
                            )
                        )
                    }
                }
        }
    }

    private fun initializeListComicsRecycleView() {
        linearLayoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.listsComicsRecyclerView.layoutManager = linearLayoutManager

        val header = ComicsDownloadStateAdapter { listComicsAdapter.retry() }
        binding.listsComicsRecyclerView.adapter = listComicsAdapter.withLoadStateHeaderAndFooter(
            header = header,
            footer = ComicsDownloadStateAdapter { listComicsAdapter.retry() }
        )

        listComicsAdapter.setOnCardClickListener(
            object : ListComicsAdapter.ListenerCardComicClick {
                override fun onComicCardClickListener(comic: ComicWithWritersAndPainters?) {
                    sharedComicViewModel.shareComic(comic)
                    controller?.displayComicDetail()
                }
            })

        lifecycleScope.launch {
            listComicsViewModel.pagingDataFlow.collectLatest(listComicsAdapter::submitData)
        }

        lifecycleScope.launch {
            listComicsAdapter.loadStateFlow.collect { loadState ->
                header.loadState = loadState.mediator
                    ?.refresh
                    ?.takeIf {
                        it is LoadState.Error && listComicsAdapter.itemCount > 0
                    }
                    ?: loadState.prepend

                val isListEmpty =
                    (loadState.refresh is LoadState.NotLoading || loadState.source.refresh is LoadState.NotLoading) && listComicsAdapter.itemCount == 0
                binding.emptyListTextView.isVisible = isListEmpty
                binding.listsComicsRecyclerView.isVisible =
                    loadState.source.refresh is LoadState.NotLoading || loadState.mediator?.refresh is LoadState.NotLoading
                binding.progressBar.isVisible = loadState.mediator?.refresh is LoadState.Loading
                binding.retryButton.isVisible =
                    loadState.mediator?.refresh is LoadState.Error && listComicsAdapter.itemCount == 0
                val errorState = loadState.source.append as? LoadState.Error
                    ?: loadState.source.prepend as? LoadState.Error
                    ?: loadState.append as? LoadState.Error
                    ?: loadState.prepend as? LoadState.Error
                errorState?.let {
                    Snackbar.make(
                        binding.root,
                        "\uD83D\uDE28 Warning! ${it.error}",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        }

        binding.retryButton.setOnClickListener { listComicsAdapter.retry() }
    }

    override fun onDestroy() {
        controller = null
        super.onDestroy()
    }

    internal interface Controller {
        fun displayComicDetail()
        fun setTopAppBar()
    }
}