package com.example.mvvmovie.ui.upcoming

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mvvmovie.model.detail.DetailResponse
import com.example.mvvmovie.model.upcoming.UpComingResponse
import com.example.mvvmovie.network.MovieRepository
import com.example.mvvmovie.utils.Resource
import com.example.mvvmovie.utils.isInternetAvailable
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class UpComingViewModel(application: Application, private val repository: MovieRepository) : AndroidViewModel(application)  {

    val movieDetails : MutableLiveData<Resource<UpComingResponse>> = MutableLiveData()
    var detailResponse: UpComingResponse? = null

    init {
        getAllUpcoming()
    }
    fun getAllUpcoming() = viewModelScope.launch {
        safeCallUpcoming()
    }

    private suspend fun safeCallUpcoming(){
        movieDetails.postValue(Resource.Loading())
        try {
            if (isInternetAvailable()){
                val response = repository.getUpcoming()
                movieDetails.postValue(handleUpcomingResponse(response))
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

    private fun handleUpcomingResponse(response: Response<UpComingResponse>): Resource<UpComingResponse>? {
        if(response.isSuccessful) {
            response.body()?.let { resultResponse ->

                if(detailResponse == null) {
                    detailResponse = resultResponse
                } else {
                    val oldMovieList = detailResponse?.results
                    val newMovieList = resultResponse.results
                    oldMovieList?.addAll(newMovieList)
                }
                return Resource.Success(detailResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
}