package com.example.mvvmovie.ui

import android.app.Application
import android.graphics.Movie
import androidx.lifecycle.*
import com.example.mvvmovie.model.movies.Movies
import com.example.mvvmovie.model.movies.Result
import kotlinx.coroutines.*
import com.example.mvvmovie.network.MovieRepository
import com.example.mvvmovie.utils.Resource
import com.example.mvvmovie.utils.SessionManager
import com.example.mvvmovie.utils.isInternetAvailable
import retrofit2.Response
import java.io.IOException

class MovieViewModel(
    app:Application,
    private val repository: MovieRepository
    ) : AndroidViewModel(app) {


    val popularMovies: MutableLiveData<Resource<Movies>> = MutableLiveData()

    var popularMoviesPage = 1
    var popularMovieResponse: Movies? = null

    val searchMovies: MutableLiveData<Resource<Movies>> = MutableLiveData()
    var searchMoviesPage = 1
    var searchMoviesResponse: Movies? = null
    var newSearchQuery:String? = null
    var oldSearchQuery:String? = null

    init {

        getAllPopMovies()
    }


    fun getAllPopMovies() = viewModelScope.launch {
        safeCallPopMovie()
    }

    fun getSearchedMovies(searchQuery:String) = viewModelScope.launch {
        searchMovies(searchQuery)
    }




    private suspend fun safeCallPopMovie(){
        popularMovies.postValue(Resource.Loading())
        try {
            if (isInternetAvailable()){
                val response = repository.getPopularMovies(popularMoviesPage)
                popularMovies.postValue(handlePopMovieResponse(response))

                val movies = popularMovies.value!!.data


            }else{
                popularMovies.postValue(Resource.Error("No Internet Connection"))
            }
        }catch (t : Throwable){
            when(t){
                is IOException -> popularMovies.postValue(Resource.Error("Network Failure"))
                else -> popularMovies.postValue(Resource.Error("Conversion Error"))
            }
        }

    }

    private fun handlePopMovieResponse(response: Response<Movies>) : Resource<Movies> {
        if(response.isSuccessful) {
            response.body()?.let { resultResponse ->
               popularMoviesPage++
                if(popularMovieResponse == null) {
                    popularMovieResponse = resultResponse

                } else {
                    val oldMovieList = popularMovieResponse?.results
                    val newMovieList = resultResponse.results
                    oldMovieList?.addAll(newMovieList)
                }
                return Resource.Success(popularMovieResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }



    private suspend fun searchMovies(searchQuery:String){
        newSearchQuery = searchQuery
        searchMovies.postValue(Resource.Loading())
        try {
            if (isInternetAvailable()){
                val response = repository.searchMovies(searchMoviesPage,searchQuery)
                searchMovies.postValue(handleSearchRespponse(response))

            }else{
                searchMovies.postValue(Resource.Error("No Internet Connection"))
            }
        }catch (t : Throwable){
            when(t){
                is IOException -> searchMovies.postValue(Resource.Error("Network Failure"))
                else -> searchMovies.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private fun handleSearchRespponse(response: Response<Movies>) : Resource<Movies> {
        if(response.isSuccessful) {
            response.body()?.let { resultResponse ->
                //searchMoviesPage++
                if(searchMoviesResponse == null || newSearchQuery != oldSearchQuery) {
                    searchMoviesPage = 1
                    oldSearchQuery = newSearchQuery
                    searchMoviesResponse = resultResponse
                } else {
                    searchMoviesPage++
                    val oldMovieList = searchMoviesResponse?.results
                    val newMovieList = resultResponse.results
                    oldMovieList?.addAll(newMovieList)
                }
                return Resource.Success(searchMoviesResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }




}