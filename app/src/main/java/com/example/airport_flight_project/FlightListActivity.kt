package com.example.airport_flight_project

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.commit
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.airport_flight_project.Utils.Companion.readJsonFromAssets

class FlightListActivity : AppCompatActivity() {

    private lateinit var viewModel: FlightViewModel


    private lateinit var mapViewModel: MapViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.flight_list_view)

        val airport = intent.getStringExtra("airport").toString()
        val depart = intent.getStringExtra("depart").toString()
        val arrivee = intent.getStringExtra("arrivee").toString()
        val airportSwitch = intent.getBooleanExtra("airportSwitch",false)

        val isTablet = findViewById<FragmentContainerView>(R.id.fragmentContainerViewMap) != null

        val jsonData = intent.getStringExtra("json_data").toString()
        Log.i("res", jsonData)

        viewModel = ViewModelProvider(this).get(FlightViewModel::class.java)

        viewModel.requestFlightList(airport = airport,depart = depart,arrivee= arrivee, airportSwitch= airportSwitch, context = this )

        // Utilisation de FragmentTransaction pour remplacer le FragmentContainerView
        supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView, ListOfFlightsFragment()).commit()

        val transaction = supportFragmentManager.beginTransaction()
        val fragment = ListOfFlightsFragment()
        transaction.replace(R.id.fragmentContainerView, fragment)
        transaction.addToBackStack(null)
        transaction.commit()

        viewModel.getFlightLiveData().observe(this, Observer { flightData ->

            mapViewModel = ViewModelProvider(this).get(MapViewModel::class.java)
            mapViewModel.setFlightLiveData(flightData)

            if(!isTablet) {
                val fragmentMap = MapFragment()
                this.supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerView, fragmentMap)
                    .addToBackStack(null)
                    .commit()
            }else{
                // ici on retrouve le second fragment
                val fragmentMap = MapFragment()
                this.supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerViewMap, fragmentMap)
                    .addToBackStack(null)
                    .commit()
            }
        })

    }
}