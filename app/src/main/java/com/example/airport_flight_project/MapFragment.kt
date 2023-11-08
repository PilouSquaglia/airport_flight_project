package com.example.airport_flight_project

import android.location.LocationManager
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration
import org.osmdroid.views.MapView
import kotlin.math.cos


@Suppress("DEPRECATION")
class MapFragment()  : Fragment() {
    private lateinit var osm: MapView
    private lateinit var mc: IMapController
    private lateinit var locationManager: LocationManager
    private lateinit var mapViewModel: MapViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val context = requireActivity().applicationContext
        mapViewModel = ViewModelProvider(requireActivity()).get(MapViewModel::class.java)
        mapViewModel.requestFlightList(context =context)

        val flight = mapViewModel.getFlightTravelLiveData().value
        Log.d(TAG, "onCreateView: "+ flight)
        val view = inflater.inflate(R.layout.fragment_map, container, false)
        val ctx = activity?.applicationContext ?: return view
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx))
        osm = view.findViewById(R.id.map)

        mc = osm.controller
        mc.setZoom(15.0)


        return view

    }

    fun convertMetersToLatitudeDegrees(distanceMeters: Int): Double {

        val distanceKilometers = distanceMeters.toDouble()// / 1000 // Convertir en kilomètres
        val kilometersPerDegree = 111.139 // Approximation de la longueur d'un degré de latitude en kilomètres
        val degrees = distanceKilometers / kilometersPerDegree
        println("La distance de $distanceMeters mètres correspond à environ $degrees degrés de latitude.")
        return degrees // Convertir la distance en degrés de latitude

    }

    fun convertMetersToLongitudeDegrees(distanceMeters: Int, latitude: Double): Double {

        val distanceKilometers = distanceMeters.toDouble()// / 1000 // Convertir en mètres en kilomètres
        val kilometersPerDegree = 111.320 * cos(latitude) // Approximation de la longueur d'un degré de longitude en kilomètres
        val degrees = distanceKilometers / kilometersPerDegree
        println("La distance de $distanceMeters mètres correspond à environ $degrees degrés de longitude.")
        return degrees // Convertir la distance en degrés de longitude
    }


}