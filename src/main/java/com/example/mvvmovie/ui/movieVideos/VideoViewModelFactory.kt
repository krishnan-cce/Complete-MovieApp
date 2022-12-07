package com.example.mvvmovie.ui.movieVideos

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mvvmovie.network.MovieRepository



class VideoViewModelFactory(
    val app : Application,
    private val repository: MovieRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return VideoViewModel(app,repository) as T
    }
}