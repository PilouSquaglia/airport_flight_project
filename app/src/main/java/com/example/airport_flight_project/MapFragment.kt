package com.example.airport_flight_project

import android.content.Context
import android.graphics.Color
import android.location.LocationManager
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx))
        osm = view.findViewById(R.id.map)

        mc = osm.controller
        mc.setZoom(6)

        mapViewModel.getFlightTravelLiveData().observe(viewLifecycleOwner, Observer {

            val lstLatLng = ArrayList<Pair<Double, Double>>()

            for(el in it.path) {
                lstLatLng.add(Pair(el.get(1).toString().toDouble(), el.get(2).toString().toDouble()))
            }
           mapViewModel.setFlightDataPathLiveData(lstLatLng)
        })

        mapViewModel.getFlightDataPathLiveData().observe(viewLifecycleOwner, Observer {
            val polyline = Polyline(osm)
            val geoPoints = ArrayList<GeoPoint>()

            // affectation des paths
            for(el in it) {
                val latitude = el.first
                val longitude = el.second
                geoPoints.add(GeoPoint(latitude, longitude)) // Remplacez ceci par vos coordonnées
                // Log.d("Coordinates", "Latitude: $latitude, Longitude: $longitude")
            }

            // tracer la ligne
            polyline.setPoints(geoPoints)
            // Définissez la couleur de la ligne
            polyline.setColor(Color.BLUE)

            osm.overlays.add(polyline)

            // marqueur de debut
            val startMarker = Marker(osm)
            startMarker.position = geoPoints.first()
            mc.setCenter(geoPoints.first())
            startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            startMarker.title = "Departure"
            osm.overlays.add(startMarker)

            // marqueur de fin
            val endMarker = Marker(osm)
            endMarker.position = geoPoints.last()
            endMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            endMarker.title = "Arrivée"
            osm.overlays.add(endMarker)

            // focus
            // focus ineficace car après le back et return la carte est tout dézoomer
            //val boundingBox = BoundingBox.fromGeoPoints(geoPoints)
            //osm.zoomToBoundingBox(boundingBox, true)

            // Diminuez le niveau de zoom
//            osm.postDelayed({
//                val currentZoomLevel = osm.zoomLevelDouble
//                mc.setZoom(currentZoomLevel - 0.5)
//            }, 1000) // Attend 1 seconde avant de dézoomer

            // desactive le draw
            //osm.invalidate()
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