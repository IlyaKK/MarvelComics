package com.test.marvelcomics.ui.screens.list_comics

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.test.marvelcomics.databinding.ListComicsFragmentBinding
import com.test.marvelcomics.ui.screens.list_comics.view_model.ListComicsViewModel

class ListComicsFragment : Fragment() {
    private lateinit var binding: ListComicsFragmentBinding

    companion object {
        fun newInstance() = ListComicsFragment()
    }

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

    }

}