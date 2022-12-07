package com.example.mvvmovie.ui.fragments


import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mvvmovie.databinding.FragmentHomeBinding
import com.example.mvvmovie.db.databse.MovieDB
import com.example.mvvmovie.db.repository.DbRepository
import com.example.mvvmovie.model.movies.Result
import com.example.mvvmovie.model.upcoming.Upcoming
import com.example.mvvmovie.network.MovieRepository
import com.example.mvvmovie.network.RetrofitClient
import com.example.mvvmovie.ui.MainActivity
import com.example.mvvmovie.ui.MovieViewModel
import com.example.mvvmovie.ui.MovieViewModelFactory
import com.example.mvvmovie.ui.OnItemClickListener
import com.example.mvvmovie.ui.popularMovies.PopularMovieDetailsActivity
import com.example.mvvmovie.ui.upcoming.OnUpClickListener
import com.example.mvvmovie.ui.upcoming.UpComingAdapter
import com.example.mvvmovie.ui.upcoming.UpComingViewModel
import com.example.mvvmovie.ui.upcoming.UpComingViewModelFactory
import com.example.mvvmovie.utils.Resource
import com.example.mvvmovie.utils.SessionManager
import com.example.mvvmovie.utils.showAlert
import com.example.mvvmovie.utils.toast


class HomeFragment : Fragment() {

    private lateinit var viewModel: MovieViewModel
    private lateinit var upviewModel: UpComingViewModel
    private lateinit var recAdapter: MovieFragmentAdapter
    private lateinit var upcomingAdapter: UpComingAdapter

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val retrofitService = RetrofitClient.apiInterface
        val mainRepository = MovieRepository(retrofitService)
        val moviessRepository = DbRepository(MovieDB(requireContext()))
        val appCtx = requireActivity().application as Application
        val viewModelProviderFactory = MovieViewModelFactory(appCtx, mainRepository,moviessRepository)

        val upViewModelProviderFactory = UpComingViewModelFactory(appCtx, mainRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory)[MovieViewModel::class.java]
        upviewModel = ViewModelProvider(this, upViewModelProviderFactory)[UpComingViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        setupRecomendations()
        setupTrending()

        viewModel.popularMovies.observe(viewLifecycleOwner, Observer{response ->
            when(response){
                is Resource.Success -> {
                    response.data?.let { movieResponse ->
                        recAdapter.setData(movieResponse.results.toList())
                    }
                }
                is Resource.Error -> {
                    response.message?.let { message ->
                        requireContext().showAlert(
                            title = "Oops !",
                            message = message,
                            negativeClick = {
                                viewModel.getAllPopMovies()
                            }
                        )
                    }
                }
                is Resource.Loading -> {

                }
            }
        })


        upviewModel.movieDetails.observe(viewLifecycleOwner, Observer{response ->
            when(response){
                is Resource.Success -> {
                    response.data?.let { movieResponse ->
                        upcomingAdapter.setData(movieResponse.results.toList())
                    }
                }
                is Resource.Error -> {
                    response.message?.let { message ->

                    }
                }
                is Resource.Loading -> {

                }
            }
        })

        binding.popular.popAll.setOnClickListener{
            val intent = Intent(activity, MainActivity::class.java)
            startActivity(intent)
        }





        return root
    }



    fun setupRecomendations(){
        recAdapter = MovieFragmentAdapter(OnItemClickListener { item: Result, _: Int ->
            SessionManager.saveMovieId(item)
            val intent = Intent(activity, PopularMovieDetailsActivity::class.java)
            startActivity(intent)
        })
        binding.popular.rvPop.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = recAdapter
        }
    }
    fun setupTrending(){
        upcomingAdapter = UpComingAdapter(OnUpClickListener { item: Upcoming, _: Int ->
            SessionManager.saveUpcomingId(item)
            val intent = Intent(activity, PopularMovieDetailsActivity::class.java)
            startActivity(intent)
        })
        binding.upcoming.rvUp.apply {
            layoutManager = GridLayoutManager(context, 4,GridLayoutManager.HORIZONTAL,false)
            adapter = upcomingAdapter
        }
    }


}