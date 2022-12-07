package com.example.mvvmovie.ui.popularMovies

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mvvmovie.MovieApplication
import com.example.mvvmovie.model.detail.DetailResponse
import com.example.mvvmovie.model.detail.Genre
import com.example.mvvmovie.network.MovieRepository
import com.example.mvvmovie.utils.Resource
import com.example.mvvmovie.utils.isInternetAvailable
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class PopularMovieViewModel(application: Application,private val repository: MovieRepository) : AndroidViewModel(application) {

    val movieDetails : MutableLiveData<Resource<DetailResponse>> = MutableLiveData()
    var detailResponse: DetailResponse? = null
    private var genreLiveData = MutableLiveData<List<Genre>>()




    fun getAllPopMovies(id:Int) = viewModelScope.launch {
        safeCallMovieDetail(id)
    }
    fun getAllGenre(): LiveData<List<Genre>> {
        return genreLiveData
    }


    private suspend fun safeCallMovieDetail(id:Int){
        movieDetails.postValue(Resource.Loading())
        try {
            if (isInternetAvailable()){
                val response = repository.getMovieDetails(id)
                movieDetails.postValue(handleDetailResponse(response))
                genreLiveData.postValue(response.body()!!.genres)
            }else{
                movieDetails.postValue(Resource.Error("No Internet Connection"))
            }
        }catch (t:Throwable){
            when(t){
                is IOException -> movieDetails.postValue(Resource.Error("Network Failure"))
                else -> movieDetails.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private fun handleDetailResponse(response: Response<DetailResponse>): Resource<DetailResponse>? {
        if(response.isSuccessful) {
            response.body()?.let { resultResponse ->

                if(detailResponse == null) {
                    detailResponse = resultResponse
                } else {
                    val oldMovieList = detailResponse
                    oldMovieList?.let {
                        resultResponse
                    } //.addAll(resultResponse)
                }
                return Resource.Success(detailResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }




}