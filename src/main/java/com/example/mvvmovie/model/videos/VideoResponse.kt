package com.example.mvvmovie.model.videos


import com.google.gson.annotations.SerializedName

data class VideoResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("results")
    val results: MutableList<Videos>
)