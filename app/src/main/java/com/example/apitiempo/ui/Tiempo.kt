package com.example.apitiempo.ui

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.apitiempo.R
import com.example.apitiempo.data.Main
import com.example.apitiempo.data.Weather
import com.example.apitiempo.data.elTiempo
import com.example.apitiempo.databinding.FragmentTiempoBinding
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority


class Tiempo : Fragment() {

    private lateinit var binding: FragmentTiempoBinding
    private val myViewModel: MyViewModel by activityViewModels()
    private lateinit var LocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentTiempoBinding.inflate(inflater,container,false)

        return binding.root
    }


    private fun mostrarUbicacion() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        LocationClient.lastLocation.addOnSuccessListener {
            myViewModel.getTiempo(it.latitude,it.longitude)
        }

    }

    /**
     * Función para pedir permisos
     *  - Si ya fueron aceptados, muestra ubicación o prepara ubicación en directo según el botón pulsado
     *  - Si el usuario los rechazó una vez, se muestra un cuadro de diálogo explicativo
     *  - Si no se han aceptado los permisos, se pide autorización al usuario
     */
    private fun pedirPermisosUbicacion() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
                mostrarUbicacion()
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            showAlertDialog()
        } else {
            somePermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    /**
     * Contrato con el resultado del usuario al solicitar permisos
     *  - si no hay ningún valor falso es que todos fueron aceptados, por lo que se llama a la función correspondiente
     *  - Si se encuentra algún valor falso, no se han aceptado todos los permisos
     */
    private val somePermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) {
        if (!it.containsValue(false)) {
                mostrarUbicacion()
        } else {
            // al menos un permiso no se ha aceptado
            Toast.makeText(requireContext(), "No se han aceptado los permisos", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Cuadro de diálogo para explicar de forma más extendida los permisos al usuario
     */
    private fun showAlertDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage("Los permisos de ubicación permitirán ver tu ubicación en el mapa. Los creadores no almacenan esta información en ningún lugar")
        builder.setNegativeButton("Rechazar", null)
        builder.setPositiveButton("Aceptar") { _, _ ->
            somePermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
        builder.create().show()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        LocationClient=LocationServices.getFusedLocationProviderClient(requireContext())
        pedirPermisosUbicacion()

        myViewModel.TiempoLiveData.observe(viewLifecycleOwner){
            val url="https://openweathermap.org/img/wn/${it?.weather?.get(0)?.icon}@2x.png"
            val icono=it?.weather?.get(0)?.icon
            binding.Ciudad.text=it?.name
            binding.Temperatura.text=it?.main?.temp.toString()
            binding.tempMax.text=it?.main?.tempMax.toString()
            binding.tempMin.text=it?.main?.tempMin.toString()
            binding.SensTermica.text=it?.main?.feelsLike.toString()
            binding.Humedad.text=it?.main?.humidity.toString()
            binding.Presion.text=it?.main?.pressure.toString()
            binding.NivelMAR.text=it?.main?.seaLevel.toString()
            binding.amanecer.text=it?.sys?.sunrise.toString()
            binding.atardecer.text=it?.sys?.sunset.toString()
           Glide.with(requireContext()).load(url).into(binding.iconotiempo)

            if(icono=="01d"||icono=="01n"){
                binding.appTiempo.setBackgroundColor(resources.getColor(R.color.despejado))
                //binding.appTiempo.
            }else if(icono=="09d"||icono=="09n"){
                binding.appTiempo.setBackgroundColor(resources.getColor(R.color.lluvia))
                //binding.appTiempo.textColor
            }else if(icono=="11d"||icono=="11n"){
                binding.appTiempo.setBackgroundColor(resources.getColor(R.color.tormenta))
                //binding.appTiempo.textColor
            }else if(icono=="13d"||icono=="13n"){
                binding.appTiempo.setBackgroundColor(resources.getColor(R.color.nieve))
                //binding.appTiempo.textColor
            }else{
                binding.appTiempo.setBackgroundColor(resources.getColor(R.color.white))
                //binding.appTiempo.textColor
            }
        }
    }
}
