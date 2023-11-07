package com.example.airport_flight_project

import android.location.LocationManager
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline
import kotlin.math.cos


@Suppress("DEPRECATION")
class MapFragment(private var flights: FlightModel)  : Fragment() {
    private lateinit var osm: MapView
    private lateinit var mc: IMapController
    private lateinit var locationManager: LocationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_map, container, false)
        val ctx = activity?.applicationContext ?: return view
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx))
        osm = view.findViewById(R.id.map)

        mc = osm.controller
        mc.setZoom(15.0)

        // calcul de latitude et longitude
        val departLatitude = convertMetersToLatitudeDegrees(flights.estDepartureAirportVertDistance )
        val departLongitude = convertMetersToLongitudeDegrees(flights.estDepartureAirportHorizDistance,departLatitude)

        val arrivedLatitude = convertMetersToLatitudeDegrees(flights.estArrivalAirportVertDistance)
        val arrivedLongitude = convertMetersToLongitudeDegrees(flights.estArrivalAirportHorizDistance,arrivedLatitude)
        // Ajouter un marqueur pour le départ de l'avion
        val startMarker = Marker(osm)
        startMarker.position = GeoPoint(departLatitude, departLongitude)
        //startMarker.icon = ResourcesCompat.getDrawable(resources, R.drawable.ic_plane_start, null)
        osm.overlays.add(startMarker)

        // Ajouter un marqueur pour l'arrivée de l'avion
        val endMarker = Marker(osm)
        endMarker.position = GeoPoint(arrivedLatitude, arrivedLongitude)
        //endMarker.icon = ResourcesCompat.getDrawable(resources, R.drawable.ic_plane_end, null)
        osm.overlays.add(endMarker)

        // Relier les deux marqueurs par un trait
        val line = Polyline()
        line.addPoint(startMarker.position)
        line.addPoint(endMarker.position)
        osm.overlays.add(line)
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