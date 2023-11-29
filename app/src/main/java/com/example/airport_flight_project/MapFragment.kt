package com.example.airport_flight_project

import android.annotation.SuppressLint
import android.location.LocationManager
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
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
    private lateinit var loadingIndicator: ProgressBar

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

        loadingIndicator = view.findViewById(R.id.loading_indicator_flight_map)
        loadingIndicator.visibility = View.VISIBLE

        val ctx = activity?.applicationContext ?: return view
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx))
        osm = view.findViewById(R.id.map)

        mc = osm.controller
        mc.setZoom(15.0)

        mapViewModel.getFlightLiveData().observe(viewLifecycleOwner, Observer { flight ->

            loadingIndicator.visibility = View.INVISIBLE
        })

        return view

    }

}