package com.example.apitiempo.ui

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.apitiempo.data.Repositorio
import com.example.apitiempo.data.elTiempo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyViewModel(): ViewModel() {
    private val repositorio=Repositorio()

    val TiempoLiveData=MutableLiveData<elTiempo?>()

    fun getTiempo(latitud:Double,longitud:Double){

        CoroutineScope(Dispatchers.IO).launch {
            val response=repositorio.getTiempo(latitud,longitud,"3ce1a70f037beb728094dac99524ab84","es", "metric" )
            if (response.isSuccessful){
                val miRespuesta=response.body()
                TiempoLiveData.postValue(miRespuesta)
            }
        }
    }
}