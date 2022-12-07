package com.example.mvvmovie.ui.movieVideos

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mvvmovie.model.videos.VideoResponse
import com.example.mvvmovie.network.MovieRepository
import com.example.mvvmovie.utils.Resource
import com.example.mvvmovie.utils.isInternetAvailable
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class VideoViewModel(application: Application, private val repository: MovieRepository) : AndroidViewModel(application)  {

    val videoDetails : MutableLiveData<Resource<VideoResponse>> = MutableLiveData()
    var videoResponse: VideoResponse? = null


    fun getAllVideos(id:Int) = viewModelScope.launch {
        safeCallVideo(id)
    }

    private suspend fun safeCallVideo(id:Int){
        videoDetails.postValue(Resource.Loading())
        try {
            if (isInternetAvailable()){
                val response = repository.getAllVideos(id)
                videoDetails.postValue(handleVideoResponse(response))
            }else{
                videoDetails.postValue(Resource.Error("No Internet Connection"))
            }
        }catch (t:Throwable){
            when(t){
                is IOException -> videoDetails.postValue(Resource.Error("Network Failure"))
                else -> videoDetails.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private fun handleVideoResponse(response: Response<VideoResponse>): Resource<VideoResponse>? {
        if(response.isSuccessful) {
            response.body()?.let { resultResponse ->

                if(videoResponse == null) {
                    videoResponse = resultResponse
                } else {
                    val oldMovieList = videoResponse?.results
                    val newMovieList = resultResponse.results
                    oldMovieList?.addAll(newMovieList)
                }
                return Resource.Success(videoResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }



}