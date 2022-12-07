package com.example.mvvmovie.ui.similarMovies

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mvvmovie.MovieApplication
import com.example.mvvmovie.model.recomendations.RecomendationResponse
import com.example.mvvmovie.model.similar.SimilarResponse
import com.example.mvvmovie.network.MovieRepository
import com.example.mvvmovie.utils.Resource
import com.example.mvvmovie.utils.isInternetAvailable
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class SimilarViewModel(application: Application, private val repository: MovieRepository) : AndroidViewModel(application) {

    val similarMovieDetails : MutableLiveData<Resource<SimilarResponse>> = MutableLiveData()
    var similarMovieResponse: SimilarResponse? = null

    fun getAllSimilarMovies(id:Int) = viewModelScope.launch {
        safeCallSimilarMovies(id)
    }

    private suspend fun safeCallSimilarMovies(id:Int){
        similarMovieDetails.postValue(Resource.Loading())
        try {
            if (isInternetAvailable()){
                val response = repository.getAllSimilarMovies(id)
                similarMovieDetails.postValue(handleSimilarMovieResponse(response))
            }else{
                similarMovieDetails.postValue(Resource.Error("No Internet Connection"))
            }
        }catch (t:Throwable){
            when(t){
                is IOException -> similarMovieDetails.postValue(Resource.Error("Network Failure"))
                else -> similarMovieDetails.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private fun handleSimilarMovieResponse(response: Response<SimilarResponse>): Resource<SimilarResponse>? {
        if(response.isSuccessful) {
            response.body()?.let { resultResponse ->

                if(similarMovieResponse == null) {
                    similarMovieResponse = resultResponse
                } else {
                    val oldMovieList = similarMovieResponse?.results
                    val newMovieList = resultResponse.results
                    oldMovieList?.addAll(newMovieList)
                }
                return Resource.Success(similarMovieResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }


}