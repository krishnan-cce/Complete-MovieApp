package com.example.mvvmovie.model.upcoming


import com.google.gson.annotations.SerializedName

data class UpComingResponse(
    @SerializedName("dates")
    val dates: Dates,
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val results: MutableList<Upcoming>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)