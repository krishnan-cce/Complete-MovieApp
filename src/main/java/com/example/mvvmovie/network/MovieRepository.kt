package com.example.mvvmovie.network


class MovieRepository(private val apiInterface : ApiInterface) {

    suspend fun getPopularMovies(pageNumber : Int) = apiInterface.getPopularMovies(pageNumber)

    suspend fun searchMovies(pageNumber : Int ,searchMovie : String) = apiInterface.searchMovies(pageNumber,searchMovie)

    suspend fun getMovieDetails(id:Int) = apiInterface.getMovieDetails(id)

    suspend fun getALlMovieCast(id:Int ) = apiInterface.getMovieCasts(id)

    suspend fun getAllRecomendations(id:Int) = apiInterface.getMovieRecomendations(id)

    suspend fun getAllVideos(id:Int) = apiInterface.getMovieVideos(id)

    suspend fun getAllSimilarMovies(id:Int) = apiInterface.getSimilarMovie(id)

    suspend fun getPersonById(id:Int,apiKey: String) = apiInterface.getPersonDetails(id,apiKey)

    suspend fun getUpcoming()= apiInterface.getUpcoming()


}