package com.example.apitiempo.data

import com.example.apitiempo.data.network.RetrofitHelper

class Repositorio() {

    private val retrofit= RetrofitHelper.getRetrofit()

    suspend fun getTiempo(lat:Double,lon:Double,id:String,lang:String,units:String)=retrofit.dameTiempo(lat,lon,id,lang,units)
}