package com.example.mvvmovie.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AbsListView
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvmovie.databinding.ActivityMainBinding

import com.example.mvvmovie.model.movies.Result
import com.example.mvvmovie.network.MovieRepository
import com.example.mvvmovie.network.RetrofitClient
import com.example.mvvmovie.ui.popularMovies.PopularMovieDetailsActivity
import com.example.mvvmovie.utils.Contsants.QUERY_PAGE_SIZE
import com.example.mvvmovie.utils.Resource
import com.example.mvvmovie.utils.SessionManager
import com.example.mvvmovie.utils.showAlert
import com.example.mvvmovie.utils.toast
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var viewModel: MovieViewModel
    private lateinit var movieAdapter : MovieAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val retrofitService = RetrofitClient.apiInterface

        val mainRepository = MovieRepository(retrofitService)


        val viewModelProviderFactory = MovieViewModelFactory(application, mainRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(MovieViewModel::class.java)


        filter()
        searchList()   //rv search
        popularList()  //rv popular
        getSearchList()
        getMovieList()




    }


    private fun filter(){
        var job: Job? = null
            binding.searchLayout.etSearch.addTextChangedListener { editable ->
                job?.cancel()
                job = MainScope().launch {
                    delay(500L)
                    editable.let {
                        if (editable.toString().isNotEmpty()){
                            viewModel.getSearchedMovies(editable.toString())

                        }else if(editable.toString().isEmpty()){
                            getMovieList()
                        }
                    }
                }
            }

    }
    private fun getSearchList(){

        viewModel.searchMovies.observe(this, Observer { response ->
            when(response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { movieResponse ->

                        movieAdapter.setData(movieResponse.results.toList())
                        hideProgressBar()
                        hideErrorMessage()
                        val totalPages =  500 + 2
                        isLastPage = viewModel.searchMoviesPage == totalPages
                        if(isLastPage) {
                            binding.rvPopular.setPadding(0, 0, 0, 0)
                        }
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        //toast("An error occured: $message")
                        hideProgressBar()
                        //loadDataFromCache()
//                        showAlert(
//                            title = "Oops !",
//                            message = message,
//                            negativeClick = {
//                                viewModel.getAllPopMovies()
//                            }
//                        )
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }

    fun getMovieList(){
        viewModel.popularMovies.observe(this, Observer{response ->
            when(response){
                is Resource.Success -> {
                    response.data?.let { movieResponse ->

                        movieAdapter.setData(movieResponse.results.toList())
                        hideProgressBar()
                        hideErrorMessage()
                        val totalPages =  500 + 2
                        isLastPage = viewModel.popularMoviesPage == totalPages
                        if(isLastPage) {
                            binding.rvPopular.setPadding(0, 0, 0, 0)
                        }
                    }
                }
                is Resource.Error -> {
                    response.message?.let { message ->
                        hideProgressBar()
                        //loadDataFromCache()
                        toast("An error occured: $message")
//                        showAlert(
//                            title = "Oops !",
//                            message = message,
//                            negativeClick = {
//                                viewModel.getAllPopMovies()
//                            }
//                        )
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }


    private fun hideProgressBar() {
        binding.progressBar.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
        isLoading = true
    }

    private fun hideErrorMessage() {
        binding.supportLayout.visibility = View.INVISIBLE
        isError = false
    }

    var isError = false
    var isLoading = false
    var isLastPage = false
    var isScrolling = false


    val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as GridLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNoErrors = !isError
            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE
            val shouldPaginate = isNoErrors && isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning &&
                    isTotalMoreThanVisible && isScrolling
            if(shouldPaginate) {


                if(binding.searchLayout.etSearch.text.toString().isEmpty() ){
                     viewModel.getAllPopMovies()
                }else{
                    viewModel.getSearchedMovies(binding.searchLayout.etSearch.text.toString())
                }
                isScrolling = false
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }
    }

    fun popularList(){

        movieAdapter = MovieAdapter(OnItemClickListener{item: Result,_:Int ->
            sendMovieResult(item)
        })
        binding.rvPopular.apply {
            adapter = movieAdapter
            layoutManager = GridLayoutManager(applicationContext,2)
            addOnScrollListener(this@MainActivity.scrollListener)

        }
    }
    private fun sendMovieResult(item: Result) {
        SessionManager.saveMovieId(item)
        val intent = Intent(this, PopularMovieDetailsActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun searchList(){

        movieAdapter = MovieAdapter(OnItemClickListener{item: Result,_:Int ->
            sendMovieResult(item)
        })
        binding.rvPopular.apply {
            adapter = movieAdapter
            layoutManager = GridLayoutManager(applicationContext,2)
            addOnScrollListener(this@MainActivity.scrollListener)
        }
    }


}