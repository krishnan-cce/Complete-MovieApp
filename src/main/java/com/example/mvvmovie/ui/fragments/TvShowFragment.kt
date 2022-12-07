package com.example.mvvmovie.ui.fragments

import android.app.Application
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.mvvmovie.R
import com.example.mvvmovie.databinding.FragmentHomeBinding
import com.example.mvvmovie.databinding.FragmentTvShowBinding
import com.example.mvvmovie.db.databse.MovieDB
import com.example.mvvmovie.db.repository.DbRepository
import com.example.mvvmovie.network.MovieRepository
import com.example.mvvmovie.network.RetrofitClient
import com.example.mvvmovie.ui.MovieViewModel
import com.example.mvvmovie.ui.MovieViewModelFactory
import com.example.mvvmovie.ui.upcoming.UpComingAdapter
import com.example.mvvmovie.ui.upcoming.UpComingViewModel
import com.example.mvvmovie.ui.upcoming.UpComingViewModelFactory


class TvShowFragment : Fragment(R.layout.fragment_tv_show) {

    private lateinit var viewModel: MovieViewModel

    private var _binding: FragmentTvShowBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val retrofitService = RetrofitClient.apiInterface
        val mainRepository = MovieRepository(retrofitService)
        val moviessRepository = DbRepository(MovieDB(requireContext()))
        val appCtx = requireActivity().application as Application
        val viewModelProviderFactory = MovieViewModelFactory(appCtx, mainRepository, moviessRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory)[MovieViewModel::class.java]

        _binding = FragmentTvShowBinding.inflate(inflater, container, false)


        val root: View = binding.root


        return root

    }


}