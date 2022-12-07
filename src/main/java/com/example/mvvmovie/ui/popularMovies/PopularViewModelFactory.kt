package com.example.mvvmovie.ui.popularMovies

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mvvmovie.network.MovieRepository
import com.example.mvvmovie.ui.MovieViewModel

class PopularViewModelFactory(
    val app : Application,
    private val repository: MovieRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PopularMovieViewModel(app,repository) as T
    }
}