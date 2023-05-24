package com.example.apitiempo.data


import com.squareup.moshi.Json

data class Sys(
    @Json(name = "country")
    val country: String?,
    @Json(name = "id")
    val id: Int?,
    @Json(name = "sunrise")
    val sunrise: Long?,
    @Json(name = "sunset")
    val sunset: Long?,
    @Json(name = "type")
    val type: Int?
)