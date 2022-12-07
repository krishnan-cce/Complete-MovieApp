package com.example.mvvmovie.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.core.content.ContextCompat.startActivity
import com.example.mvvmovie.model.movies.Result
import com.example.mvvmovie.model.recomendations.Recomendations
import com.example.mvvmovie.model.upcoming.Upcoming
import com.example.mvvmovie.ui.popularMovies.PopularMovieDetailsActivity
import com.example.mvvmovie.utils.Contsants.MOVIEID
import com.example.mvvmovie.utils.Contsants.RECOMENDED_MOVIEID
import com.example.mvvmovie.utils.Contsants.SIMILAR_MOVIEID
import com.example.mvvmovie.utils.Contsants.UPCOMING_ID

class SessionManager {
    companion object {
        private lateinit var sharedPref: SharedPreferences
        private lateinit var editor: SharedPreferences.Editor
        private const val PREF_NAME = "MovieDB"


        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context

        fun initializeContext(context: Context) {
            Companion.context = context
            sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            editor = sharedPref.edit()
        }


        var movieId: Int
            get() = (sharedPref.getInt(MOVIEID, 0) ?: "") as Int
            set(s) = sharedPref.edit().putInt(MOVIEID, s).apply()

        fun saveMovieId(item : Result){
            movieId = item.id

        }

        var similarMovieId: Int
            get() = (sharedPref.getInt(SIMILAR_MOVIEID, 0) ?: "") as Int
            set(s) = sharedPref.edit().putInt(SIMILAR_MOVIEID, s).apply()

        fun saveSimilarMovieId(item : com.example.mvvmovie.model.similar.Result){
            similarMovieId = item.id

        }

        var recomendedMovieId: Int
            get() = (sharedPref.getInt(RECOMENDED_MOVIEID, 0) ?: "") as Int
            set(s) = sharedPref.edit().putInt(RECOMENDED_MOVIEID, s).apply()

        fun saveRecMovieId(item : Recomendations){
            recomendedMovieId = item.id

        }

        var upcomingId: Int
            get() = (sharedPref.getInt(UPCOMING_ID, 0) ?: "") as Int
            set(s) = sharedPref.edit().putInt(UPCOMING_ID, s).apply()

        fun saveUpcomingId(item : Upcoming){
            movieId = item.id

        }

    }
}