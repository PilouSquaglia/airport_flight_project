package com.example.airport_flight_project

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
import org.osmdroid.util.BoundingBox
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline
import kotlin.math.cos


@Suppress("DEPRECATION")
class MapFragment()  : Fragment() {
    private lateinit var osm: MapView
    private lateinit var mc: IMapController
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
        val ctx = activity?.applicationContext ?: return view
        mapViewModel = ViewModelProvider(requireActivity()).get(MapViewModel::class.java)
        mapViewModel.requestFlightList(context = ctx)

        Log.i("AAAAAAAAAAAAA##################################", "################################")
        //mapViewModel.requestPlanePosition(context = ctx)
        Log.i("AAAAAAAAAAAAA##################################", "AAAAAAAAAAAAAA################################")


        val view = inflater.inflate(R.layout.fragment_map, container, false)

        loadingIndicator = view.findViewById(R.id.loading_indicator_flight_map)
        loadingIndicator.visibility = View.VISIBLE

        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx))
        osm = view.findViewById(R.id.map)

        mc = osm.controller
        mc.setZoom(15.0)

        mapViewModel.getFlightLiveData().observe(viewLifecycleOwner, Observer { flight ->

            loadingIndicator.visibility = View.INVISIBLE
        })

        return view

    }

//    override fun onPause() {
//        super.onPause()
//        // Log.d("resume", "onPause: ")
//        // Enregistrez l'état actuel de la carte
//        val currentZoomLevel = osm.zoomLevelDouble
//        val currentCenter = osm.mapCenter
//
//        val sharedPreferences = activity?.getSharedPreferences("mapState", Context.MODE_PRIVATE)
//        sharedPreferences?.edit()?.apply {
//            putFloat("zoomLevel", currentZoomLevel.toFloat())
//            putString("centerLat", currentCenter.latitude.toString())
//            putString("centerLon", currentCenter.longitude.toString())
//            apply()
//        }
//    }
//
//    override fun onResume() {
//        super.onResume()
//        // Log.d("resume", "onResume: ")
//        // Restaurez l'état de la carte à partir des préférences partagées
//        val sharedPreferences = activity?.getSharedPreferences("mapState", Context.MODE_PRIVATE)
//        if (sharedPreferences != null) {
//            val zoomLevel = sharedPreferences.getFloat("zoomLevel", 5.0f).toDouble()
//            val centerLat = sharedPreferences.getString("centerLat", "0.0")?.toDouble() ?: 0.0
//            val centerLon = sharedPreferences.getString("centerLon", "0.0")?.toDouble() ?: 0.0
//
//            mc.setZoom(zoomLevel)
//            mc.setCenter(GeoPoint(centerLat, centerLon))
//        }
//    }

}