package com.example.mvvmovie.ui.upcoming

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mvvmovie.network.MovieRepository

class UpComingViewModelFactory (
    val app : Application,
    private val repository: MovieRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return UpComingViewModel(app,repository) as T
    }
}