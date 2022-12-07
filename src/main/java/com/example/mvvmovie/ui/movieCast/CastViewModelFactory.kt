package com.example.mvvmovie.ui.movieCast

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mvvmovie.network.MovieRepository
import com.example.mvvmovie.ui.popularMovies.PopularMovieViewModel

class CastViewModelFactory(
    val app : Application,
    private val repository: MovieRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CastViewModel(app,repository) as T
    }
}