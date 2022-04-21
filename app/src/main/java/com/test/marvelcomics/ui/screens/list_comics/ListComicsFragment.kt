package com.test.marvelcomics.ui.screens.list_comics

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.test.marvelcomics.Injection
import com.test.marvelcomics.databinding.ListComicsFragmentBinding
import com.test.marvelcomics.domain.entity.database.ComicWithWritersAndPainters
import com.test.marvelcomics.ui.screens.list_comics.recycler_view.ListComicsAdapter
import com.test.marvelcomics.ui.view_models.SharedComicViewModel
import com.test.marvelcomics.ui.view_models.view_model_list_comics.ListComicsViewModel
import com.test.marvelcomics.ui.view_models.view_model_list_comics.UiAction
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ListComicsFragment() : Fragment() {
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
            this,
            Injection.provideViewModelFactory(owner = requireActivity(), context = requireContext())
        )[ListComicsViewModel::class.java]
    }

    private val sharedComicViewModel: SharedComicViewModel by lazy {
        ViewModelProvider(requireActivity())[SharedComicViewModel::class.java]
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
    }

    private fun initializeListComicsRecycleView() {
        linearLayoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.listsComicsRecyclerView.layoutManager = linearLayoutManager
        binding.listsComicsRecyclerView.adapter = listComicsAdapter
        listComicsAdapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.ALLOW
        listComicsAdapter.setOnCardClickListener(
            object : ListComicsAdapter.ListenerCardComicClick {
                override fun onComicCardClickListener(comic: ComicWithWritersAndPainters?) {
                    sharedComicViewModel.comicMutableLiveData.value = comic
                    controller?.displayComicDetail()
                }
            })

        listComicsViewModel.accept(UiAction.ShowComics(dataRange = "1949-01-01,2022-04-05"))

        lifecycleScope.launch {
            listComicsViewModel.pagingDataFlow.collectLatest(listComicsAdapter::submitData)
        }
    }


    private fun checkInternet(): Boolean {
        val conn = requireActivity().getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo: NetworkInfo? = conn.activeNetworkInfo
        return networkInfo?.isConnected == true
    }

    override fun onDestroy() {
        controller = null
        super.onDestroy()
    }

    internal interface Controller {
        fun displayComicDetail()
    }
}