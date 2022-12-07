package com.example.mvvmovie.network

import com.example.mvvmovie.model.casts.CastResponse
import com.example.mvvmovie.model.detail.DetailResponse
import com.example.mvvmovie.model.movies.Movies
import com.example.mvvmovie.model.person.PersonResponse
import com.example.mvvmovie.model.recomendations.RecomendationResponse
import com.example.mvvmovie.model.similar.SimilarResponse
import com.example.mvvmovie.model.upcoming.UpComingResponse
import com.example.mvvmovie.model.videos.VideoResponse
import com.example.mvvmovie.utils.Contsants.APIKEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {

    @GET("movie/popular?")
    suspend fun getPopularMovies(
        @Query("page")
        pageNumber: Int = 1,
        @Query("api_key")
        apiKey: String = APIKEY
    ): Response<Movies>

    @GET("search/movie?")
    suspend fun searchMovies(
        @Query("page")
        pageNumber: Int = 1,
        @Query("query")
        searchMovie: String,
//        @Query("include_adult")
//        adultMovies: Boolean,
        @Query("api_key")
        apiKey: String = APIKEY
    ): Response<Movies>


    @GET("movie/{movie_id}?")
    suspend fun getMovieDetails(@Path("movie_id") id : Int, @Query("api_key") api_key : String = APIKEY) : Response<DetailResponse>

    @GET("movie/{movie_id}/credits?")
    suspend fun getMovieCasts(@Path("movie_id") id : Int, @Query("api_key") api_key : String= APIKEY) : Response<CastResponse>

    @GET("movie/{movie_id}/videos?")
    suspend fun getMovieVideos(@Path("movie_id") id : Int, @Query("api_key") api_key : String= APIKEY) : Response<VideoResponse>

    @GET("movie/{movie_id}/recommendations?")
    suspend fun getMovieRecomendations(@Path("movie_id") id : Int, @Query("api_key") api_key : String= APIKEY) : Response<RecomendationResponse>

    @GET("movie/{movie_id}/similar?")
    suspend fun getSimilarMovie(@Path("movie_id") id : Int, @Query("api_key") api_key : String= APIKEY) : Response<SimilarResponse>


    @GET("person/{person_id}?")
    suspend fun getPersonDetails(@Path("person_id") id : Int, @Query("api_key") api_key : String) : Response<PersonResponse>


    @GET("movie/upcoming?")
    suspend fun getUpcoming(
        @Query("page")
        pageNumber: Int = 1,
        @Query("api_key")
        apiKey: String = APIKEY
    ): Response<UpComingResponse>

}