package com.example.mvvmovie.ui.movieRecomended

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
import com.example.mvvmovie.model.videos.VideoResponse
import com.example.mvvmovie.network.MovieRepository
import com.example.mvvmovie.utils.Resource
import com.example.mvvmovie.utils.isInternetAvailable
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class RecomendedViewModel(application: Application, private val repository: MovieRepository) : AndroidViewModel(application) {

    val recDetails : MutableLiveData<Resource<RecomendationResponse>> = MutableLiveData()
    var recResponse: RecomendationResponse? = null


    fun getAllRec(id:Int) = viewModelScope.launch {
        safeCallRec(id)
    }

    private suspend fun safeCallRec(id:Int){
        recDetails.postValue(Resource.Loading())
        try {
            if (isInternetAvailable()){
                val response = repository.getAllRecomendations(id)
                recDetails.postValue(handleRecResponse(response))
            }else{
                recDetails.postValue(Resource.Error("No Internet Connection"))
            }
        }catch (t:Throwable){
            when(t){
                is IOException -> recDetails.postValue(Resource.Error("Network Failure"))
                else -> recDetails.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private fun handleRecResponse(response: Response<RecomendationResponse>): Resource<RecomendationResponse>? {
        if(response.isSuccessful) {
            response.body()?.let { resultResponse ->

                if(recResponse == null) {
                    recResponse = resultResponse
                } else {
                    val oldMovieList = recResponse?.results
                    val newMovieList = resultResponse.results
                    oldMovieList?.addAll(newMovieList)
                }
                return Resource.Success(recResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }



}