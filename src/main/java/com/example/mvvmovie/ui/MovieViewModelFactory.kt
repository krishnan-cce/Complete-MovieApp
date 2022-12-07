package com.example.mvvmovie.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

import com.example.mvvmovie.network.MovieRepository

class MovieViewModelFactory(
    val app : Application,
    private val repository: MovieRepository

    ) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MovieViewModel(app,repository) as T
        }
    }
