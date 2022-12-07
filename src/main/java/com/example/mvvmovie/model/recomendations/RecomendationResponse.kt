package com.example.mvvmovie.model.recomendations


import com.google.gson.annotations.SerializedName

data class RecomendationResponse(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val results: MutableList<Recomendations>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)