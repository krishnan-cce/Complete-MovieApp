package com.example.mvvmovie.ui.movieRecomended

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mvvmovie.network.MovieRepository



class RecomendedViewModelFactory(
    val app : Application,
    private val repository: MovieRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RecomendedViewModel(app,repository) as T
    }
}