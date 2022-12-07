package com.example.mvvmovie.model.casts


import com.google.gson.annotations.SerializedName

data class CastResponse(
    @SerializedName("cast")
    val cast: MutableList<Cast>,
    @SerializedName("crew")
    val crew: MutableList<Crew>,
    @SerializedName("id")
    val id: Int
)