package com.example.mvvmovie.model.similar


import com.example.mvvmovie.model.similar.Result
import com.google.gson.annotations.SerializedName

data class SimilarResponse(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val results: MutableList<Result>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)