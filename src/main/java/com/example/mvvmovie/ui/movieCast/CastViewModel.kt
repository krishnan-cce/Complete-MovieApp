package com.example.mvvmovie.ui.movieCast

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mvvmovie.model.casts.CastResponse
import com.example.mvvmovie.network.MovieRepository
import com.example.mvvmovie.utils.Resource
import com.example.mvvmovie.utils.isInternetAvailable
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class CastViewModel(application: Application, private val repository: MovieRepository) : AndroidViewModel(application) {

    val castDetails : MutableLiveData<Resource<CastResponse>> = MutableLiveData()
    var castResponse: CastResponse? = null


    fun getAllCasts(id:Int) = viewModelScope.launch {
        safeCallCast(id)
    }


    private suspend fun safeCallCast(id:Int){
        castDetails.postValue(Resource.Loading())
        try {
            if (isInternetAvailable()){
                val response = repository.getALlMovieCast(id)
                castDetails.postValue(handleCastResponse(response))
            }else{
                castDetails.postValue(Resource.Error("No Internet Connection"))
            }
        }catch (t:Throwable){
            when(t){
                is IOException -> castDetails.postValue(Resource.Error("Network Failure"))
                else -> castDetails.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private fun handleCastResponse(response: Response<CastResponse>): Resource<CastResponse>? {
        if(response.isSuccessful) {
            response.body()?.let { resultResponse ->

                if(castResponse == null) {
                    castResponse = resultResponse
                } else {
                    val oldMovieList = castResponse?.cast
                    val newMovieList = resultResponse.cast
                    oldMovieList?.addAll(newMovieList)
                }
                return Resource.Success(castResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }


}