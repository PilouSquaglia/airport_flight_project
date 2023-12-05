package com.example.airport_flight_project

import android.graphics.Color
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline


@Suppress("DEPRECATION")
class MapFragment()  : Fragment() {
    private lateinit var osm: MapView
    private lateinit var mc: IMapController
    private lateinit var mapViewModel: MapViewModel
    private lateinit var loadingIndicator: ProgressBar
    private val markers: ArrayList<Marker> = ArrayList()
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

        val view = inflater.inflate(R.layout.fragment_map, container, false)

        loadingIndicator = view.findViewById(R.id.loading_indicator_flight_map)
        loadingIndicator.visibility = View.VISIBLE

        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx))
        osm = view.findViewById(R.id.map)

        mc = osm.controller
        mc.setZoom(7.0)

        mapViewModel.getFlightLiveData().observe(viewLifecycleOwner, Observer { flight ->
            // loadingIndicator.visibility = View.INVISIBLE
        })

        mapViewModel.getFlightTravelLiveData().observe(viewLifecycleOwner, Observer { plane ->

            print(plane)
            loadingIndicator.visibility = View.INVISIBLE
            drawFlightPath(plane.path)
        })

        return view

    }

    private fun drawFlightPath(path: List<List<Any>>) {
        clearMarkers()

        // Ajouter le marqueur de départ
        if (path.isNotEmpty()) {
            val startPoint = path.first()
            val startLatitude = startPoint[1] as Double
            val startLongitude = startPoint[2] as Double
            addMarker(startLatitude, startLongitude, "Départ")

        }
        // Ajouter le marqueur d'arrivée
        if (path.isNotEmpty()) {
            val endPoint = path.last()
            val endLatitude = endPoint[1] as Double
            val endLongitude = endPoint[2] as Double
            addMarker(endLatitude, endLongitude, "Arrivée")
        }

        // Ajouter le reste des marqueurs et connecter
        for (point in path) {
            val latitude = point[1] as Double
            val longitude = point[2] as Double
            addPoint(latitude, longitude)
        }

        connectMarkers()
        osm.controller.setCenter(markers.first().position)
    }

    private fun addMarker(latitude: Double, longitude: Double, title: String? = null) {
        val marker = Marker(osm)
        marker.position = GeoPoint(latitude, longitude)
        //marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
        if (title != null) {
            marker.title = title
        }
        osm.overlays.add(marker)
    }

    private fun addPoint(latitude: Double, longitude: Double) {
        val marker = Marker(osm)
        marker.position = GeoPoint(latitude, longitude)
        markers.add(marker)
    }

    private fun connectMarkers() {
        if (markers.size >= 2) {
            val line = Polyline(osm)
            line.setColor(Color.BLUE)
            line.setWidth(5f)
            line.setPoints(markers.map { it.position })

            osm.overlays.add(line)
        }
    }

    private fun clearMarkers() {
        osm.overlays.removeAll(markers)
        markers.clear()
    }

}
