package com.example.apitiempo.data.network

import com.example.apitiempo.data.elTiempo
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("weather")
    suspend fun dameTiempo(
        @Query("lat" )lat:Double,
        @Query("lon")lon:Double,
        @Query("appid")id:String,
        @Query("lang")lang:String,
        @Query("units")units: String
    ): Response<elTiempo>


}